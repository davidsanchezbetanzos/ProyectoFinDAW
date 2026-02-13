package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.EquipoRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
            EquipoRepository equipoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuario.getEquipo() != null) {

            Long equipoId = usuario.getEquipo().getId();

            if (equipoId == null || !equipoRepository.existsById(equipoId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El equipo especificado no existe");
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        if (datosNuevos.getEquipo() != null) {
            Long equipoId = datosNuevos.getEquipo().getId();

            if (equipoId == null || !equipoRepository.existsById(equipoId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El equipo especificado no existe");

            }

            usuarioExistente.setEquipo(datosNuevos.getEquipo());

        }else {
        // Si explícitamente viene null, quitamos el equipo
        usuarioExistente.setEquipo(null);
        
    }

            usuarioExistente.setNick(datosNuevos.getNick());
            usuarioExistente.setEmail(datosNuevos.getEmail());

            return usuarioRepository.save(usuarioExistente);
        

    }

}
