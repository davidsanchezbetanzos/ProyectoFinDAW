package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.JornadaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {
    private final JornadaService jornadaService;

    // Inyección por constructor 
    public JornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public List<Jornada> obtenerTodas() {
        return jornadaService.obtenerJornadas();
    }

    @GetMapping("/{id}")
    public Jornada obtenerPorId(@PathVariable Long id) {
        return jornadaService.obtenerJornada(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Jornada crear(@Valid @RequestBody Jornada jornada) {
        if (jornada.getPartidos() != null) { //Si la jornada tiene partidos, los asociamos antes de guardar
        jornada.getPartidos().forEach(p -> p.setJornada(jornada));
    }
        return jornadaService.crearJornada(jornada);
    }

@PostMapping("/{id}/finalizar")
public Jornada finalizarJornada(@PathVariable Long id, @RequestBody List<Partido> resultados) {
    return jornadaService.registrarResultados(id, resultados);
}



    @PutMapping("/{id}")
    public Jornada actualizar(@PathVariable Long id, @Valid @RequestBody Jornada jornada) {
        return jornadaService.actualizarJornada(id, jornada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        jornadaService.eliminarJornada(id);
    }
    
}
