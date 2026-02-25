package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

//validaciones
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

//persistencia
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;

//Clase Java para usuario
@Entity // marcamos esta clase como una entidad (tabla) en la base de datos
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relacion con entidad Equipo
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @NotBlank(message = "El nick es obligatorio")
    private String nick;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    // Campo que no se persiste a base de datos pero lo usamos en el service para
    // calcular la clasificacion
    @Transient
    private int puntos;

    // Constructor con campos
    public Usuario(long id, String nick, String email) {
        this.id = id;
        this.nick = nick;
        this.email = email;
    }

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con campos
    public Usuario(Long id, String nick, String email) {
        this.id = id;
        this.nick = nick;
        this.email = email;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getPuntos() {
        return puntos;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

}
