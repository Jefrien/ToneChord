package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewChord;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.events.ShowHideToolbarEvent;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 19/12/2016.
 */
public class InicioFragment extends Fragment {

    /*
    Declarar instancias globales
     */
    private RecyclerView recycler;
    private AdapterCardViewChord adapter;
    private RecyclerView.LayoutManager lManager;
    ArrayList<Chord> items = new ArrayList<>();
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;

    public InicioFragment(){

    }

    public static InicioFragment newInstance(String accion){
        Bundle bundle = new Bundle();
        bundle.putString("accion",accion);
        InicioFragment _f = new InicioFragment();
        _f.setArguments(bundle);
        return _f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_inicio, container, false);
        cargarChords();
        setLista(v);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        if(getArguments().getString("accion").equals("update")){
            actualizarLista();
        }
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inicio, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem busquedaMenu = menu.findItem(R.id.action_search_inicio);
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
                newText = newText.toLowerCase();
                final List<Chord> filteredChordList = new ArrayList<Chord>();
                for(Chord chord:items){
                    final String textAutor = chord.getAutor().toLowerCase();
                    final String textTitulo = chord.getTitulo().toLowerCase();
                    if(textTitulo.contains(newText) || textAutor.contains(newText)){
                        filteredChordList.add(chord);
                    }
                }
                adapter.animateTo(filteredChordList);
                recycler.scrollToPosition(0);
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

    private void actualizarLista(){
        cargarChords();
        adapter.notifyDataSetChanged();
    }

    private void cargarChords(){
        ArrayList<String> keys = BaseDeDatos.cargarChords(getActivity());
        Chord c;
        for(String key : keys){
            c = Objeto.read(getContext(),key);
            llenarLista(c);
        }
        if(items.isEmpty()){
            Toast.makeText(getContext(),"Parece que no tienes chords", Toast.LENGTH_SHORT).show();
        } else {
            Comunicator.setChords(items);

        }
    }

    private void setLista(View rootView){
        try {
            Collections.reverse(items);
            recycler = (RecyclerView) rootView.findViewById(R.id.recicladorInicioFragment);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(lManager);
            adapter = new AdapterCardViewChord(items);
            recycler.setAdapter(adapter);

            recycler.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int idChord = items.get(position).getId();
                            Intent i = new Intent(getActivity(), ChordActivity.class);
                            i.putExtra("id", idChord);
                            i.putExtra("origen", "local");
                            startActivity(i);
                        }
                    })
            );
        } catch (NullPointerException e){
            Log.e("SetLista Inicio","NullPointerEception");
        }

    }

    public void llenarLista(Chord chord){
        items.add(chord);
    }


}
