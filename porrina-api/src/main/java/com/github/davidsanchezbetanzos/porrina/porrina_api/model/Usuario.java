package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

//validaciones
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.persistence.Column;
//persistencia
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(unique = true)   
    private String nick;

    // El email será nuestro id de google para Oauth
    @Column(unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    // Guardaremos el nombre real que nos devuelva Google
    private String nombre;

    // Para diferenciar administradores, definimos un enum para que solo pueda tener 2 valores
    public enum Rol {
    ADMIN, USER
}
    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USER; //Los usuarios tendran rol USER por defecto

    // Campo que no se persiste a base de datos pero lo usamos en el service para
    // calcular la clasificacion
    @Transient
    private int puntos;

    // Constructor con campos
    public Usuario(String nick, String email, String nombre, Rol rol) {
        this.nick = nick;
        this.email = email;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Constructor vacío
    public Usuario() {
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

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
