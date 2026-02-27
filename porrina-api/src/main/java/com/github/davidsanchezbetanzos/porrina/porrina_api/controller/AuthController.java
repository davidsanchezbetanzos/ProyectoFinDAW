package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:4200") //para que acepte peticiones de angular.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Inyección de dependencias
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    

    /**
     * Este endpoint simula lo que pasará cuando Google nos devuelva los datos.
     * URL de prueba: http://localhost:8080/api/auth/test-google?email=algo@test.com&nombre=David
     */
    @GetMapping("/test-google")
    public Usuario testLogin(@RequestParam String email, @RequestParam String nombre) {
        System.out.println(">> Simulando entrada de Google para: " + email);
        return authService.procesarLoginGoogle(email, nombre);
    }

//endpoint para completar el registro añadiendo un nick
@PostMapping("/completar-registro") 
public Usuario completarRegistro(@RequestParam String email, @RequestParam String nick) {
    return authService.completarRegistro(email, nick);
}

}
