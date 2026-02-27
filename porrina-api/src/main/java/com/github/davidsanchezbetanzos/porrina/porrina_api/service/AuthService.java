package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias.
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario procesarLoginGoogle(String email, String nombre) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);

        if (usuarioExistente.isPresent()) {
            return usuarioExistente.get();
        } else {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setNombre(nombre);
            // El rol ya viene como USER por defecto en el model de la entidad
            return usuarioRepository.save(nuevoUsuario);
        }
    }
    
    public Usuario completarRegistro(String email, String nick) {
        if (usuarioRepository.existsByNick(nick)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "El nick ya existe");
    }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        usuario.setNick(nick);
        return usuarioRepository.save(usuario);
    }
}
    



