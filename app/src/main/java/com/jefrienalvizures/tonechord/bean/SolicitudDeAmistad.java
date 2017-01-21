package com.jefrienalvizures.tonechord.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Jefrien on 16/1/2017.
 */
public class SolicitudDeAmistad implements Serializable{
    private int idAmistad;
    private int idUsuario1;
    private int idUsuario2;
    private int estado;
    private String usuario1;
    private String usuario2;
    private Bitmap imagen;

    public SolicitudDeAmistad() {
    }

    public SolicitudDeAmistad(int idAmistad, int idUsuario1, int idUsuario2, int estado) {
        this.idAmistad = idAmistad;
        this.idUsuario1 = idUsuario1;
        this.idUsuario2 = idUsuario2;
        this.estado = estado;
    }

    public SolicitudDeAmistad(int idAmistad, int idUsuario1, int idUsuario2, int estado, String usuario1, String usuario2, Bitmap imagen) {
        this.idAmistad = idAmistad;
        this.idUsuario1 = idUsuario1;
        this.idUsuario2 = idUsuario2;
        this.estado = estado;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.imagen = imagen;
    }

    public String toJson(){
        String respuesta = "{" +
                "\"idAmistad\":\""+this.idAmistad+"\"," +
                "\"idUsuario1\":\""+this.idUsuario1+"\"," +
                "\"idUsuario2\":\""+this.idUsuario2+"\"," +
                "\"estado\":\""+this.estado+"\"}";
        return respuesta;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdAmistad() {
        return idAmistad;
    }

    public void setIdAmistad(int idAmistad) {
        this.idAmistad = idAmistad;
    }

    public int getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(int idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public int getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(int idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }
}
