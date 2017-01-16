package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien Alvizures on 07/01/2017.
 */
public class ShowHideToolbarEvent {
    int estado = 1;

    public ShowHideToolbarEvent(int estado) {
        this.estado = estado;
    }

    public ShowHideToolbarEvent() {
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
