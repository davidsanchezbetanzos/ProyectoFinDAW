package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.EquipoRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;
    private final PronosticoService pronosticoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
            EquipoRepository equipoRepository, PronosticoService pronosticoService) {
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
        this.pronosticoService = pronosticoService;
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

            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un usuario con ese email"
        );
    }

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
//validacion de que no se cambia el mail a 1 ya existente. comprueba que el email proporcionado no este en bd (a no ser que no lo estamos cambiando)
            if (!usuarioExistente.getEmail().equals(datosNuevos.getEmail()) && usuarioRepository.existsByEmail(datosNuevos.getEmail())) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un usuario con ese email"
        );
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


    // Método para obtener la clasificación
    public List<Usuario> obtenerClasificacion() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Calculamos los puntos para cada usuario
        for (Usuario u : usuarios) {
            int puntos = pronosticoService.calcularPuntosTotalesUsuario(u.getId());
            u.setPuntos(puntos); // Seteamos el valor en el campo temporal
        }

        // Ordenamos la lista de mayor a menor puntuación
        // Sort va cogiendo parejas de elementos de la lista y comparandolos como definimos despues de la flecha. 
        // Invirtiendo el orden hacemos que se ordene "de más a menos puntos"
        usuarios.sort((u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));

        return usuarios;
    }

}
