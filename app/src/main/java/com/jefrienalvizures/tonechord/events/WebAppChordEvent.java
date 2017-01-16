package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien on 23/12/2016.
 */
public class WebAppChordEvent {
    private boolean ejecutar;
    private String chord;

    public WebAppChordEvent() {
    }

    public String getChord() {
        return chord;
    }

    public void setChord(String chord) {
        this.chord = chord;
    }

    public boolean isEjecutar() {
        return ejecutar;
    }

    public void setEjecutar(boolean ejecutar) {
        this.ejecutar = ejecutar;
    }
}
