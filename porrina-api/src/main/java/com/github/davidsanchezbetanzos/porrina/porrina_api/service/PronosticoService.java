package com.github.davidsanchezbetanzos.porrina.porrina_api.service;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Partido;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Pronostico;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.PronosticoRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronosticoService {
    private final PronosticoRepository pronosticoRepository;
    //Inyección de dependencias
    public PronosticoService(PronosticoRepository pronosticoRepository) {
        this.pronosticoRepository = pronosticoRepository;
    }
    //guardar pronostico
    public Pronostico guardar(Pronostico pronostico) {
        // Antes de guardar siempre calculamos y escribimos el "1X2" del usuario
        pronostico.calcularQuiniela();
        return pronosticoRepository.save(pronostico);
    }

    @Transactional // Importante: o se guardan los 3 o ninguno
public List<Pronostico> guardarVarios(List<Pronostico> pronosticos) {
    for (Pronostico p : pronosticos) {
        // ¿Ya existe una apuesta de este usuario para este partido?
        Optional<Pronostico> existente = pronosticoRepository.buscarPorUsuarioYPartido(
            p.getUsuario().getId(), 
            p.getPartido().getId()
        );

        if (existente.isPresent()) {
            // Si existe, actualizamos los goles del que ya está en la DB
            Pronostico real = existente.get();
            real.setGolesLocal(p.getGolesLocal());
            real.setGolesVisitante(p.getGolesVisitante());
            // guardamos el pronostico
            this.guardar(real); 
        } else {
            // 3. Si es nuevo, lo guardamos directamente
            this.guardar(p);
        }
    }
    return pronosticos;
}



    // LISTAR: Obtiene los 3 pronósticos de un partido para un usuario y una jornada
    public List<Pronostico> obtenerPronosticosUsuarioEnJornada(Long usuarioId, Long jornadaId) {
        return pronosticoRepository.findByUsuario_IdAndPartido_Jornada_Id(usuarioId, jornadaId);
    }

    //  Calcular los puntos totales de un usuario dinámicamente
    public int calcularPuntosTotalesUsuario(Long usuarioId) {
        List<Pronostico> pronosticos = pronosticoRepository.findByUsuarioId(usuarioId);
        int puntosTotales = 0;

        for (Pronostico p : pronosticos) {
            int puntosPronostico = 0;
            Partido partidoreal = p.getPartido();
            if (partidoreal.getGoleslocal() == null || partidoreal.getGolesvisitante() == null) {
            continue; //si el partido no tiene goles, no suma ni devuelve nada pero sigue recorriendo la lista de pronosticos
        }
            // Regla +1: Acertar la quiniela (1, X, 2)
        if (p.getQuiniela().equals(partidoreal.getQuiniela())) {
            puntosPronostico += 1;
        }

        // Regla +5: Acertar el resultado exacto (sumamos 4 porque ya se ha sumado 1 en la anterior regla)
        if (p.getGolesLocal().equals(partidoreal.getGoleslocal()) && 
            p.getGolesVisitante().equals(partidoreal.getGolesvisitante())) {
            puntosPronostico += 4;
        }

            puntosTotales += puntosPronostico;
        }

        return puntosTotales;
    }
    //recuperar todos los pronosticos
    public List<Pronostico> listarTodos() {
        return pronosticoRepository.findAll();
    }

}
