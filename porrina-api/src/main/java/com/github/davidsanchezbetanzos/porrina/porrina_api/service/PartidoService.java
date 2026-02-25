package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.PartidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PartidoService {
    private final PartidoRepository partidoRepository;

    // inyección de dependencias
    public PartidoService(PartidoRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }

    public List<Partido> obtenerTodos() {
        return partidoRepository.findAll();
    }

    public List<Partido> obtenerPorJornada(Long jornadaId) {
        return partidoRepository.findByJornadaId(jornadaId);
    }

    public Partido obtenerPorId(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Partido no encontrado"));
    }

    public Partido crearPartido(Partido partido) {
        // Antes de guardar, calculamos el campo quiniela por si el partido se crease ya
        // con datos de goles.
        partido.calcularQuiniela();
        return partidoRepository.save(partido);
    }

    // Método para actualizar el resultado, escribiendo los goles y calculando el
    // campo quiniela
    public Partido actualizarResultado(Long id, Long golesLocal, Long golesVisitante) {
        Partido partido = obtenerPorId(id);

        partido.setGoleslocal(golesLocal);
        partido.setGolesvisitante(golesVisitante);

        // La propia entidad se encarga de decidir si es "1", "X" o "2"
        partido.calcularQuiniela();

        return partidoRepository.save(partido);
    }

    // Método para actualizar el "partido entero" recibiendo un objeto partido
    // entero y no solo el resultado

    public Partido actualizarPartido(Long id, Partido datosNuevos) {
        // Verificamos que existe (lanza 404 si no)
        Partido partidoExistente = obtenerPorId(id);

        // Actualizamos solo los campos permitidos
        partidoExistente.setEquipolocal(datosNuevos.getEquipolocal());
        partidoExistente.setEquipovisitante(datosNuevos.getEquipovisitante());
        partidoExistente.setGoleslocal(datosNuevos.getGoleslocal());
        partidoExistente.setGolesvisitante(datosNuevos.getGolesvisitante());

        // Recalculamos la quiniela (1X2) basándonos en los nuevos goles
        partidoExistente.calcularQuiniela();

        // 4. Guardamos los cambios
        return partidoRepository.save(partidoExistente);
    }

    public void eliminarPartido(Long id) {
        if (!partidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partido no encontrado");
        }
        partidoRepository.deleteById(id);
    }

}
