package com.github.davidsanchezbetanzos.porrina.porrina_api.util;

import com.github.davidsanchezbetanzos.porrina.porrina_api.model.*;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Jornada.EstadoJornada;
import com.github.davidsanchezbetanzos.porrina.porrina_api.model.Usuario.Rol;
import com.github.davidsanchezbetanzos.porrina.porrina_api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class IniciarDatos {

    @Bean
    @Transactional // Para que se haga como un solo insert
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   EquipoRepository equipoRepository,
                                   JornadaRepository jornadaRepository,
                                   PartidoRepository partidoRepository,
                                   PronosticoRepository pronosticoRepository) { 
        return args -> {
            if (usuarioRepository.count() == 0) {
                Random random = new Random();

                // Creamnos un admin  
                Usuario admin = new Usuario();
                admin.setNick("AdminPorrina");
                admin.setEmail("admin@test.com");
                admin.setNombre("David Administrador");
                admin.setRol(Rol.ADMIN);
                usuarioRepository.save(admin);

                //  EQUIPOS MOCK
                List<String> nombresEquipos = List.of("Os que faltaban", "Doentes polo caldo", "Berzotas", "Aquí no valía", "Orden y talento", "Diamond chukeles");
                List<Equipo> equipos = new ArrayList<>();
                for (String n : nombresEquipos) {
                    Equipo e = new Equipo();
                    e.setNombre(n);
                    equipos.add(equipoRepository.save(e));
                }

                // 30 USUARIOS
                List<String> nombresBase = List.of("David", "Fran", "Mauro", "Donato", "Bebeto", "Djalminha", "Valerón", "Naybet", "Tristán", "Scaloni");
                List<String> prefijos = List.of("ElMago", "Capitan", "Super", "Muro", "Tanque", "Pichichi", "Lince");
                List<Usuario> todosLosUsuarios = new ArrayList<>();

                for (int i = 0; i < 30; i++) {
                    Usuario u = new Usuario();
                    String nombreReal = nombresBase.get(random.nextInt(nombresBase.size()));
                    u.setNombre(nombreReal + " " + (i + 1));
                    u.setEmail("user" + i + "@test.com");
                    u.setNick(prefijos.get(random.nextInt(prefijos.size())) + nombreReal + (i + 10));
                    u.setPagado(i < 20);
                    //el rol ya es user por defecto en el modelo
                    if (random.nextBoolean()) {
                        u.setEquipo(equipos.get(random.nextInt(equipos.size())));
                    }
                    todosLosUsuarios.add(usuarioRepository.save(u));
                } 

                //  LISTAS DE EQUIPOS PARA GENERAR PARTIDOS
                List<String> rivalesDepor = List.of("Celta", "Real Madrid", "Barcelona", "Atletico", "Sevilla", "Betis");
                List<String> filialesYFemenino = List.of("Fabril", "Depor ABANCA");
                List<String> rivalesEspeciales = List.of("Coruxo", "Bergantiños", "Alhama CF", "Valencia Fem");
                List<String> equiposMundo = List.of("Bayern", "Liverpool", "PSG", "Milan", "Inter", "Arsenal");

                // GENERAMOS 10 JORNADAS PASADAS
                for (int j = 1; j <= 10; j++) {
                    Jornada jor = new Jornada();
                    jor.setFechaini(LocalDateTime.now().minusWeeks(11 - j).withHour(10).withMinute(0));
                    jor.setFechafin(LocalDateTime.now().minusWeeks(11 - j).plusDays(2).withHour(22).withMinute(0));
                    jor.setEstado(EstadoJornada.JUGADA);
                    jornadaRepository.save(jor);
                     //CREAMOS LOS 3 PARTIDOS DE CADA JORNADA
                    for (int p = 1; p <= 3; p++) { 
                        Partido partido = new Partido();
                        partido.setJornada(jor);

                        if (p == 1) { //Partido del depor contra un rival
                            String rival = rivalesDepor.get(random.nextInt(rivalesDepor.size()));
                            if (random.nextBoolean()) {
                                partido.setEquipolocal("Deportivo");
                                partido.setEquipovisitante(rival);
                            } else {
                                partido.setEquipolocal(rival);
                                partido.setEquipovisitante("Deportivo");
                            }
                        } else if (p == 2) { //partido del abanca o del fabril
                            String especial = filialesYFemenino.get(random.nextInt(filialesYFemenino.size()));
                            String rival = rivalesEspeciales.get(random.nextInt(rivalesEspeciales.size()));
                            if (random.nextBoolean()) {
                                partido.setEquipolocal(especial);
                                partido.setEquipovisitante(rival);
                            } else {
                                partido.setEquipolocal(rival);
                                partido.setEquipovisitante(especial);
                            }
                        } else { //otro partido internacional
                            String e1 = equiposMundo.get(random.nextInt(equiposMundo.size()));
                            String e2;
                            do { e2 = equiposMundo.get(random.nextInt(equiposMundo.size())); } while (e1.equals(e2)); //rerolleamos el random si el equipo que sale es el mismo.
                            partido.setEquipolocal(e1);
                            partido.setEquipovisitante(e2);
                        }
                        //asignamos goles, calculamos quinela y persistimos el partido
                        partido.setGoleslocal((long) random.nextInt(4));
                        partido.setGolesvisitante((long) random.nextInt(4));
                        partido.calcularQuiniela();
                        partidoRepository.save(partido);

                        // Para cada partido ya jugado, generamos un pronóstico para cada usuaro.
                        for (Usuario u : todosLosUsuarios) {
                            Pronostico pr = new Pronostico();
                            pr.setUsuario(u);
                            pr.setPartido(partido);
                            pr.setGolesLocal((long) random.nextInt(4));
                            pr.setGolesVisitante((long) random.nextInt(4));
                            pr.calcularQuiniela();
                            pronosticoRepository.save(pr);
                        }//Cierre bucle pronósticos
                    } // Cierre bucle partidos
                } // Cierre bucle jornadas

                System.out.println(">> BASE DE DATOS INICIALIZADA");
            } else {
                System.out.println(">> La base de datos ya contiene datos.");
            }
        }; // Cierre lambda args
    } // Cierre método initDatabase
} // Cierre clase IniciarDatos