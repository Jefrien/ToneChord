package com.jefrienalvizures.tonechord.lib;

import android.app.Activity;

import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Linea;
import com.jefrienalvizures.tonechord.bean.Usuario;

import java.util.ArrayList;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class Comunicator {
    private static Usuario usuario;
    private static String letra;
    private static ArrayList<Linea> lineas;
    private static ArrayList<Chord> chords;
    private static ArrayList<Chord> chordsPublicos;
    private static String emailUser;
    private static Activity activity;
    private static boolean mensajesNuevos = false;

    public static boolean isMensajesNuevos() {
        return mensajesNuevos;
    }

    public static void setMensajesNuevos(boolean mensajesNuevos) {
        Comunicator.mensajesNuevos = mensajesNuevos;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Comunicator.activity = activity;
    }

    public static String getEmailUser() {
        return emailUser;
    }

    public static void setEmailUser(String emailUser) {
        Comunicator.emailUser = emailUser;
    }

    public static ArrayList<Chord> getChordsPublicos() {
        return chordsPublicos;
    }

    public static void setChordsPublicos(ArrayList<Chord> chordsPublicos) {
        Comunicator.chordsPublicos = chordsPublicos;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Comunicator.usuario = usuario;
    }

    public static ArrayList<Chord> getChords() {
        return chords;
    }

    public static void setChords(ArrayList<Chord> chords) {
        Comunicator.chords = chords;
    }

    public static String getLetra() {
        return letra;
    }

    public static void setLetra(String letra) {
        Comunicator.letra = letra;
    }

    public static ArrayList<Linea> getLineas() {
        return lineas;
    }

    public static void setLineas(ArrayList<Linea> lineas) {
        Comunicator.lineas = lineas;
    }
}
