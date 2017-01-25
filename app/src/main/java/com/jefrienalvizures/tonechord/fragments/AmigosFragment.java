package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.EnviarMensajeActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.UserProfileActivity;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewAmigo;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewSolicitud;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 14/1/2017.
 */
public class AmigosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    /*
   Declarar instancias globales
    */
    private RecyclerView recycler;
    private AdapterCardViewAmigo adapter;
    private RecyclerView.LayoutManager lManager;
    private List<SolicitudDeAmistad> items = new ArrayList<>();
    private Dialogos dialogos;
    private Usuario usuarioActual;
    private SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;

    public static AmigosFragment newInstance(){
        AmigosFragment af = new AmigosFragment();

        return af;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_amigos,container,false);
        gson = new Gson();
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorAmigosFragment);
        dialogos = new Dialogos(getContext());
        loadUser();
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragmentAmigosRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        CargarAmigosTask cargarAmigosTask = new CargarAmigosTask();
                                        cargarAmigosTask.execute();
                                        setupRecycler();
                                    }
                                }
        );

        return rootView;
    }

    public void setupRecycler(){

        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        adapter = new AdapterCardViewAmigo(getContext(),items);
        Collections.reverse(items);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("POSITION",position+"");
                        String email = items.get(position).getUsuario1();
                        Log.e("EmailInicio",email);
                        if(email.equals(usuarioActual.getEmail())){
                            email = items.get(position).getUsuario2();
                        }
                        Log.e("EmailFinal",email);
                        showDialogAmigos(email);
                    }
                })
        );
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void showDialogAmigos(final String emailAmigo){
        final DialogoCreator dc = new DialogoCreator(getContext(),getActivity(),R.layout.amigo_opciones_dialog);
        View v = dc.getView();
        Button btnEnviarMnesaje = (Button) v.findViewById(R.id.btnAmigoEnviarMensaje);
        Button btnVerPerfil = (Button) v.findViewById(R.id.btnAmigoVerPerfil);
        Button btnEliminarAmigo = (Button) v.findViewById(R.id.btnAmigoEliminarDeMisAmigos);

        btnEnviarMnesaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
                String[] emails = {emailAmigo};
                GetIdConversacion getIdConversacion = new GetIdConversacion();
                getIdConversacion.execute(emails);
            }
        });

        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
                String[] emails = {emailAmigo};
                CargarNombreUsuarioTask cargarNombreUsuarioTask = new CargarNombreUsuarioTask();
                cargarNombreUsuarioTask.execute(emails);
            }
        });

        btnEliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
                String[] emails = {emailAmigo};
                EliminarAmigo eliminarAmigo = new EliminarAmigo();
                eliminarAmigo.execute(emails);
            }
        });

        dc.show();
    }

    private void verPerfil(Usuario usuarioAmigo){
        Intent i = new Intent(getActivity(), UserProfileActivity.class);
        String[] datos = {
                String.valueOf(usuarioAmigo.getId()),
                usuarioAmigo.getName(),
                usuarioAmigo.getEmail(),
                usuarioAmigo.getImagen()
        };
        i.putExtra("usuario", datos);
        Log.e("Usuario enviado", datos.length + "");
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        CargarAmigosTask cargarAmigosTask = new CargarAmigosTask();
        cargarAmigosTask.execute();
    }

    /** ASYNCTASKS **/

    class CargarNombreUsuarioTask extends AsyncTask<String,Void,Void> {
        Usuario usuarioAmigo;
        @Override
        protected Void doInBackground(String... params) {
            try {
                String idRespuesta = Conexion.getInstancia().getIdUsuarioByEmail(params[0]);
                Response responseId = gson.fromJson(idRespuesta,Response.class);
                final String respuesta = Conexion.getInstancia().getInfoUsuario(Integer.parseInt(responseId.getMessage()));
                Log.e("Respuesta get nombre",respuesta);
                Gson gson = new Gson();
                if(respuesta!=null){

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
                            usuarioAmigo = gson.fromJson(response.getMessage(),Usuario.class);
                        }
                    }
                }
                } catch (JsonSyntaxException e){
                    Log.e("Error","Json Syntax Exception:"+e.getStackTrace());
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(usuarioAmigo!=null){
                verPerfil(usuarioAmigo);
            }
            super.onPostExecute(aVoid);
        }
    }

    class EliminarAmigo extends AsyncTask<String,Void,Void> {

        Response response;

        @Override
        protected Void doInBackground(String... params) {
            String respuesta = Conexion.getInstancia()
                    .eliminarAmigo(
                            params[0],
                            usuarioActual.getEmail()
                    );

            try {
                response = gson.fromJson(respuesta, Response.class);
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                if(response.getStatus().equals("ok")){
                    Toast.makeText(getActivity(),
                            response.getMessage(),Toast.LENGTH_SHORT).show();
                    new CargarAmigosTask().execute();
                } else {
                    Toast.makeText(getActivity(),
                            response.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(aVoid);
        }
    }

    class CargarAmigosTask extends AsyncTask<Void,Void,Void> {

        String respuesta;
        List<SolicitudDeAmistad> lista;
        List<SolicitudDeAmistad> listaNueva = new ArrayList<>();

        @Override
        protected void onPreExecute() {
//            dialogos.showProgressDialog("Cargando");
            listaNueva.clear();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            respuesta = Conexion.getInstancia().getAmigos(usuarioActual.getEmail());
            if(respuesta!=null){
                if(!respuesta.equals("-")) {
                    Log.e("RESPUESTA S", respuesta);
                    try {
                        lista = gson.fromJson(respuesta, new TypeToken<List<SolicitudDeAmistad>>() {
                        }.getType());
                        if (lista != null) {
                            for (SolicitudDeAmistad s : lista) {
                                String imagenstr;
                                Bitmap imagen;
                                if (s.getUsuario1().equals(usuarioActual.getEmail())) {
                                    imagenstr = Conexion.getInstancia().getImagenUsuarioByEmail(s.getUsuario2());
                                } else {
                                    imagenstr = Conexion.getInstancia().getImagenUsuarioByEmail(s.getUsuario1());
                                }

                                imagen = Conexion.getInstancia().getImagen(imagenstr);
                                listaNueva.add(new SolicitudDeAmistad(
                                        s.getIdAmistad(),
                                        s.getUsuario1(),
                                        s.getUsuario2(),
                                        s.getEstado(),
                                        s.getUsuario1name(),
                                        s.getUsuario2name(),
                                        imagen
                                ));

                            }
                        }
                    } catch (JsonSyntaxException e){
                        Log.e("Excepcion","JSON SYNTAX: "+e.getMessage());
                    }
                } else {
                    Log.e("Estado","Sin solicitudes");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

                if(listaNueva!=null){
                    items.clear();
                    for (SolicitudDeAmistad s : listaNueva){
                        items.add(s);
                    }
                    adapter.setSolicitudes(items);
                    adapter.notifyDataSetChanged();
                }
            swipeRefreshLayout.setRefreshing(false);
            //dialogos.hideProgressDialog();

            super.onPostExecute(aVoid);
        }
    }

    class GetIdConversacion extends AsyncTask<String,Void,Void> {

        String respuesta;

        @Override
        protected Void doInBackground(String... params) {
            respuesta = Conexion.getInstancia()
                    .getIdConversacion(usuarioActual.getEmail(),params[0]);
            Log.e("RESPUESTA 1",respuesta);
            if(respuesta.trim().equals("-")) {
                respuesta = Conexion.getInstancia()
                        .crearConversacion(usuarioActual.getEmail(),params[0]);
                Log.e("RESPUESTA 2",respuesta);
                respuesta = Conexion.getInstancia()
                        .getIdConversacion(usuarioActual.getEmail(),params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if(!respuesta.trim().equals("-")) {
                    int idConversacion = Integer.parseInt(respuesta);
                    Intent i = new Intent(getActivity(), EnviarMensajeActivity.class);
                    i.putExtra("idConversacion", idConversacion);
                    startActivity(i);
                    getActivity().finish();
                } else {

                }
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

}
