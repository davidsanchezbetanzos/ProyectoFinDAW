package com.github.davidsanchezbetanzos.porrina.porrina_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class PorrinaApiApplication {

	private static final Logger log = LoggerFactory.getLogger(PorrinaApiApplication.class);

	public static void main(String[] args) {
		log.info("Arranca el main");
		
		SpringApplication.run(PorrinaApiApplication.class, args);
		log.info("Acaba el main");
		
	}

}
