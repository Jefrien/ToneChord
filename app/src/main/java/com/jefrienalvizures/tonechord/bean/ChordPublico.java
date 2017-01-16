package com.jefrienalvizures.tonechord.bean;


import java.io.Serializable;


/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class ChordPublico implements Serializable {
    private int id;
    private String autor;
    private String titulo;
    private boolean publico;
    private boolean anonimo;
    private String lineas;
    private Integer likes;
    private Integer desLikes;
    private boolean favorito;
    private boolean likeForMy;
    private boolean desLikeForMy;
    private int by;

    public String toJson(){
        String json = "";
        json = "{" +
                "\"autor\":\""+this.autor+"\"" +
                "\"titulo\":\""+this.titulo+"\"" +
                "\"publico\":\""+this.publico+"\"" +
                "\"anonimo\":\""+this.anonimo+"\"" +
                "\"lineas\":\""+this.lineas+"\"" +
                "\"likes\":\""+this.likes+"\"" +
                "\"desLkes\":\""+this.desLikes+"\"" +
                "\"favorito\":\""+this.favorito+"\"" +
                "\"likeForMy\":\""+this.likeForMy+"\"" +
                "\"desLikeForMy\":\""+this.desLikeForMy+"\"" +
                "\"by\":\""+this.by+"\"" +
                "}";

        return json;
    }

    public ChordPublico() {
    }

    public ChordPublico(int id, String autor, String titulo, boolean publico, boolean anonimo, String lineas, Integer likes, Integer desLikes, boolean favorito, boolean likeForMy, boolean desLikeForMy, int by) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.publico = publico;
        this.anonimo = anonimo;
        this.lineas = lineas;
        this.likes = likes;
        this.desLikes = desLikes;
        this.favorito = favorito;
        this.likeForMy = likeForMy;
        this.desLikeForMy = desLikeForMy;
        this.by = by;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDesLikes() {
        return desLikes;
    }

    public void setDesLikes(Integer desLikes) {
        this.desLikes = desLikes;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isLikeForMy() {
        return likeForMy;
    }

    public void setLikeForMy(boolean likeForMy) {
        this.likeForMy = likeForMy;
    }

    public boolean isDesLikeForMy() {
        return desLikeForMy;
    }

    public void setDesLikeForMy(boolean desLikeForMy) {
        this.desLikeForMy = desLikeForMy;
    }

    public int getBy() {
        return by;
    }

    public void setBy(int by) {
        this.by = by;
    }
}
