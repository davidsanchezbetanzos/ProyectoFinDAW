package com.github.davidsanchezbetanzos.porrina.porrina_api.util;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario.Rol;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration //Esto hace que Spring detecta la clase al arrancar
public class IniciarDatos {
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            // Comprobamos si ya hay datos para no duplicarlos en cada reinicio
            if (usuarioRepository.count() == 0) {
                
                //  Creamos un Administrador de pruebas
                Usuario admin = new Usuario();
                admin.setNick("AdminPorrina");
                admin.setEmail("admin@test.com");
                admin.setNombre("David Administrador");
                admin.setRol(Rol.ADMIN); // Asignamos el rol ADMIN
                
                // Creamos un Usuarios normales de pruebas
                Usuario user = new Usuario();
                user.setNick("Pepiño");
                user.setEmail("pepe@test.com");
                user.setNombre("Pepe Usuario");
                // No hace falta setear el rol porque en la entidad ya pusimos "Rol.USER" por defecto


                Usuario user2 = new Usuario();
                user2.setNick("Juanin");
                user2.setEmail("juan@test.com");
                user2.setNombre("Juan Usuario");
                
                // 3. Persistimos (guardamos) en la base de datos
                usuarioRepository.save(admin);
                usuarioRepository.save(user);
                
                System.out.println("--------------------------------------------");
                System.out.println(">> BASE DE DATOS INICIALIZADA CON USUARIOS DE PRUEBA");
                System.out.println(">> Admin: admin@test.com");
                System.out.println(">> Pepiño: pepe@test.com");
                System.out.println(">> Juanin: juan@test.com");
                System.out.println("--------------------------------------------");
            } else {
                System.out.println(">> La base de datos ya contiene datos, saltando inicialización.");
            }
        };
    }
}
