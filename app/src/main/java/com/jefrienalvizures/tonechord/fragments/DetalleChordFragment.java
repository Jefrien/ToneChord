package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jefrienalvizures.tonechord.BuildConfig;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.UserProfileActivity;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.List;

import javax.security.auth.login.LoginException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien on 10/1/2017.
 */
public class DetalleChordFragment extends Fragment {

    @Bind(R.id.detalleChordTitulo)
    TextView _titulo;
    @Bind(R.id.detalleChordAutor)
    TextView _autor;
    @Bind(R.id.detalleChordUsuario)
    TextView _usuario;
    @Bind(R.id.btnDetalleEliminar)
    Button btnEliminar;
    @Bind(R.id.btnDetalleEditar)
    Button btnEditar;
    Dialogos dialogos;
    Usuario usuarioActual = null;
    Chord chordActual;
    Gson gson;
    Usuario usuarioChord;
    boolean respuestaVerificarInternet=true;

    public static DetalleChordFragment newInstance(String chord,int id){
        DetalleChordFragment dcf = new DetalleChordFragment();
        Bundle args = new Bundle();
        args.putString("chord",chord);
        args.putInt("id",id);
        dcf.setArguments(args);
        return dcf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle_chord, container, false);
        ButterKnife.bind(this,rootView);
        verificoInternet();
        gson = new Gson();
        dialogos = new Dialogos(getContext());
        loadUser();
        chordActual = gson.fromJson(getArguments().getString("chord"),Chord.class);
        chordActual.setId(getArguments().getInt("id"));
        loadChordDetalle();
        return rootView;
    }

    @OnClick(R.id.btnDetalleEditar)
    public void btnEditar(){
        if(Comunicator.isInternet()) {
            postEvent(2);
        } else {
            noInternetDialog();
        }
    }

    @OnClick(R.id.btnDetalleCompartir)
    public void btnCompartir(){
        compartirDialog();
    }

    @OnClick(R.id.btnDetalleEliminar)
    public void btnEliminar(){
        if(Comunicator.isInternet()) {
            eliminarDialog();
        } else {
            noInternetDialog();
        }
    }

    @OnClick(R.id.detalleChordUsuarioPerfil)
    public void verPerfil(){
        if(Comunicator.isInternet()) {
            Intent i = new Intent(getActivity(), UserProfileActivity.class);
            String[] datos = {
                    String.valueOf(usuarioChord.getId()),
                    usuarioChord.getName(),
                    usuarioChord.getEmail(),
                    usuarioChord.getImagen()
            };
            i.putExtra("usuario", datos);
            Log.e("Usuario enviado", datos.length + "");
            startActivity(i);
        } else {
            noInternetDialog();
        }
    }

    private boolean verificoInternet(){

        Thread thread = new Thread() {
            @Override
            public void run() {
                if(!Conexion.getInstancia().verificarConexion()){
                    respuestaVerificarInternet = false;
                } else {
                    respuestaVerificarInternet = true;
                }
            }
        };
        thread.start();
        return respuestaVerificarInternet;
    }

    private void noInternetDialog(){
        final DialogoCreator dc = new DialogoCreator(getContext(),getActivity(),
                R.layout.no_internet_dialog);
        View v = dc.getView();

        Button btnNoInternetHecho = (Button) v.findViewById(R.id.hechoNoInternerDialog);
        btnNoInternetHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
            }
        });

        dc.show();
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void loadChordDetalle(){
        dialogos.showProgressDialog("Cargando");
        _titulo.setText(chordActual.getTitulo().toString());
        _autor.setText(chordActual.getAutor().toString());
        // _usuario.setText(String.valueOf(chordActual.getIdUsuario()));
        if(chordActual.getIdUsuario()==usuarioActual.getId()){
            btnEliminar.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.VISIBLE);
        } else {
            btnEliminar.setVisibility(View.GONE);
            btnEditar.setVisibility(View.GONE);
        }
        CargarNombreUsuarioTask cnut = new CargarNombreUsuarioTask();
        cnut.execute();
        dialogos.hideProgressDialog();
    }

    private void compartirDialog(){
        final DialogoCreator dc = new DialogoCreator(getContext(),getActivity(),
                R.layout.compartir_dialog);
        View v = dc.getView();

        final EditText correoCompartir = (EditText) v.findViewById(R.id.txtCorreoCompartir);
        EditText codigoCompartir = (EditText) v.findViewById(R.id.txtCopyPegaCompartir);
        Button enviarCompartir = (Button) v.findViewById(R.id.btnEnviarCompartir);


        String codigo = "chord_"+chordActual.getIdUsuario()+"#"+chordActual.getId()+"#"+ BuildConfig.VERSION_NAME;
        codigoCompartir.setText(codigo);

        final String mensajeTxt = "Hola, te mando este chord para que lo veas :D Codigo: "+codigo;

        enviarCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificoInternet()){
                    if(correoCompartir.getText().length()==0 ||
                            correoCompartir.getText().toString().trim().isEmpty() ||
                            !correoCompartir.getText().toString().contains("@")){
                        correoCompartir.setError("Correo invalido");
                    } else {
                        Mensaje[] mensajes = {new Mensaje(
                                0,
                                usuarioActual.getEmail(),
                                correoCompartir.getText().toString(),
                                mensajeTxt
                        )};
                        EnviarChordCorreo enviarTask = new EnviarChordCorreo();
                        enviarTask.execute(mensajes);
                    }
                } else {
                    noInternetDialog();
                }
                dc.dismis();
            }
        });

        dc.show();
    }

    private void eliminarDialog(){
        final DialogoCreator dc = new DialogoCreator(getContext(),getActivity(),
                R.layout.eliminar_chord_dialog);
        View v = dc.getView();

        Button eliminarSi = (Button) v.findViewById(R.id.btnEliminarChordSi);
        Button eliminarNo = (Button) v.findViewById(R.id.btnEliminarChordNo);

        eliminarNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
            }
        });

        eliminarSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarChordTask eliminarTask = new EliminarChordTask();
                eliminarTask.execute();
                dc.dismis();
            }
        });

        dc.show();
    }

    private void postEvent(Integer id){
        FragmentEventChanged fce = new FragmentEventChanged();
        fce.setId(id);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(fce);
    }

    class CargarNombreUsuarioTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            final String respuesta = Conexion.getInstancia().getInfoUsuario(chordActual.getIdUsuario());
            Log.e("Respuesta get nombre",respuesta);
            Gson gson = new Gson();
            if(respuesta!=null){
                try {
                    final Response response = gson.fromJson(respuesta, Response.class);
                    if (response != null) {
                        if(response.getStatus().equals("error")){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),response.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            usuarioChord = gson.fromJson(response.getMessage(),Usuario.class);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _usuario.setText(usuarioChord.getName());
                                }
                            });

                        }
                    }
                } catch (JsonSyntaxException e){
                    Log.e("Error","Json Syntax Exception:"+respuesta);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _usuario.setText("No disponible");
                        }
                    });
                }
            }
            return null;
        }
    }

    class EliminarChordTask extends AsyncTask<Void,Void,Void> {

        String respuesta;
        Response response;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Eliminando Chord");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            respuesta = Conexion.getInstancia().eliminoChord(chordActual.getId());
            if(respuesta!=null){
                try{
                    response = gson.fromJson(respuesta,Response.class);
                    if(response.getStatus().equals("ok")){
                        Objeto.delete(getContext(),chordActual.getId()+"");
                        BaseDeDatos.borrarChord(getActivity(),chordActual.getId()+"");
                    }
                } catch (JsonSyntaxException e){
                    Log.e("Error eliminar chord","JsonSyntax: "+e.getStackTrace());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                if(response.getStatus().equals("error")){
                    Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
                } else if(response.getStatus().equals("ok")) {
                    Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"Error desconocido: "+response.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            dialogos.hideProgressDialog();
            postEvent(4);
            super.onPostExecute(aVoid);
        }
    }

    class EnviarChordCorreo extends AsyncTask<Mensaje,Void,Void> {

        String respuesta;
        Response response;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Enviando chord");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Mensaje... params) {
            respuesta = Conexion.getInstancia().enviarMensaje(params[0]);
            if(respuesta!=null || respuesta.isEmpty()){
                response = gson.fromJson(respuesta,Response.class);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                if(response.getStatus().equals("error")){
                    Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
                } else if(response.getStatus().equals("ok")){
                    Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"Error desconocido: "+response.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }
}
