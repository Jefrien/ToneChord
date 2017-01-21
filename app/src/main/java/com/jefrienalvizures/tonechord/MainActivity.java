package com.jefrienalvizures.tonechord;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.jefrienalvizures.tonechord.interfaces.InterfaceMensajesNuevos;
import com.jefrienalvizures.tonechord.interfaces.InterfaceServiceMensajes;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.servicios.MensajeService;

public class MainActivity extends AppCompatActivity {

    /**
     * By Jefrien Armando Alvizures Martínez
     *
     * Actividad Principal
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * By Jefrien Armando Alvizures Martínez
         * Configuro el webview para mostrar una animacion de carga
         * @assets index.html, loading.gif.
         */
        WebView web = (WebView) findViewById(R.id.webView);
        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
        web.loadUrl("file:///android_asset/index.html");
        /**
         * By Jefrien Armando Alvizures Martínez
         * Configuro un tiempo en milisegundos de espera
         * antes de lanzar la actividad de Login.
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                init();
            }
        }, 3000);


    }

    private void init() {
        /**
         * By Jefrien Armando Alvizures Martínez
         * Verifico si ya existe un usuario con sesion activa.
         */
        if(BaseDeDatos.verificarSesion(this)){
            startActivity(new Intent(this,HomeActivity.class));
            this.finish();
        } else {
            startActivity(new Intent(this,LoginActivity.class));
            this.finish();
        }
    }
}
