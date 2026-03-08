package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Equipo;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.EquipoRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.UsuarioRepository;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.PronosticoService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipoService {
private final EquipoRepository equipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PronosticoService pronosticoService;

    public EquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository, PronosticoService pronosticoService) {
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pronosticoService = pronosticoService;
    }
    public List<Equipo> obtenerEquipos() {
        return equipoRepository.findAll();
    }

    public Equipo obtenerEquipo(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipo no encontrado"));
    }

    // crear ojbeto equipo y guardarlo en la bbdd
    public Equipo crearEquipo(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    // buscar objeto equipo por id y eliminarlo de la bbdd
    public void eliminarEquipo(Long id) {
        if (!equipoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Equipo no encontrado");
        }

        if (usuarioRepository.existsByEquipoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el equipo porque tiene usuarios asociados");
        }

        equipoRepository.deleteById(id);
    }

    //devuelve lista de usuarios que pertenecen a un equipo

    public List<Usuario> obtenerUsuariosDeEquipo(Long equipoId) {

    if (!equipoRepository.existsById(equipoId)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Equipo no encontrado"
        );
    }  

    return usuarioRepository.findByEquipoId(equipoId);
}

  public List<Equipo> obtenerClasificacionEquipos() {List<Equipo> equipos = equipoRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Equipo e : equipos) {
            // Filtramos usuarios de este equipo
            List<Usuario> miembros = usuarios.stream()
                .filter(u -> u.getEquipo() != null && u.getEquipo().getId().equals(e.getId()))
                .toList();

            // Sumamos los puntos de cada miembro
            int sumaPuntos = miembros.stream()
                .mapToInt(u -> pronosticoService.calcularPuntosTotalesUsuario(u.getId()))
                .sum();

            e.setPuntosTotales(sumaPuntos);
          
        }

        // Ordenamos por puntos
        equipos.sort((e1, e2) -> Integer.compare(e2.getPuntosTotales(), e1.getPuntosTotales()));
        
        return equipos;

}
}