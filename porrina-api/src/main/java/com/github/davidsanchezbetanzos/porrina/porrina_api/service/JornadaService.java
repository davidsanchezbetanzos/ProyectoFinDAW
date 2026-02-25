package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.JornadaRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.PartidoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class JornadaService {
    private final JornadaRepository jornadaRepository;
    private final PartidoRepository partidoRepository; 

    // Constructor para la inyección de dependencias
    public JornadaService(JornadaRepository jornadaRepository,PartidoRepository partidoRepository) {
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
        //Validamos que la fechaini sea anterior a la fechafin
        if (jornada.getFechaini().isAfter(jornada.getFechafin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser posterior a la de fin");
        }
       //Guardamos la jornada primero para que tenga un ID asignado
        Jornada jornadaGuardada = jornadaRepository.save(jornada);

        //Creamos 3 partidos vacíos
        for (int i = 0; i < 3; i++) {
            Partido partido = new Partido();
            partido.setJornada(jornadaGuardada); // Vinculamos el partido a la jornada recién creada
            partido.setEquipolocal("Equipo Local " + (i + 1)); // Nombres genéricos para que el admin los edite
            partido.setEquipovisitante("Equipo Visitante " + (i + 1));
            
            partidoRepository.save(partido);
        }

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
}
