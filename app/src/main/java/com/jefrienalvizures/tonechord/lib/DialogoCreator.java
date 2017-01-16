package com.jefrienalvizures.tonechord.lib;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jefrienalvizures.tonechord.R;

/**
 * Created by Jefrien on 9/1/2017.
 */
public class DialogoCreator extends AlertDialog.Builder {
    AlertDialog ad = null;
    View vista;

    public DialogoCreator(Context context, Activity activity, int layout) {
        super(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(layout, null);
        ad = this.create();
        ad.setView(v);
        this.vista = v;
    }

    @Override
    public AlertDialog show() {
        ad.show();
        return null;
    }

    public void mostrar(){
        ad.show();
    }


    public View getView(){
        return this.vista;
    }

    public void dismis(){
        ad.dismiss();
    }
}
