package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

//persistencia
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

//import para fix de json infinito (quitar cuando implementemos DTO)
import com.fasterxml.jackson.annotation.JsonIgnore;

//Clase Java para usuario
@Entity // marcamos esta clase como una entidad (tabla) en la base de datos
@Table(name = "Equipo")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de equipo es obligatorio")
    private String nombre;

    @Transient
    private int puntosTotales;

    @JsonIgnore //quitar cuando implementemos DTO
    @OneToMany(mappedBy = "equipo")
    private List<Usuario> usuarios = new ArrayList<>();

    

    // Constructor vacío
    public Equipo() {
    }

    // Constructor con campos
    public Equipo(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosTotales(){
        return puntosTotales;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuntosTotales(int puntos){
        this.puntosTotales = puntos;
    }

}
