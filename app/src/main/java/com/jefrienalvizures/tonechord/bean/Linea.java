package com.jefrienalvizures.tonechord.bean;

import java.io.Serializable;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class Linea implements Serializable {
    private char tipo;
    private String linea;

    public Linea() {
    }

    public Linea(char tipo, String linea) {
        this.tipo = tipo;
        this.linea = linea;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }
}
