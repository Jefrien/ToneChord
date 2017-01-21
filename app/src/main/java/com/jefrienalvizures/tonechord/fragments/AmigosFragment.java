package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewAmigo;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewSolicitud;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 14/1/2017.
 */
public class AmigosFragment extends Fragment {

    /*
   Declarar instancias globales
    */
    private RecyclerView recycler;
    private AdapterCardViewAmigo adapter;
    private RecyclerView.LayoutManager lManager;
    private List<SolicitudDeAmistad> items;
    private Dialogos dialogos;
    private Usuario usuarioActual;
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
        dialogos = new Dialogos(getContext());
        loadUser();

        CargarAmigosTask cargarAmigosTask = new CargarAmigosTask();
        cargarAmigosTask.execute();
        setupRecycler(rootView);

        //setupRecycler();
        return rootView;
    }

    public void setupRecycler(View rootView){
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorAmigosFragment);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    /** ASYNCTASKS **/

    class CargarAmigosTask extends AsyncTask<Void,Void,Void> {

        String respuesta;
        List<SolicitudDeAmistad> lista;
        List<SolicitudDeAmistad> listaNueva = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Cargando");
            listaNueva.clear();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            respuesta = Conexion.getInstancia().getAmigos(usuarioActual.getId());
            if(respuesta!=null){
                if(!respuesta.equals("-")) {
                    Log.e("RESPUESTA S", respuesta);
                    lista = gson.fromJson(respuesta, new TypeToken<List<SolicitudDeAmistad>>() {
                    }.getType());
                    if (lista != null) {
                        for (SolicitudDeAmistad s : lista) {
                            String imagenstr;
                            Bitmap imagen;
                            imagenstr = Conexion.getInstancia().getImagenUsuario(s.getIdUsuario1());
                            imagen = Conexion.getInstancia().getImagen(imagenstr);
                            listaNueva.add(new SolicitudDeAmistad(
                                    s.getIdAmistad(),
                                    s.getIdUsuario1(),
                                    s.getIdUsuario2(),
                                    s.getEstado(),
                                    s.getUsuario1(),
                                    s.getUsuario2(),
                                    imagen
                            ));

                        }
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
                    //items.clear();
                    for (SolicitudDeAmistad s : listaNueva){
                        Log.e("RESULTADOS AM","" +
                                " | idAmistad: " + s.getIdAmistad() +
                                " | idU1: " + s.getIdUsuario1() +
                                " | idU2: " + s.getIdUsuario2() +
                                " | U1: " + s.getUsuario1() +
                                " | U2: " + s.getUsuario2() +
                                " | IMG: " + s.getImagen().getWidth() +
                                "");
                    }
                    adapter = new AdapterCardViewAmigo(getContext(),listaNueva);
                    Collections.reverse(listaNueva);
                    recycler.setAdapter(adapter);
                }

            dialogos.hideProgressDialog();

            super.onPostExecute(aVoid);
        }
    }
}
