package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada.EstadoJornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.JornadaRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.PartidoRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class JornadaService {
    private final JornadaRepository jornadaRepository;
    private final PartidoRepository partidoRepository;

    // Constructor para la inyección de dependencias
    public JornadaService(JornadaRepository jornadaRepository, PartidoRepository partidoRepository) {
        this.jornadaRepository = jornadaRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<Jornada> obtenerJornadas() {
        return jornadaRepository.findAll();
    }

    public Jornada obtenerJornada(Long id) {
        return jornadaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jornada no encontrada"));
    }

    public Jornada crearJornada(Jornada jornada) {
        // Validamos que la fechaini sea anterior a la fechafin
        if (jornada.getFechaini().isAfter(jornada.getFechafin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser posterior a la de fin");
        }

       
        
        //El parámetro que recibe la función contiene una jornada que ya tiene partidos
        //Asociamos estos partidos a la jornada que estamos creando
        if (jornada.getPartidos() != null) {
        for (Partido p : jornada.getPartidos()) {
            p.setJornada(jornada); 
        }
    }
    
         // Guardamos la jornada 
        Jornada jornadaGuardada = jornadaRepository.save(jornada);

        // Creamos 3 partidos vacíos
       /* for (int i = 0; i < 3; i++) {
            Partido partido = new Partido();
            partido.setJornada(jornadaGuardada); // Vinculamos el partido a la jornada recién creada
            partido.setEquipolocal("Equipo Local " + (i + 1)); // Nombres genéricos para que el admin los edite
            partido.setEquipovisitante("Equipo Visitante " + (i + 1));

            partidoRepository.save(partido);
        }*/ 

        return jornadaGuardada;

    }

    public void eliminarJornada(Long id) {
        if (!jornadaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se puede eliminar: Jornada no encontrada");
        }
        jornadaRepository.deleteById(id);
    }

    // Obtener jornada activa (solo debería haber una)
    public Optional<Jornada> obtenerJornadaActiva() {
        List<Jornada> activas = jornadaRepository.findByEstado(Jornada.EstadoJornada.ACTIVA);
        // Devolvemos la primera que encuentre, o vacío si no hay ninguna
        return activas.stream().findFirst();
    }

    public Jornada actualizarJornada(Long id, Jornada datosNuevos) {

        Jornada jornadaExistente = jornadaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jornada no encontrada"));

        if (datosNuevos.getFechaini().isAfter(datosNuevos.getFechafin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser posterior a la de fin");
        }

        jornadaExistente.setFechaini(datosNuevos.getFechaini());
        jornadaExistente.setFechafin(datosNuevos.getFechafin());
        jornadaExistente.setEstado(datosNuevos.getEstado());

        return jornadaRepository.save(jornadaExistente);
    }

    // Lógica automática


    // Si hay una jornada ACTIVA y ya ha pasado su fecha de inicio -> EN_CURSO
    @Transactional
    public void actualizarEstadosAutomaticos() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Jornada> activas = jornadaRepository.findByEstado(EstadoJornada.ACTIVA);
        for (Jornada j : activas) { //recorremos las jornadas activas, aunque solo debería haber una.
            if (ahora.isAfter(j.getFechaini())) {
                j.setEstado(EstadoJornada.EN_CURSO);
                jornadaRepository.save(j);
                System.out.println(">> Sistema: Jornada #" + j.getId() + " cerrada. Ahora EN CURSO.");
            }

        }
        // Si NO hay ninguna ACTIVA ni EN_CURSO, buscamos la siguiente para activar

    boolean hayAlguienJugando = !jornadaRepository.findByEstado(EstadoJornada.EN_CURSO).isEmpty();
    boolean hayAlguienActivo = !activas.isEmpty();

    if (!hayAlguienJugando && !hayAlguienActivo) {
        List<Jornada> planificadas = jornadaRepository.findByEstado(EstadoJornada.PLANIFICADA);
        planificadas.stream()
            .filter(j -> j.getFechaini().isAfter(ahora))
            .min(Comparator.comparing(Jornada::getFechaini))
            .ifPresent(proxima -> {
                proxima.setEstado(EstadoJornada.ACTIVA);
                jornadaRepository.save(proxima);
            });
    }
    }

    // LÓGICA MANUAL:
    // Para cuando se graben los resultados desde el panel admin
    // La jornada pasa a estado "JUGADA"
    @Transactional
    public Jornada registrarResultados(Long jornadaId, List<Partido> partidosConGoles) {
        // Buscamos la jornada
        Jornada jornada = jornadaRepository.findById(jornadaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jornada no encontrada"));

        // Actualizamos los goles de cada partido
        // Recorremos los partidos que nos llegan del frontend
        for (Partido pEnviado : partidosConGoles) {
            // Buscamos el partido real en la base de datos para asegurar que existe
            partidoRepository.findById(pEnviado.getId()).ifPresent(pReal -> {
                pReal.setGoleslocal(pEnviado.getGoleslocal());
                pReal.setGolesvisitante(pEnviado.getGolesvisitante());
                partidoRepository.save(pReal);
            });
        }

        // Cambiamos el estado de la jornada a JUGADA
        jornada.setEstado(Jornada.EstadoJornada.JUGADA);

        return jornadaRepository.save(jornada);
    }

}
