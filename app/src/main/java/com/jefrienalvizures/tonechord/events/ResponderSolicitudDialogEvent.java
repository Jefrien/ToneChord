package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien on 16/1/2017.
 */
public class ResponderSolicitudDialogEvent {
    private int position;

    public ResponderSolicitudDialogEvent() {
    }

    public ResponderSolicitudDialogEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
