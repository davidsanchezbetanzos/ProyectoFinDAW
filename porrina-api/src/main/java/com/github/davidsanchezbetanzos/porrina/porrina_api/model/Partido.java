package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "Partido")
public class Partido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  Aquí se crea la relacion columna 'jornada_id' en la base de datos.
    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    @JsonIgnoreProperties("partidos")
    @NotNull(message = "El partido debe estar asociado a una jornada")
    private Jornada jornada_id;

    @NotBlank(message = "El equipo local es obligatorio")
    private String equipolocal;

    @NotBlank(message = "El equipo visitante es obligatorio")
    private String equipovisitante;

   // mientras no se definan, los campos de goles estarán a null
    private Long goleslocal;
    private Long golesvisitante;

    // Almacenará "1", "X" o "2"
    @Pattern(regexp = "[1X2]", message = "La quiniela solo puede ser 1, X o 2")
    private String quiniela;

    /**
     Método para que se calcule la quiniela en base a los valores de goleslocal y golevisitante
     */
    public void calcularQuiniela() {
        //Si el partido no tiene aún datos de goles, el método mantiene el campo a null.
        if (this.goleslocal == null || this.golesvisitante == null) {
            this.quiniela = null;
        } else if (this.goleslocal > this.golesvisitante) {
            this.quiniela = "1";
        } else if (this.goleslocal < this.golesvisitante) {
            this.quiniela = "2";
        } else {
            this.quiniela = "X";
        }
    }

    //Constructor vacío
    public Partido() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Jornada getJornada() { return jornada_id; }
    public void setJornada(Jornada jornada) { this.jornada_id = jornada; }

    public String getEquipolocal() { return equipolocal; }
    public void setEquipolocal(String equipolocal) { this.equipolocal = equipolocal; }

    public String getEquipovisitante() { return equipovisitante; }
    public void setEquipovisitante(String equipovisitante) { this.equipovisitante = equipovisitante; }

    public Long getGoleslocal() { return goleslocal; }
    public void setGoleslocal(Long goleslocal) { this.goleslocal = goleslocal; }

    public Long getGolesvisitante() { return golesvisitante; }
    public void setGolesvisitante(Long golesvisitante) { this.golesvisitante = golesvisitante; }

    public String getQuiniela() { return quiniela; }
    public void setQuiniela(String quiniela) { this.quiniela = quiniela; }

}
