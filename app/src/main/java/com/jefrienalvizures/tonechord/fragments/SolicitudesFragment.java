package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewChord;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewSolicitud;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.events.ResponderSolicitudDialogEvent;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 15/1/2017.
 */
public class SolicitudesFragment extends Fragment {

    /*
    Declarar instancias globales
     */
    private RecyclerView recycler;
    private AdapterCardViewSolicitud adapter;
    private RecyclerView.LayoutManager lManager;
    private List<SolicitudDeAmistad> items = new ArrayList<>();
    private Dialogos dialogos;
    private Usuario usuarioActual;
    Gson gson;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;

    public static SolicitudesFragment newInstance(){
        SolicitudesFragment sf = new SolicitudesFragment();

        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_solicitudes,container,false);
        setHasOptionsMenu(true);
        loadUser();
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorSolicitudesFragment);
        gson = new Gson();
        dialogos = new Dialogos(getContext());
        CargarSolicitudesTask cargarSolicitudesTask = new CargarSolicitudesTask();
        cargarSolicitudesTask.execute();
        //setupRecycler(rootView);

        setupRecycler();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        eventBus.untegister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(ResponderSolicitudDialogEvent event){
        // your implementation
        showResponderDialog(event.getPosition());
    }

    public void setupRecycler(){

        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        adapter = new AdapterCardViewSolicitud(getContext(),items);
        Collections.reverse(items);
        recycler.setAdapter(adapter);

    }

    private void reloadItems(){
        items.clear();
        CargarSolicitudesTask cargarSolicitudesTask = new CargarSolicitudesTask();
        cargarSolicitudesTask.execute();
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void showResponderDialog(final int position){
        final DialogoCreator dc = new DialogoCreator(getContext(),getActivity(),R.layout.opciones_solicitud_dialog);
        View v = dc.getView();

        TextView _decsripcion = (TextView) v.findViewById(R.id.descripcion);
        Button _aceptar = (Button) v.findViewById(R.id.btnAceptarSolicitud);
        Button _rechazar = (Button) v.findViewById(R.id.btnRechazarSolicitud);

        _aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer[] params = {items.get(position).getIdAmistad()};
                AceptarSolicitudTask aceptarSolicitudTask = new AceptarSolicitudTask();
                aceptarSolicitudTask.execute(params);
                dc.dismis();
            }
        });

        _rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer[] params = {position};
                RechazarSolicitudTask rechazarSolicitudTask = new RechazarSolicitudTask();
                rechazarSolicitudTask.execute(params);
                dc.dismis();
            }
        });

        dc.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_solicitudes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_solicitudes_cargar:
                CargarSolicitudesTask cargarSolicitudesTask = new CargarSolicitudesTask();
                cargarSolicitudesTask.execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** ASYNCTASKS **/

    class CargarSolicitudesTask extends AsyncTask<Void,Void,Void> {

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

                respuesta = Conexion.getInstancia().getSolicitudesDeAmistad(usuarioActual.getId());
                if(respuesta!=null){
                    if(!respuesta.equals("-")) {
                        Log.e("RESPUESTA S", respuesta);
                        lista = gson.fromJson(respuesta, new TypeToken<List<SolicitudDeAmistad>>() {
                        }.getType());
                        if (lista != null) {
                            for (SolicitudDeAmistad s : lista) {
                                String usuario1, usuario2, imagenstr;
                                Response usuario1Response, usuario2Response;
                                Bitmap imagen;
                                /** RESPONSES **/
                                usuario1Response = gson.fromJson(Conexion.getInstancia().getNombreUsuarioById(s.getIdUsuario1()),
                                        Response.class);
                                usuario2Response = gson.fromJson(Conexion.getInstancia().getNombreUsuarioById(s.getIdUsuario2()),
                                        Response.class);
                                usuario1 = usuario1Response.getMessage();
                                usuario2 = usuario2Response.getMessage();
                                imagenstr = Conexion.getInstancia().getImagenUsuario(s.getIdUsuario1());
                                imagen = Conexion.getInstancia().getImagen(imagenstr);
                                Log.e("RESULTADOS", "" +
                                        " | U1: " + usuario1 +
                                        " | U2: " + usuario2 +
                                        " | IMG: " + imagenstr +
                                        "");
                                listaNueva.add(new SolicitudDeAmistad(
                                        s.getIdAmistad(),
                                        s.getIdUsuario1(),
                                        s.getIdUsuario2(),
                                        0,
                                        usuario1,
                                        usuario2,
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
            try {
                if(listaNueva!=null){
                    items.clear();
                    for (SolicitudDeAmistad s : listaNueva){
                        Log.e("RESULTADOS","" +
                                " | idAmistad: " + s.getIdAmistad() +
                                " | idU1: " + s.getIdUsuario1() +
                                " | idU2: " + s.getIdUsuario2() +
                                " | U1: " + s.getUsuario1() +
                                " | U2: " + s.getUsuario2() +
                                " | IMG: " + s.getImagen().getWidth() +
                                "");
                        items.add(s);
                    }
                  //  adapter.setSolicitudes(listaNueva);
                    adapter.notifyDataSetChanged();
                }
            } catch (NullPointerException e){

            } catch (IndexOutOfBoundsException e){

            }
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

        class AceptarSolicitudTask extends AsyncTask<Integer,Void,Void> {

        String respuesta;
        Response response;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Agregando a tus amigos");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            respuesta = Conexion.getInstancia().aceptarSolicitudDeAmistad(params[0]);
            if(respuesta!=null){
                response = gson.fromJson(respuesta,Response.class);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(response!=null){
                Toast.makeText(getContext(),response.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            reloadItems();
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

    class RechazarSolicitudTask extends AsyncTask<Integer,Void,Void> {

        String respuesta;
        Response response;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Agregando a tus amigos");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            respuesta = Conexion.getInstancia().borrarSolicitudDeAmistad(items.get(params[0]));
            if(respuesta!=null){
                response = gson.fromJson(respuesta,Response.class);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(response!=null){
                Toast.makeText(getContext(),response.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            reloadItems();
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }
}
