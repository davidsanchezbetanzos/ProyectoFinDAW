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
        return usuarioRepository.findAll();
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
    return usuarioRepository.save(usuario);
}


public void eliminarUsuario(Long id) {
    if (!usuarioRepository.existsById(id)) {
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Usuario no encontrado"
        );
    }
        usuarioRepository.deleteById(id);
}


public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {

    Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado"
            ));

    usuarioExistente.setNick(datosNuevos.getNick());
    usuarioExistente.setEmail(datosNuevos.getEmail());

    return usuarioRepository.save(usuarioExistente);
}

}
