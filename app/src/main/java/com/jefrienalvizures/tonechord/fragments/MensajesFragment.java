package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.EnviarMensajeActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewMensaje;
import com.jefrienalvizures.tonechord.bean.Conversacion;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.interfaces.InterfaceMensajesNuevos;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 13/1/2017.
 */
public class MensajesFragment extends Fragment implements InterfaceMensajesNuevos,SwipeRefreshLayout.OnRefreshListener {

    public static MensajesFragment newInstance(){
        MensajesFragment mf = new MensajesFragment();
        return mf;
    }

    /** DECLARANDO VARIABLES **/
    private RecyclerView recycler;
    private AdapterCardViewMensaje adapter;
    private RecyclerView.LayoutManager lManager;
    private ArrayList<Conversacion> items = new ArrayList<>();
    private Usuario usuarioActual;
    private Gson gson = new Gson();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mensajes,container,false);
        loadUser();
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorMensajesFragment);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragmentMensajesRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        new CargarMensajes().execute();
                                    }
                                }
        );

        return rootView;
    }

    public void actualizar(){
        Log.e("MensajesFragment","Dentro de actualizar");
        //cargarChords();
        adapter.notifyDataSetChanged();
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void cargarChords(ArrayList<Conversacion> tmp){
        if(items!=null){
            items.clear();
        }

        items = tmp;

        if(items.isEmpty()){
            Toast.makeText(getContext(),"Sin mensajes", Toast.LENGTH_SHORT).show();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setLista();
            }
        });
    }

    private void setLista(){
        try {
            //Collections.reverse(items);

            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(lManager);
            adapter = new AdapterCardViewMensaje(getActivity(),items);
            recycler.setAdapter(adapter);

            recycler.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.e("POSITION",position+"");
                            int idConversacion = items.get(position).getIdConversacion();
                            Intent i = new Intent(getActivity(), EnviarMensajeActivity.class);
                            i.putExtra("idConversacion",idConversacion);
                            startActivity(i);
                            getActivity().finish();
                        }
                    })
            );

        } catch (NullPointerException e){
            Log.e("SetLista Inicio","NullPointerEception");
        }

    }

    @Override
    public void onChangedListMensajes(boolean estado) {
        if(estado){
            actualizar();
        }
    }

    @Override
    public void onRefresh() {
        new CargarMensajes().execute();
    }

    class CargarMensajes extends AsyncTask<Void,Void,Void>{

        String respuesta;

        @Override
        protected Void doInBackground(Void... params) {
            try {

                respuesta = Conexion.getInstancia().getConversaciones(usuarioActual.getEmail());
                List<Conversacion> tmp = gson.fromJson(respuesta, new TypeToken<List<Conversacion>>() {
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
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }
    }


}
