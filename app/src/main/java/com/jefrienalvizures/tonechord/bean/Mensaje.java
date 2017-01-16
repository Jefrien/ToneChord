package com.jefrienalvizures.tonechord.bean;

import java.io.Serializable;

/**
 * Created by Jefrien on 12/1/2017.
 */
public class Mensaje implements Serializable {
    private int id;
    private String usuarioEnvia;
    private String usuarioDestino;
    private String mensaje;
    private String date="";
    private String nombreEnvia;

    public Mensaje() {
    }

    public Mensaje(int id, String usuarioEnvia, String usuarioDestino, String mensaje) {
        this.id = id;
        this.usuarioEnvia = usuarioEnvia;
        this.usuarioDestino = usuarioDestino;
        this.mensaje = mensaje;
    }

    public Mensaje(int id, String usuarioEnvia, String usuarioDestino, String mensaje, String date, String nombreEnvia) {
        this.id = id;
        this.usuarioEnvia = usuarioEnvia;
        this.usuarioDestino = usuarioDestino;
        this.mensaje = mensaje;
        this.date = date;
        this.nombreEnvia = nombreEnvia;
    }

    public String getNombreEnvia() {
        return nombreEnvia;
    }

    public void setNombreEnvia(String nombreEnvia) {
        this.nombreEnvia = nombreEnvia;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioEnvia() {
        return usuarioEnvia;
    }

    public void setUsuarioEnvia(String usuarioEnvia) {
        this.usuarioEnvia = usuarioEnvia;
    }

    public String getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(String usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
