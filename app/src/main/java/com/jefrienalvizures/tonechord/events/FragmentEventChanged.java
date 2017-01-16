package com.jefrienalvizures.tonechord.events;

/**
 * Created by jefrien on 4/09/16.
 */
public class FragmentEventChanged {
    private Integer id;

    public FragmentEventChanged() {
    }

    public FragmentEventChanged(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
