package com.jefrienalvizures.tonechord.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Jefrien on 16/1/2017.
 */
public class SolicitudDeAmistad implements Serializable{
    private int idAmistad;
    private String usuario1;
    private String usuario2;
    private int estado;
    private String usuario1name;
    private String usuario2name;
    private Bitmap imagen;

    public SolicitudDeAmistad() {
    }

    public SolicitudDeAmistad(int idAmistad, String usuario1, String usuario2, int estado, String usuario1name, String usuario2name, Bitmap imagen) {
        this.idAmistad = idAmistad;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.estado = estado;
        this.usuario1name = usuario1name;
        this.usuario2name = usuario2name;
        this.imagen = imagen;
    }

    public String toJson(){
        String respuesta = "{" +
                "\"idAmistad\":\""+this.idAmistad+"\"," +
                "\"usuario1\":\""+this.usuario1+"\"," +
                "\"usuario2\":\""+this.usuario2+"\"," +
                "\"estado\":\""+this.estado+"\"}";
        return respuesta;
    }


    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
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

    public String getUsuario1() {
        return usuario1;
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

    public String getUsuario1name() {
        return usuario1name;
    }

    public void setUsuario1name(String usuario1name) {
        this.usuario1name = usuario1name;
    }

    public String getUsuario2name() {
        return usuario2name;
    }

    public void setUsuario2name(String usuario2name) {
        this.usuario2name = usuario2name;
    }
}
