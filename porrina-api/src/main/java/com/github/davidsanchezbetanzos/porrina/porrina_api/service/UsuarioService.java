package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UsuarioService {

     private final UsuarioRepository usuarioRepository;

      public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public List<Usuario> obtenerUsuarios() {
        return List.of(
            new Usuario(1L, "david", "david@email.com"),
            new Usuario(2L, "pepe", "pepe@email.com")
        );
    }


    public Usuario obtenerUsuario(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> 
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado"
                )
            );
    }


    public Usuario crearUsuario(Usuario usuario) {
    // Simulación: asignamos un id fijo
    usuario.setId(3L);
    return usuario;
}

}
