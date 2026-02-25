package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Pronostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PronosticoRepository extends JpaRepository<Pronostico, Long> {

    // Obtener todas las apuestas de un usuario
    List<Pronostico> findByUsuarioId(Long usuarioId);

    // Ver quién ha apostado en un partido concreto
    List<Pronostico> findByPartidoId(Long partidoId);

    // Para comprobar si un usuario ya ha apostado a determinado partido.
    Optional<Pronostico> findByUsuarioIdAndPartidoId(Long usuarioId, Long partidoId);

    // Lo mismo sin traer todo el objeto (más rápido)
    boolean existsByUsuarioIdAndPartidoId(Long usuarioId, Long partidoId);
}
