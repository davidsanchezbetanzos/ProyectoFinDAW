package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Pronostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    boolean existsByUsuario_IdAndPartido_Id(Long usuarioId, Long partidoId);

    // Busca todos los pronósticos de un usuario que pertenezcan a una jornada específica
    List<Pronostico> findByUsuario_IdAndPartido_Jornada_Id(Long usuarioId, Long jornadaId);

    List<Pronostico> findByPartido_Jornada_Id(Long jornadaId);

    //metodos con @query (si funcionan los de arriba habría que quitarlos)

    @Query("SELECT p FROM Pronostico p WHERE p.usuario.id = :uId AND p.partido.jornada.id = :jId")
    List<Pronostico> obtenerMisPronosticos(@Param("uId") Long uId, @Param("jId") Long jId);

    @Query("SELECT p FROM Pronostico p WHERE p.partido.jornada.id = :jId")
    List<Pronostico> obtenerTodosDeLaJornada(@Param("jId") Long jId);
    
    
    @Query("SELECT p FROM Pronostico p WHERE p.usuario.id = :uId AND p.partido.id = :pId")
    Optional<Pronostico> buscarPorUsuarioYPartido(@Param("uId") Long uId, @Param("pId") Long pId);

}
