package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
