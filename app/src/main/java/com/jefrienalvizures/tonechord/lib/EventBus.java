package com.jefrienalvizures.tonechord.lib;

/**
 * Created by Jefrien on 19/12/2016.
 */
public interface EventBus {
    void register(Object subscriber);
    void untegister(Object subscriber);
    void post(Object event);
}
