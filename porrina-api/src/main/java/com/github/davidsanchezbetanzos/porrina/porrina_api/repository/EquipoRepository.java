package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Equipo;


public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    
}
