package com.jefrienalvizures.tonechord.bean;

import java.io.Serializable;
/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class Chord implements Serializable {
    private int id;
    private String autor;
    private String titulo;
    private boolean publico;
    private boolean anonimo;
    private String lineas;
    private int idUsuario;

    public String toJson(){
        String json = "";
        json = "{\"autor\":\""+this.autor+"\"," +
                "\"titulo\":\""+this.titulo+"\"," +
                "\"publico\":\""+this.publico+"\"," +
                "\"anonimo\":\""+this.anonimo+"\"," +
                "\"idUsuario\":\""+this.idUsuario+"\"" +
                "}";

        return json;
    }

    public Chord() {
    }

    public Chord(int id, String autor, String titulo, boolean publico, boolean anonimo, String lineas, int idUsuario) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.publico = publico;
        this.anonimo = anonimo;
        this.lineas = lineas;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }

    public String getLineas() {
        return lineas;
    }

    public void setLineas(String lineas) {
        this.lineas = lineas;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
