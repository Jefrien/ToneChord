package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewChord;
import com.jefrienalvizures.tonechord.adapters.AdapterChord;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import com.jefrienalvizures.tonechord.net.Conexion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 20/12/2016.
 */
public class CloudFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /*
     Declarar instancias globales
      */
    private RecyclerView recycler;
    private AdapterCardViewChord adapter;
    private RecyclerView.LayoutManager lManager;
    String res = null;
    List<Chord> items;
    Conexion conexion = Conexion.getInstancia();
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinator;

    public CloudFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_cloud, container, false);
        items = new ArrayList<>();

        setupRecycler(v);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragmentCloudRefresh);
        coordinator = (CoordinatorLayout) v.findViewById(R.id.fragmentCloud);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        setLista();
                                    }
                                }
        );

        hiloVerificarConexion();
        verificarConexion();
        setHasOptionsMenu(true);
        return v;
    }

    public void setupRecycler(View rootView){
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorCloudFragment);
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
                        i.putExtra("chord",items.get(position).toJson());
                        Log.e("DATOS","ID: "+idChord+" | TITULO: "+items.get(position).toJson());
                        startActivity(i);
                    }
                })
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cloud, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem busquedaMenu = menu.findItem(R.id.action_search_cloud);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(busquedaMenu);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] txtBuscar = {newText};
                new BuscarChordCloudTask().execute(txtBuscar);
                //lista.invalidate();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search_cloud:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setLista(){
        new CargarCloudTask().execute();
    }

    @Override
    public void onRefresh() {
        setLista();
    }

    public void verificarConexion(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hiloVerificarConexion();
                handler.postDelayed(this, 4000); //now is every 2 minutes
            }
        }, 4000);
    }

    public void hiloVerificarConexion(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                if(!Conexion.getInstancia().verificarConexion()){
                    showNoInternet();
                } else {
                    if(snackbar!=null){
                        snackbar.dismiss();
                    }
                }
            }
        };
        thread.start();
    }
    Snackbar snackbar=null;
    public void showNoInternet(){
        if(snackbar==null || !snackbar.isShown()) {
            snackbar = Snackbar
                    .make(coordinator, "Sin conexion a internet!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hiloVerificarConexion();
                        }
                    });
            snackbar.show();
        }
    }

    class CargarCloudTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            res = conexion.cargarChordsPublicos();
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
            swipeRefreshLayout.setRefreshing(false);
            adapter.setChords(items);
            adapter.notifyDataSetChanged();
        }
    }

    class BuscarChordCloudTask extends AsyncTask<String,Void,Void> {
        String res = "";
        Gson gson = new Gson();
        List<Chord> listaEncontrada;
        Boolean estado = true;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // swipeRefreshLayout.setRefreshing(true);
            listaEncontrada = new ArrayList<Chord>();
        }

        @Override
        protected Void doInBackground(String... params) {
            res = conexion.buscarChordsCloud(params[0].toString());
            if(params[0].toString().length()==0){
                estado = false;
            } else {
                if (res != null) {
                    if (res.equals("-")) {

                    } else {
                        List<Chord> lista = gson.fromJson(res, new TypeToken<List<Chord>>() {
                        }.getType());
                        if (lista != null) {
                            if (!lista.isEmpty()) {
                                items.clear();
                                items.addAll(lista);
                                listaEncontrada.addAll(lista);
                                Log.e("Agregada", "Tamaño: " + items.size());
                            } else {

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
            if(items!=null){
                    if(estado) {

                        adapter.animateTo(items);
                        recycler.scrollToPosition(0);
                        Log.e("Notifico", "TRUE");
                    } else {
                        new CargarCloudTask().execute();
                        Log.e("ORIGINALES", "TRUE");
                    }
            }

          //  swipeRefreshLayout.setRefreshing(false);
        }
    }
}
