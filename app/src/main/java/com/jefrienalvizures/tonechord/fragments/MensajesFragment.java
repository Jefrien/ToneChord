package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewMensaje;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.interfaces.InterfaceMensajesNuevos;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Objeto;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jefrien on 13/1/2017.
 */
public class MensajesFragment extends Fragment implements InterfaceMensajesNuevos {

    public static MensajesFragment newInstance(){
        MensajesFragment mf = new MensajesFragment();
        return mf;
    }

    /** DECLARANDO VARIABLES **/
    private RecyclerView recycler;
    private AdapterCardViewMensaje adapter;
    private RecyclerView.LayoutManager lManager;
    private ArrayList<Mensaje> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mensajes,container,false);
        cargarChords();
        setLista(rootView);
        return rootView;
    }

    public void actualizar(){
        Log.e("MensajesFragment","Dentro de actualizar");
        cargarChords();
        adapter.notifyDataSetChanged();
    }

    private void cargarChords(){
        ArrayList<String> keys = BaseDeDatos.cargarMensajes(getActivity());
        Mensaje c;
        for(String key : keys){
            c = Objeto.readMensaje(getContext(),key);
            items.add(c);
        }
        if(items.isEmpty()){
            Toast.makeText(getContext(),"Sin mensajes", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLista(View rootView){
        try {
            Collections.reverse(items);
            recycler = (RecyclerView) rootView.findViewById(R.id.recicladorMensajesFragment);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(lManager);
            adapter = new AdapterCardViewMensaje(items);
            recycler.setAdapter(adapter);


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
}
