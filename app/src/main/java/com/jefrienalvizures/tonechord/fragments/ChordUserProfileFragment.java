package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
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
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewChord;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 15/1/2017.
 */
public class ChordUserProfileFragment extends Fragment {


    private RecyclerView recycler;
    private AdapterCardViewChord adapter;
    private RecyclerView.LayoutManager lManager;
    List<Chord> items;
    Usuario usuario;
    Gson gson = new Gson();
    String[] datos;

    public static ChordUserProfileFragment newInstance(String[] d){
        ChordUserProfileFragment f= new ChordUserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("datos",d);
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chord_user_profile,
                container,false);
        items = new ArrayList<>();
        setupInfoUser();
        setupRecycler(rootView);
        setLista();
        return rootView;
    }

    private void setupInfoUser(){
        try {
            datos = getArguments().getStringArray("datos");
            usuario = new Usuario();
            usuario.setId(Integer.parseInt(datos[0]));
            usuario.setName(datos[1]);
            usuario.setEmail(datos[2]);
            usuario.setImagen(datos[3]);
        } catch (NullPointerException e){
            Log.e("Excepcion","NullPointer: "+e.getStackTrace());
        }
    }

    public void setupRecycler(View rootView){
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorProfileChordsFragment);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        adapter = new AdapterCardViewChord(items);
        Collections.reverse(items);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int idChord = items.get(position).getId();
                        Intent i = new Intent(getActivity(), ChordActivity.class);
                        i.putExtra("id",idChord);
                        i.putExtra("origen","cloud");
                        i.putExtra("showInfo",false);
                        i.putExtra("chord",items.get(position).toJson());
                        Log.e("DATOS","ID: "+idChord+" | TITULO: "+items.get(position).toJson());
                        startActivity(i);
                    }
                })
        );
    }

    private void setLista(){
        new CargarCloudTask().execute();
    }

    class CargarCloudTask extends AsyncTask<Void,Void,Void> {
        String res;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            res = Conexion.getInstancia().cargarChordsPublicosById(usuario.getId());
            if (res != null) {
                if(!res.equals("-")) {
                    Gson gson = new Gson();
                    ArrayList<Chord> tmp = gson.fromJson(res, new TypeToken<ArrayList<Chord>>() {
                    }.getType());

                    if (tmp != null) {
                        items.clear();
                        for (Chord chord : tmp) {
                            items.add(chord);
                        }
                    } else {
                        ArrayList<Chord> tmp2 = new ArrayList<>();
                        for (Chord c0 : items) {
                            tmp2.add(c0);
                        }
                        items.clear();
                        if (tmp2 != null) {
                            for (Chord chord : tmp2) {
                                items.add(chord);

                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setChords(items);
            adapter.notifyDataSetChanged();
        }
    }
}
