package com.jefrienalvizures.tonechord.lib;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.SQLiteToneChord;
import com.jefrienalvizures.tonechord.net.Conexion;
import com.jefrienalvizures.tonechord.servicios.MensajeService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class BaseDeDatos {

    private static String BASE_URL = Conexion.BASE_URL_IMG;

    public static void guardarChordIf(Activity a,Chord _chord){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();

        ArrayList<String> l = cargarChords(a);

        if(l.isEmpty()){
            ContentValues chord = new ContentValues();
            chord.put("archivo",_chord.getId()+"");
            bd.insert("chords", null, chord);
            Objeto.write(a,_chord,_chord.getId()+"");
        } else {
            if(!l.contains(_chord.getId())){
                ContentValues chord = new ContentValues();
                chord.put("archivo", _chord.getId()+"");
                bd.insert("chords", null, chord);
                // Guardo el objecto local
                Objeto.write(a,_chord,_chord.getId()+"");
            }
        }

        bd.close();
    }

    public static void guardarChordFav(Activity a,Chord _chord){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ArrayList<String> l = cargarChords(a);
        ArrayList<String> lf = cargarChordsFav(a);
        if(!l.contains(String.valueOf(_chord.getId()))){
            //Log.e("No Existe en chords",_chord.getId()+"");
            if(!lf.contains(String.valueOf(_chord.getId()))){
                //Log.e("No Existe en chordsFav","Agrego:"+_chord.getId()+"");
                ContentValues chord = new ContentValues();
                chord.put("archivo",_chord.getId()+"");
                bd.insert("chordsFavoritos", null, chord);
                Objeto.write(a,_chord,_chord.getId()+"");
            }
        } else {
            if(!lf.contains(String.valueOf(_chord.getId()))){
                //Log.e("Esiste 1 no exite 2","Agrego:"+_chord.getId()+"");
                ContentValues chord = new ContentValues();
                chord.put("archivo",_chord.getId()+"");
                bd.insert("chordsFavoritos", null, chord);
            }
        }
        bd.close();
    }

    public static ArrayList<String> cargarChords(Activity a){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ArrayList<String> archivos = new ArrayList<>();
        Cursor fila = bd.rawQuery(
                "select archivo from chords"
        ,null);
        if(fila.moveToFirst()){
            do{
                archivos.add(fila.getString(0));
               // Log.e("ARCHIVO",fila.getString(0));
            } while (fila.moveToNext());
        }
        bd.close();
        return archivos;
    }

    public static ArrayList<String> cargarChordsFav(Activity a){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();

        ArrayList<String> archivos = new ArrayList<>();

        Cursor fila = bd.rawQuery(
                "select archivo from chordsFavoritos"
                ,null);

        if(fila.moveToFirst()){
            do{
                archivos.add(fila.getString(0));
                // Log.e("ARCHIVO",fila.getString(0));
            } while (fila.moveToNext());
        }
        bd.close();
        return archivos;
    }

    public static void borrarChord(Activity a,String key){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        bd.delete("chords","archivo="+key,null);
        bd.delete("chordsFavoritos","archivo="+key,null);
        bd.close();
    }

    public static boolean verificarSesion(Activity a) {
        boolean res = false;
        SQLiteToneChord tonechord = new SQLiteToneChord(a, "ToneChord", null, 1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select name from usuario"
                , null);

        if(fila.getCount() == 0){
            res = false;
        } else {
            res = true;
        }
        bd.close();
        return res;
    }

    public static void iniciarSesion(Activity a,Usuario usuario){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ContentValues _usuario = new ContentValues();
        _usuario.put("id",usuario.getId());
        _usuario.put("name",usuario.getName());
        _usuario.put("email",usuario.getEmail());
        _usuario.put("imagen",usuario.getImagen());
        bd.insert("usuario",null,_usuario);
        bd.close();
    }

    public static void setImage(Activity a,String imagen){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ContentValues _imagen = new ContentValues();
        _imagen.put("imagen",imagen);
        bd.update("usuario",_imagen,null,null);
        bd.close();
        Bitmap resultado = null;

        try {
            URL urlImagen = new URL(BASE_URL + imagen);
            resultado = BitmapFactory.decodeStream(urlImagen.openConnection().getInputStream());
            if(resultado!=null){
                Objeto.writeImage(a,imagen,resultado);
            }
        } catch (MalformedURLException mue){

        } catch (IOException e){

        }
    }

    public static void modificarPerfil(Activity a,Usuario usuario){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ContentValues _usuario = new ContentValues();
        _usuario.put("name",usuario.getName());
        _usuario.put("email",usuario.getEmail());
        bd.update("usuario",_usuario,null,null);
        bd.close();
    }


    public static void setFavoritoFalse(Activity a,int idChord){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ArrayList<String> lista = cargarChords(a);

        for(String s: cargarChordsFav(a)){
            if(s.equals(idChord+"")) {
                if(!lista.contains(s)) {
                    Objeto.delete(a, s);
                }
            }
        }
        bd.delete("chordsFavoritos","archivo="+idChord,null);
        bd.close();
    }

    public static void cerrarSesion(Activity a){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        for(String s: cargarChords(a)){
            Objeto.delete(a,s);
        }
        bd.delete("usuario",null,null);
        bd.delete("chords",null,null);
        bd.delete("mensajes",null,null);
        bd.delete("chordsFavoritos",null,null);
        bd.close();
        a.stopService(new Intent(a,MensajeService.class));
    }

    public static Usuario getUsuario(Activity a){
        Usuario usuarioRes=new Usuario();

        SQLiteToneChord tonechord = new SQLiteToneChord(a, "ToneChord", null, 1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select id,name,email,imagen from usuario"
                , null);
        if(fila.moveToFirst()){
            do{
                //Log.e("DESDE LA BD",fila.getInt(0)+"");
                usuarioRes.setId(fila.getInt(0));
                usuarioRes.setName(fila.getString(1));
                usuarioRes.setEmail(fila.getString(2));
                usuarioRes.setImagen(fila.getString(3));
            } while (fila.moveToNext());
        }
        bd.close();
        return usuarioRes;
    }

    /** MENSAJES **/
    public static void guardarMensajeIf(Activity a,Mensaje _mensaje){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();

        ArrayList<String> l = cargarMensajes(a);

        if(l.isEmpty()){
            ContentValues chord = new ContentValues();
            chord.put("archivo",_mensaje.getId()+"");
            bd.insert("mensajes", null, chord);
            Objeto.writeMensaje(a,_mensaje,_mensaje.getId()+"");
        } else {
            if(!l.contains(_mensaje.getDate())){
                ContentValues chord = new ContentValues();
                chord.put("archivo", _mensaje.getId()+"");
                bd.insert("mensajes", null, chord);
                // Guardo el objecto local
                Objeto.writeMensaje(a,_mensaje,_mensaje.getId()+"");
            }
        }

        bd.close();
    }

    public static ArrayList<String> cargarMensajes(Activity a){
        SQLiteToneChord tonechord = new SQLiteToneChord(a,"ToneChord",null,1);
        SQLiteDatabase bd = tonechord.getWritableDatabase();
        ArrayList<String> archivos = new ArrayList<>();
        Cursor fila = bd.rawQuery(
                "select archivo from mensajes"
                ,null);
        if(fila.moveToFirst()){
            do{
                archivos.add(fila.getString(0));
                // Log.e("ARCHIVO",fila.getString(0));
            } while (fila.moveToNext());
        }
        bd.close();
        return archivos;
    }
}
