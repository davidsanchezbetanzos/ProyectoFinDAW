package com.github.davidsanchezbetanzos.porrina.porrina_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
public class PingController {

    private static final Logger log = LoggerFactory.getLogger(PingController.class);

    @GetMapping("/api/ping")
    public Map<String, String> ping() {
        log.info("Entrando en PingController /api/ping");
        return Map.of("status", "ok");
    }
}