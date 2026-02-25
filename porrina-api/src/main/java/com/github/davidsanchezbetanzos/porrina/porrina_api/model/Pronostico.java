package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "Pronostico")
public class Pronostico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //varios pronosticos se asignan a 1 usuario
    @JoinColumn(name = "usuario_id", nullable = false) //join con usuario. no puede ser null
    @JsonIgnoreProperties({"pronosticos", "password"}) //para evitar json infinitos
    private Usuario usuario;

    @ManyToOne //varios pronosticos de diferentes usuarios corresponden a 1 partido
    @JoinColumn(name = "partido_id", nullable = false) 
    @JsonIgnoreProperties("jornada")
    private Partido partido;

    @NotNull(message = "Los goles locales son obligatorios")
    private Long golesLocal;

    @NotNull(message = "Los goles visitantes son obligatorios")
    private Long golesVisitante;

    // Este campo se llena automáticamente con "1", "X" o "2"
    private String quiniela;

    /**
     * Calcula el signo de la quiniela basado en los goles.
     * Se debe invocar en el Service antes de hacer el save().
     */
    public void calcularQuiniela() {
        if (this.golesLocal == null || this.golesVisitante == null) {
            this.quiniela = null;
        } else if (this.golesLocal > this.golesVisitante) {
            this.quiniela = "1";
        } else if (this.golesLocal < this.golesVisitante) {
            this.quiniela = "2";
        } else {
            this.quiniela = "X";
        }
    }

    //Constructor vacío
    public Pronostico() {}

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }

    public Long getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Long golesLocal) { this.golesLocal = golesLocal; }

    public Long getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Long golesVisitante) { this.golesVisitante = golesVisitante; }

    public String getQuiniela() { return quiniela; }
    public void setQuiniela(String quiniela) { this.quiniela = quiniela; }
}
    

