package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;


import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Equipo;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/equipos")
public class EquipoController {

    //inyección de dependencias

    private final EquipoService equipoService;

public EquipoController(EquipoService equipoService) {
    this.equipoService = equipoService;
}

   @GetMapping
    public List<Equipo> obtenerEquipos() {
        return equipoService.obtenerEquipos();
    }

     @GetMapping("/{id}")
    public Equipo obtenerEquipo(@PathVariable Long id) {
        return equipoService.obtenerEquipo(id);
    }

    @GetMapping("/{id}/usuarios")
public List<Usuario> obtenerUsuariosDeEquipo(@PathVariable Long id) {
    return equipoService.obtenerUsuariosDeEquipo(id);
}


     @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipo crearEquipo(@Valid @RequestBody Equipo equipo) {
        return equipoService.crearEquipo(equipo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
    }
}
