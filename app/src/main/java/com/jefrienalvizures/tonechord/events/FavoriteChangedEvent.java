package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien on 27/12/2016.
 */
public class FavoriteChangedEvent {
    private boolean estado;

    public FavoriteChangedEvent(boolean estado) {
        this.estado = estado;
    }

    public FavoriteChangedEvent() {
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
