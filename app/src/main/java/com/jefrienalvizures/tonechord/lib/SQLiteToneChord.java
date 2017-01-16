package com.jefrienalvizures.tonechord.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class SQLiteToneChord extends SQLiteOpenHelper {


    public SQLiteToneChord(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chords(archivo text)");
        db.execSQL("create table usuario(id integer,name text,email text,imagen text)");
        db.execSQL("create table chordsFavoritos(archivo text)");
        db.execSQL("create table mensajes(archivo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
