package com.jefrienalvizures.tonechord;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewBurbujaMensaje;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewMensaje;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.events.MensajesNuevosEvent;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.net.Conexion;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnviarMensajeActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chatEditText)
    EditText editorMensajeResponder;
    @Bind(R.id.chatBtnEnviar)
    Button btnEnviarMensaje;
    Usuario usuarioActual=null;
    /** DECLARANDO VARIABLES **/
    private RecyclerView recycler;
    private AdapterCardViewBurbujaMensaje adapter;
    private LinearLayoutManager lManager;
    private ArrayList<Mensaje> items = new ArrayList<>();
    private int idConversacion;
    private Gson gson = new Gson();
    private String emailAmigo=null;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    private InfoConversacion infoConversacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        idConversacion = bundle.getInt("idConversacion");
        loadUser();
        setupToolbar();
        new GetDataNombre().execute();
        new CargarMensajes().execute();
        actualizador();
        setLista();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,MensajesActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);

        }

    }

    private void cargarChords(final ArrayList<Mensaje> tmp){
        if(items.isEmpty()) {
            for (Mensaje c : tmp) {
                items.add(c);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLista();
                }
            });
            Log.e("VACIO","CARGO");
        } else {
            if(items.size() == tmp.size()){

            } else {
                Log.e("DIFERENTE","AGREGO");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.animateTo(tmp);
                        items = tmp;
                    }
                });

            }
        }

       /* if(items.size() != tmp.size()) {
            Log.e("ES DIFERENTE","CARGO");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLista();
                }
            });
        }*/

    }

    private void setLista(){
            recycler = (RecyclerView) findViewById(R.id.recicladorMensajesChat);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(this);
            lManager.setStackFromEnd(true);
            recycler.setLayoutManager(lManager);

            adapter = new AdapterCardViewBurbujaMensaje(this,items);
            recycler.setAdapter(adapter);

    }

    @OnClick(R.id.chatBtnEnviar)
    public void enviarMensaje(){
        String textoEdit = editorMensajeResponder.getText().toString();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editorMensajeResponder.getWindowToken(),0);
        editorMensajeResponder.setText("");

        //Log.e("Usuario Destino",emailAmigo);
        Mensaje mensajeNuevo = new Mensaje();
        mensajeNuevo.setUsuarioEnvia(usuarioActual.getEmail());
        mensajeNuevo.setId(idConversacion);
        mensajeNuevo.setMensaje(textoEdit);
        Mensaje[] mensajes = {mensajeNuevo};
        EnviarMensajeTask enviarMensajeTask = new EnviarMensajeTask();
        enviarMensajeTask.execute(mensajes);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MensajesActivity.class));
        this.finish();
    }

    private void actualizador(){

        final Handler handler1 = new Handler();
        Runnable run = null;
        final Runnable finalRun = run;
        handler1.postDelayed(run = new Runnable() {
            public void run() {
                new CargarMensajes().execute();
                handler1.postDelayed(this, 3000); //now is every 2 minutes
            }
        }, 3000);
    }

    class GetDataNombre extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String datos = Conexion.getInstancia().getDatosConversacion(idConversacion);
                infoConversacion = gson.fromJson(datos, InfoConversacion.class);
                if (infoConversacion.getEmail1().equals(usuarioActual.getEmail())) {
                    Log.e("JAJA", infoConversacion.getEmail2());
                    emailAmigo = infoConversacion.getEmail2();
                } else {
                    Log.e("JAJA", infoConversacion.getEmail1());
                    emailAmigo = infoConversacion.getEmail1();
                }

                String nombre = Conexion.getInstancia().getNombreUsuarioByEmail(emailAmigo);
                final Response r = gson.fromJson(nombre, Response.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().setTitle(r.getMessage());
                    }
                });
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    class CargarMensajes extends AsyncTask<Void,Void,Void>{

        String respuesta;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                respuesta = Conexion.getInstancia().getMensajesConversacion(idConversacion);
                List<Mensaje> tmp = gson.fromJson(respuesta, new TypeToken<List<Mensaje>>() {
                }.getType());

                cargarChords(new ArrayList<>(tmp));
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    class EnviarMensajeTask extends AsyncTask<Mensaje,Void,Void> {

        String respuesta;
        Response response;

        @Override
        protected void onPreExecute() {
            //dialogos.showProgressDialog("Enviando chord");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Mensaje... params) {
            String datos = Conexion.getInstancia().getDatosConversacion(idConversacion);
            infoConversacion = gson.fromJson(datos,InfoConversacion.class);
            Log.e("CARGO DATOS",datos);

            if(infoConversacion.getEmail1().equals(usuarioActual.getEmail())){
                Log.e("JAJA",infoConversacion.getEmail2());
                emailAmigo = infoConversacion.getEmail2();
                params[0].setUsuarioDestino(infoConversacion.getEmail2());
            } else {
                Log.e("JAJA",infoConversacion.getEmail1());
                emailAmigo = infoConversacion.getEmail1();
                params[0].setUsuarioDestino(infoConversacion.getEmail1());
            }
            respuesta = Conexion.getInstancia().enviarMensaje(params[0]);
            try {
                if (respuesta != null || respuesta.isEmpty()) {
                    response = gson.fromJson(respuesta, Response.class);
                    //BaseDeDatos.guardarMensajeIf(EnviarMensajeActivity.this,params[0]);
                }
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                Log.e("Mensajes","Mensaje enviado");
                //actualizar();
            }
            new CargarMensajes().execute();
            //dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

    class InfoConversacion {
        private String email1;
        private String email2;

        public String getEmail1() {
            return email1;
        }

        public void setEmail1(String email1) {
            this.email1 = email1;
        }

        public String getEmail2() {
            return email2;
        }

        public void setEmail2(String email2) {
            this.email2 = email2;
        }
    }
}
