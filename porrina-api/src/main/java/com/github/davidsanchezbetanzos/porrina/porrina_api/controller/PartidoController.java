package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.PartidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    // Inyección por constructor
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public List<Partido> obtenerTodos() {
        return partidoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Partido obtenerPorId(@PathVariable Long id) {
        return partidoService.obtenerPorId(id);
    }

    // (jornadas/{jornadaId} recuperala jornada pero /jornada/{jornadaId} recupera
    // los partidos de esa jornada )
    @GetMapping("/jornada/{jornadaId}")
    public List<Partido> obtenerPorJornada(@PathVariable Long jornadaId) {
        return partidoService.obtenerPorJornada(jornadaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Partido crear(@Valid @RequestBody Partido partido) {
        return partidoService.crearPartido(partido);
    }

    // Endpoint específico para actualizar SOLO el resultado
  @PutMapping("/{id}/resultado")
public Partido actualizarResultado(@PathVariable Long id, @RequestBody java.util.Map<String, Long> body) {
    // Sacamos los valores del mapa usando las llaves del JSON
    Long golesLocal = body.get("golesLocal");
    Long golesVisitante = body.get("golesVisitante");
    
    return partidoService.actualizarResultado(id, golesLocal, golesVisitante);
}
    // Modificar o crear partido a partir de un objeto partido.
    @PutMapping("/{id}")
    public Partido actualizar(@PathVariable Long id, @Valid @RequestBody Partido partido) {
        return partidoService.actualizarPartido(id, partido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        partidoService.eliminarPartido(id);
    }

}
