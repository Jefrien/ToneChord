package com.jefrienalvizures.tonechord.bean;

/**
 * Created by Jefrien on 22/1/2017.
 */
public class Conversacion {
    private int idConversacion;
    private String usuario1;
    private String usuario2;
    private String mensaje;

    public Conversacion() {
    }

    public Conversacion(int idConversacion, String usuario1, String usuario2) {
        this.idConversacion = idConversacion;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public Conversacion(int idConversacion, String usuario1, String usuario2, String mensaje) {
        this.idConversacion = idConversacion;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(int idConversacion) {
        this.idConversacion = idConversacion;
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
}
