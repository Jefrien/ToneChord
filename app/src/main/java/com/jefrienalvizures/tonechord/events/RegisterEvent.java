package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien on 19/12/2016.
 */
public class RegisterEvent {
    private String email;

    public RegisterEvent() {
    }

    public RegisterEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
