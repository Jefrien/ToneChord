package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien Alvizures on 2/10/16.
 */
public class ListaChordsChanged {
    private boolean cambio;

    public ListaChordsChanged() {
    }

    public ListaChordsChanged(boolean cambio) {
        this.cambio = cambio;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }
}
