package com.jefrienalvizures.tonechord.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.jefrienalvizures.tonechord.LoginActivity;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class Objeto {
    public static void write(Context context,Object obj, String nombre){
        FileOutputStream fos = null;
        ObjectOutputStream salida = null;

        //Se crea el fichero
        try {
            File file = new File(context.getExternalFilesDir("chords"), nombre+".tc");
            fos = new FileOutputStream(file);
            salida = new ObjectOutputStream(fos);
            salida.writeObject(obj);
           // Toast.makeText(context,"Objeto "+nombre,Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e("WRITE OBJECT ERROR",e.getMessage());
        } catch (IOException e) {
            Log.e("WRITE OBJECT ERROR",e.getMessage());
        }finally {
            try {
                if(fos!=null) fos.close();
                if(salida!=null) salida.close();
            } catch (IOException e) {
                Log.e("WRITE OBJECT ERROR",e.getMessage());
            }
        }

    }

    public static Chord read(Context context,String nombre){
        Chord chord = null;
        FileInputStream fis = null;
        ObjectInputStream entrada = null;
        try {
            File file = new File(context.getExternalFilesDir("chords"),nombre+".tc");
            fis = new FileInputStream(file);
            entrada = new ObjectInputStream(fis);
            chord = (Chord) entrada.readObject();
        } catch (FileNotFoundException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch (IOException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch (ClassCastException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch(ClassNotFoundException e) {
            Log.e("READ OBJECT ERROR",e.getMessage());
        }finally {
            try {
                if(fis!=null) fis.close();
                if(entrada!=null) entrada.close();
            } catch (IOException e){
                Log.e("READ OBJECT ERROR",e.getMessage());
            }
        }
        return chord;
    }

    public static void edit(Context context,String nombre, Chord _chord){
        FileOutputStream fos = null;
        ObjectOutputStream salida = null;

        //Se crea el fichero
        try {
            File file = new File(context.getExternalFilesDir("chords"), nombre+".tc");
            file.delete();
            fos = new FileOutputStream(file);
            salida = new ObjectOutputStream(fos);
            salida.writeObject(_chord);
            Log.e("Objeto editado","Nombre: "+nombre+".tc | Titulo: "+_chord.getTitulo());
        } catch (FileNotFoundException e) {
            Log.e("WRITE OBJECT ERROR",e.getMessage());
        } catch (IOException e) {
            Log.e("WRITE OBJECT ERROR",e.getMessage());
        }finally {
            try {
                if(fos!=null) fos.close();
                if(salida!=null) salida.close();
            } catch (IOException e) {
                Log.e("WRITE OBJECT ERROR",e.getMessage());
            }
        }
    }

    public static void delete(Context context,String nombre){
        try {
            File file = new File(context.getExternalFilesDir("chords"), nombre+".tc");
            file.delete();
            Log.e("DELETE","OBJETO ELIMINADO CON EXITO");
        }catch (ClassCastException e){
            Log.e("DELETE OBJECT ERROR",e.getMessage());
        }
    }

    public static void writeImage(Context context,String nombre,Bitmap _bitmap){
        //create a file to write bitmap data
        try {
            File f = new File(context.getExternalFilesDir("img"), nombre);
            f.createNewFile();

            //Convert bitmap to byte array
            Bitmap bitmap = _bitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException ioe){
            Log.e("Error","Error creando la imagen");
        }
    }

    public static Bitmap readImage(Context context,String nombre){
        Bitmap resultado = null;
        File f = new File(context.getExternalFilesDir("img"), nombre);
        if(f.exists()){
            resultado = BitmapFactory.decodeFile(f.getAbsolutePath());
        }

        return resultado;
    }

    public static void writeMensaje(Context context,Object obj,String nombre){
            FileOutputStream fos = null;
            ObjectOutputStream salida = null;
            //Se crea el fichero
            try {
                File file = new File(context.getExternalFilesDir("inbox"), nombre+".tc");
                fos = new FileOutputStream(file);
                salida = new ObjectOutputStream(fos);
                salida.writeObject(obj);
                // Toast.makeText(context,"Objeto "+nombre,Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.e("WRITE OBJECT ERROR",e.getMessage());
            } catch (IOException e) {
                Log.e("WRITE OBJECT ERROR",e.getMessage());
            }finally {
                try {
                    if(fos!=null) fos.close();
                    if(salida!=null) salida.close();
                } catch (IOException e) {
                    Log.e("WRITE OBJECT ERROR",e.getMessage());
                }
            }
    }

    public static Mensaje readMensaje(Context context, String nombre){
        Mensaje obj = null;
        FileInputStream fis = null;
        ObjectInputStream entrada = null;
        try {
            File file = new File(context.getExternalFilesDir("inbox"),nombre+".tc");
            fis = new FileInputStream(file);
            entrada = new ObjectInputStream(fis);
            obj = (Mensaje) entrada.readObject();
        } catch (FileNotFoundException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch (IOException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch (ClassCastException e){
            Log.e("READ OBJECT ERROR",e.getMessage());
        } catch(ClassNotFoundException e) {
            Log.e("READ OBJECT ERROR",e.getMessage());
        }finally {
            try {
                if(fis!=null) fis.close();
                if(entrada!=null) entrada.close();
            } catch (IOException e){
                Log.e("READ OBJECT ERROR",e.getMessage());
            }
        }
        return obj;
    }
}
