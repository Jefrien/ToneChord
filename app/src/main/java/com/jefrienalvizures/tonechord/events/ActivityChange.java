package com.jefrienalvizures.tonechord.events;

/**
 * Created by Jefrien Alvizures on 2/10/16.
 */
public class ActivityChange {
    private int id;
    private String param;

    public ActivityChange() {
    }

    public ActivityChange(int id, String param) {
        this.id = id;
        this.param = param;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
