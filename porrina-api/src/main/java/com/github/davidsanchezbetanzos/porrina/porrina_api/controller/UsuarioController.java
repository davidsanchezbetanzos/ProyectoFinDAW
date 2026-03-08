package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.UsuarioService;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
//marcadores de spring
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//metodos para listas
import java.util.List;

//pasar variables desde la URL
import org.springframework.web.bind.annotation.PathVariable;

//metodos rest
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//estados de error
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

//-- ENDPOINTS

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    // -- GET LISTADO DE USUARIOS del repository
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllByOrderByIdAsc();
    }

    // GET CLASIFICACION (LISTA DE USUARIOS ORDENADOS CON SUS PUNTOS)

    @GetMapping("/clasificacion")
    public List<Usuario> getClasificacion() {
        return usuarioService.obtenerClasificacion();
    }

    // -- GET USUARIO POR ID /api/usuarios/{id}
    @GetMapping("/{id}")
    public Usuario getUsuarioPorId(@PathVariable Long id) {

        Usuario usuario = usuarioService.obtenerUsuario(id);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");

        }

        return usuario;
    }

        // sacar el objeto usuario buscando por email
    @GetMapping("/perfil/{email}")
    public Usuario obtenerPerfil(@PathVariable String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    // -- POST USUARIO /api/usuarios/     
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    // -- DELETE USUARIO /api/usuarios/{id} (Sacamos el id de pathvariable)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

    // -- PUT (UPDATE) USUARIO
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuarioActualizado) {
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
    }

    // -- TOGGLE PARA CAMBIAR EL ESTADO "PAGADO" - "PENDIENTE DE PAGO"

    @PutMapping("/{id}/toggle-pago")
    public Usuario togglePago(@PathVariable Long id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPagado(!user.isPagado()); // Si es false lo pone a true, y viceversa
        return usuarioRepository.save(user);
    }



}
