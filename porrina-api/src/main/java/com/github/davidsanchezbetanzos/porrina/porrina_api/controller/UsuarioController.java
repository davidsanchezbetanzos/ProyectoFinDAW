package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.UsuarioService;

//marcadores de spring
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//metodos para listas
import java.util.List;

//pasar variables desde la URL
import org.springframework.web.bind.annotation.PathVariable;

//estados de error
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.obtenerUsuarios();
    }


    @GetMapping("/{id}")
    public Usuario getUsuarioPorId(@PathVariable Long id){

        Usuario usuario = usuarioService.obtenerUsuario(id);

        if (usuario == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado");

        }


        return usuario;
    }
}
