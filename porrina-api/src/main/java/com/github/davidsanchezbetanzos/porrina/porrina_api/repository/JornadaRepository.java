package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada.EstadoJornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface JornadaRepository extends JpaRepository<Jornada, Long> {
    
     List<Jornada> findByEstado(EstadoJornada estado);

     List<Partido> findByPartidos_Jornada_Id (Long jornada);
}