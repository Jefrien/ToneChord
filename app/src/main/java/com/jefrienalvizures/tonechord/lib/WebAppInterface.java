package com.jefrienalvizures.tonechord.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.jefrienalvizures.tonechord.DetalleChordActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.events.WebAppChordEvent;

/**
 * Created by Jefrien on 23/12/2016.
 */
public class WebAppInterface {

    Context context;
    Chord chord;
    String chordStr = "";

    public WebAppInterface(Context context){
        this.context = context;
    }

    public WebAppInterface (Context context, Chord chord){
        this.context = context;
        this.chord = chord;
    }

    @JavascriptInterface
    public void setChord (String txt){
        this.chordStr = txt.toLowerCase();
        Log.e("LLamando a ","LANZAR CON ACORDE "+chordStr);
        postEvent(true,chordStr);
    }

    private void postEvent(boolean r,String c){
        WebAppChordEvent wace = new WebAppChordEvent();
        wace.setEjecutar(r);
        wace.setChord(c);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(wace);
    }

    @JavascriptInterface
    public void infoChord(){
        Intent i = new Intent(new Intent(context, DetalleChordActivity.class));
        i.putExtra("id",chord.getId());
        i.putExtra("chord",chord.toJson());
        i.putExtra("lineas",chord.getLineas());
        Log.e("Desde la interfaz",chord.toJson());
        context.startActivity(i);
        ((Activity)context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
