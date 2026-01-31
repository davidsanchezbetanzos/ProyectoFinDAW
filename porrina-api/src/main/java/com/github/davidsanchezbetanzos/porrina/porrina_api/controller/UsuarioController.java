package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.UsuarioService;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//estados de error
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//metodos post
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;


//-- ENDPOINTS

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
//-- GET LISTADO DE USUARIOS /api/usuarios
    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.obtenerUsuarios();
    }
//-- GET USUARIO POR ID /api/usuarios/{id}
    @GetMapping("/{id}")
    public Usuario getUsuarioPorId(@PathVariable Long id) {

        Usuario usuario = usuarioService.obtenerUsuario(id);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");

        }

        return usuario;
    }

//-- POST USUARIO /api/usuarios/ (EN EL BODY VA EL USUARIO)
    @PostMapping
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

//-- DELETE USUARIO /api/usuarios/{id} (EN EL BODY VA EL ID)

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void eliminarUsuario(@PathVariable Long id) {
    usuarioService.eliminarUsuario(id);
} 

//-- PUT (UPDATE) USUARIO 
@PutMapping("/{id}")
public Usuario actualizarUsuario(
        @PathVariable Long id,
        @Valid @RequestBody Usuario usuarioActualizado
) {
    return usuarioService.actualizarUsuario(id, usuarioActualizado);
}

}
