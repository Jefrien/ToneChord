package com.jefrienalvizures.tonechord.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jefrien on 5/1/2017.
 */
public class ImageHelper {

    private String imagenPath = null;
    private Bitmap bitmap = null;
    private File archivoImg = null;
    private ByteArrayOutputStream bytearrayoutputstream;
    private byte[] BYTE;

    public ImageHelper(String path){
        this.imagenPath = path;
    }

    public Bitmap procesoImagen(){
        bitmap = BitmapFactory.decodeFile(imagenPath);
        archivoImg = new File(imagenPath);

        comprimoImagen();
        fixRotacion();
        return bitmap;
    }

    private void fixRotacion(){
        // Arreglo la rotacion
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(archivoImg.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        }catch (IOException e){}
    }

    private void comprimoImagen(){
        if(bitmap.getWidth()>1024) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);
        }
        bytearrayoutputstream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40,bytearrayoutputstream);
        BYTE = bytearrayoutputstream.toByteArray();
        bitmap =  BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

    }
}
