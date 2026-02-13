package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Equipo;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.EquipoRepository;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipoService {
    private final EquipoRepository equipoRepository;

     public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }


public List<Equipo> obtenerEquipos() {
    return equipoRepository.findAll();
}


public Equipo obtenerEquipo(Long id) {
    return equipoRepository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Equipo no encontrado"
                    )
            );
}
//crear ojbeto equipo y guardarlo en la bbdd
public Equipo crearEquipo(Equipo equipo) {
    return equipoRepository.save(equipo);
}

//buscar objeto equipo por id y eliminarlo de la bbdd
public void eliminarEquipo(Long id) {
    if (!equipoRepository.existsById(id)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Equipo no encontrado"
        );
    }
    equipoRepository.deleteById(id);
}




}
