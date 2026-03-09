package com.github.davidsanchezbetanzos.porrina.porrina_api.scheduler;

import com.github.davidsanchezbetanzos.porrina.porrina_api.service.JornadaService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JornadaScheduler {

    private final JornadaService jornadaService;

    public JornadaScheduler(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }
    //se ejecuta al inicio
    @EventListener(ApplicationReadyEvent.class)
    public void ejecutarAlInicio() {
        System.out.println(">> Sistema: App lista. Ejecutando comprobación inicial de jornadas...");
        jornadaService.actualizarEstadosAutomaticos();
    }

    // Se ejecuta cada hora para revisar si hay que activar la siguiente jornada
    // Cron: segundos minutos horas día mes día_semana
   @Scheduled(cron = "0 0 * * * *") 
public void revisarCambioDeEstado() {
    jornadaService.actualizarEstadosAutomaticos();
}
}