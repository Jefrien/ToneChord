package com.jefrienalvizures.tonechord.lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.BuildConfig;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Usuario;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class Dialogos {

    private ProgressDialog mProgressDialog;
    Context context;

    public Dialogos(){

    }

    public Dialogos(Context c){
        this.context = c;
    }

    public static void ok(Activity a, final String titulo, final String texto){
            final AlertDialog.Builder builder = new AlertDialog.Builder(a);
            LayoutInflater inflater = a.getLayoutInflater();
            View v = inflater.inflate(R.layout.ayuda_dialog, null);
            // builder.setView(v);
            final AlertDialog ad = builder.create();
            ad.setView(v);
            Button btn0 = (Button) v.findViewById(R.id.ayuda_nueva_letra_hecho_button);
            TextView t = (TextView) v.findViewById(R.id.tituloInfo);
            TextView t1 = (TextView) v.findViewById(R.id.ayuda_nueva_letra_texto);
            t.setText(titulo);
            t1.setText(texto);

            btn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad.dismiss();
                }
            });
            ad.show();
    }

    public void aboutDialog(Activity a) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater inflater = a.getLayoutInflater();
        View v = inflater.inflate(R.layout.about_dialog, null);
        final AlertDialog ad = builder.create();
        ad.setView(v);

        TextView version = (TextView) v.findViewById(R.id.versionAppAbout);
        version.setText(String.format(a.getResources().getString(R.string.version_app), BuildConfig.VERSION_NAME));

        Button btn = (Button) v.findViewById(R.id.hechoAboutDialog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        ad.show();
    }

    /** Metodo para configurar y mostrar un ProgressDialog **/
    public void showProgressDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(text);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    /** Metodo para ocultar un progressDialog **/
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void changeTextProgressDialog(String text){
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(text);
        }
    }

}
