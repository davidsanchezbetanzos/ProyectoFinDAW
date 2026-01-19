package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {
    
    public List<Usuario> obtenerUsuarios() {
        return List.of(
            new Usuario(1L, "david", "david@email.com"),
            new Usuario(2L, "pepe", "pepe@email.com")
        );
    }
}
