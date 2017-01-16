package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jefrienalvizures.tonechord.ChordActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterCardViewChord;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.lib.RecyclerItemClickListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jefrien on 20/12/2016.
 */
public class FavoriteFragment extends Fragment {

    List<Chord> items;
    /*
   Declarar instancias globales
    */
    private RecyclerView recycler;
    private AdapterCardViewChord adapter;
    private RecyclerView.LayoutManager lManager;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;

    public FavoriteFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_favorite, container, false);
        items = new ArrayList<>();
        setLista(v);
        cargarChords();
        return v;
    }

    public void actualizarLista(){
        cargarChords();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarChords();
    }

    private void setLista(View rootView){
        Collections.reverse(items);
        recycler = (RecyclerView) rootView.findViewById(R.id.recicladorFavFragment);
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
                        i.putExtra("id",idChord);
                        i.putExtra("origen","f");
                        startActivity(i);
                    }
                })
        );


    }


    public void llenarLista(Chord chord){
        items.add(chord);
    }

    public void cargarChords(){
        if(items!=null) {
            items.clear();
            ArrayList<String> keys = BaseDeDatos.cargarChordsFav(getActivity());
            Chord c;
            for (String key : keys) {
                c = Objeto.read(getContext(), key);
                llenarLista(c);
            }
            adapter.animateTo(items);
            recycler.scrollToPosition(0);
        }
    }

}
