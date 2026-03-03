package com.github.davidsanchezbetanzos.porrina.porrina_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //consulta SELECT 1 FROM usuario WHERE equipo_id = ? y devuelve true si al menos existe 1 usuario con ese equipoID
    boolean existsByEquipoId(Long equipoId);
    //lo mismo con el email
    boolean existsByEmail(String email);

    //sacar todos los usuarios ordenados por ID
     List<Usuario> findAllByOrderByIdAsc();


    //select para sacar los usuarios que tengan un determinado equipo   
    List<Usuario> findByEquipoId(Long equipoId);

    // select para buscar por nick
    boolean existsByNick(String nick);

     //select para sacar los usuarios que tengan un determinado equipo   
    Optional<Usuario> findByEmail(String email);

    // select para sacar si un usuario ha pagado

    //boolean isPagado(Long id);
    
}
