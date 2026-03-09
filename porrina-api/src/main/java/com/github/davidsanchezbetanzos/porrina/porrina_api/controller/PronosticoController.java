package com.github.davidsanchezbetanzos.porrina.porrina_api.controller;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Pronostico;
import com.github.davidsanchezbetanzos.porrina.porrina_api.service.PronosticoService;

import jakarta.transaction.Transactional;

import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.PronosticoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/pronosticos")
@CrossOrigin(origins = "http://localhost:4200") // Para que Angular no tenga problemas de CORS
public class PronosticoController {
    private final PronosticoService pronosticoService;
    private final PronosticoRepository pronosticoRepository;
    //Inyección de dependencias
    public PronosticoController(PronosticoService pronosticoService, PronosticoRepository pronosticoRepository) {
        this.pronosticoService = pronosticoService;
        this.pronosticoRepository = pronosticoRepository;
    }


    //Sacar el pronostico de un usuario para un partido

    @GetMapping("/usuario/{usuarioId}/partido/{partidoId}")
public Pronostico obtenerPorUsuarioYPartido(@PathVariable Long usuarioId, @PathVariable Long partidoId) {
    // Método que ya definimos en el repository
    return pronosticoRepository.findByUsuarioIdAndPartidoId(usuarioId, partidoId).orElse(null);
}


    //Sacar los pronosticos de un usuario para toda la jornada
    @Transactional
    @GetMapping("/usuario/{usuarioId}/jornada/{jornadaId}")
    public List<Pronostico> obtenerMisPronosticos(
            @PathVariable Long usuarioId, 
            @PathVariable Long jornadaId) {
        return pronosticoService.obtenerPronosticosUsuarioEnJornada(usuarioId, jornadaId);
    }

    //sacar todos los pronosticos de una jornada
    @Transactional
    @GetMapping("/jornada/{jornadaId}/todos")
public List<Pronostico> obtenerTodosLosDeLaJornada(@PathVariable Long jornadaId) {    
    return pronosticoRepository.findByPartido_Jornada_Id(jornadaId);
}

   //Crear un nuevo pronóstico
    @PostMapping
    public Pronostico crear(@RequestBody Pronostico pronostico) {
        return pronosticoService.guardar(pronostico);
    }

//Guardar los 3 pronosticos de la jornada juntos
    @CrossOrigin(origins = "http://localhost:4200") 
@PostMapping("/guardar-varios")
public List<Pronostico> guardarVarios(@RequestBody List<Pronostico> pronosticos) {
    return pronosticoService.guardarVarios(pronosticos);
}

    //Modificar un pronostico existente (solo si la jornada esta en estado activa)

    @PutMapping("/{id}")
public Pronostico actualizar(@PathVariable Long id, @RequestBody Pronostico pronosticoActualizado) {
    // Buscamos el original
    Optional<Pronostico> pronosticoprevio = pronosticoRepository.findById(id);

    // Si existe, lo modificamos.
    if (pronosticoprevio.isPresent()) {
        Pronostico p = pronosticoprevio.get(); // Sacamos el objeto
        p.setGolesLocal(pronosticoActualizado.getGolesLocal());
        p.setGolesVisitante(pronosticoActualizado.getGolesVisitante());
        p.calcularQuiniela();
        
        // Devolvemos el nuevo 
        return pronosticoService.guardar(p);
    } else {
        // Si no existe devolvemos null
        return null;
    }
}


@DeleteMapping("/{id}")
public void borrar(@PathVariable Long id) {
    pronosticoRepository.deleteById(id);
}

    //Listar todos los pronósticos
    @GetMapping
    public List<Pronostico> listar() {
        return pronosticoService.listarTodos();
    }


    //Consultar los puntos de un usuario
    @GetMapping("/puntos/usuario/{usuarioId}")
    public int obtenerPuntosUsuario(@PathVariable Long usuarioId) {
        return pronosticoService.calcularPuntosTotalesUsuario(usuarioId);
    }
}
