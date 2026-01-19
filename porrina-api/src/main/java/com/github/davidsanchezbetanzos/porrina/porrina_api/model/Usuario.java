package com.github.davidsanchezbetanzos.porrina.porrina_api.model;

//Clase Java para usuario

public class Usuario {
    private long id;
    private String nick;
    private String email;

    public Usuario(long id, String nick, String email){
        this.id = id;
        this.nick = nick;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

}
