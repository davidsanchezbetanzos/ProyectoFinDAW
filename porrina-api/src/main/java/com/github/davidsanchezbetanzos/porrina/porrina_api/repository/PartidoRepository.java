package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    
    
    List<Partido> findByJornadaId(Long jornadaId);
}