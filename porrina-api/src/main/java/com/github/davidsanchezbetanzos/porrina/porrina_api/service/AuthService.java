package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
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
    
    public Usuario completarRegistro(Long id, String nick) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setNick(nick);
        return usuarioRepository.save(usuario);
    }
}
    



