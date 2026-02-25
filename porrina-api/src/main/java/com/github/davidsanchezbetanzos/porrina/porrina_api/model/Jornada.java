package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "Jornada")
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaini;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechafin;

    @Enumerated(EnumType.STRING)
    private EstadoJornada estado = EstadoJornada.PLANIFICADA;


    public enum EstadoJornada {
        PLANIFICADA, //Planificada pero no activa
        ACTIVA,   // Se pueden crear pronósticos
        JUGADA,   // Partidos terminados, puntos calculados
        CANCELADA //jornada cancelada
    }

    // Relación OneToMany: Una jornada tiene muchos partidos.
    // mappedBy indica que la relación se gestiona en el atributo "jornada" de la clase Partido.
    @OneToMany(mappedBy = "jornada_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("jornada")
    private List<Partido> partidos = new ArrayList<>();

    // Constructor vacío 
    public Jornada() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaini() {
        return fechaini;
    }

    public void setFechaini(LocalDateTime fechaini) {
        this.fechaini = fechaini;
    }

    public LocalDateTime getFechafin() {
        return fechafin;
    }

    public void setFechafin(LocalDateTime fechafin) {
        this.fechafin = fechafin;
    }

    public EstadoJornada getEstado() {
        return estado;
    }

    public void setEstado(EstadoJornada estado) {
        this.estado = estado;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
    

}
