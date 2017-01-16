package com.jefrienalvizures.tonechord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterLinea;
import com.jefrienalvizures.tonechord.bean.Linea;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien on 10/1/2017.
 */
public class EditarChordFragment extends Fragment{
    ArrayList<Linea> lineas = new ArrayList<>();
    AdapterLinea adaptador;

    public static EditarChordFragment newInstance(String jsonLineas){
        EditarChordFragment ecf = new EditarChordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lineas",jsonLineas);
        ecf.setArguments(bundle);
        return ecf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_identificar, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this,rootView);
        Gson gson = new Gson();
        lineas = gson.fromJson(getArguments().getString("lineas"),new TypeToken<ArrayList<Linea>>() {
        }.getType());
        if(lineas!=null) {
            cargoLista(rootView);
        }
        return rootView;
    }

    private void cargoLista(View rootView){
        ListView lista = (ListView) rootView.findViewById(R.id.listaLineasIdentificar);
        adaptador = new AdapterLinea(getActivity(),lineas);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Linea li = adaptador.getItem(i);
                cambioLista(i,li.getLinea());

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editarLineaDialog(position);
                return false;
            }
        });

    }

    public void editarLineaDialog(final int index){
        final DialogoCreator dc = new DialogoCreator(
                getContext(),getActivity(),R.layout.editar_linea_dialog);
        View v = dc.getView();

        final EditText nuevoTexto = (EditText) v.findViewById(R.id.textoEditarLineaDialog);
        Button btnEditar = (Button) v.findViewById(R.id.btnEditarLineaDialog);
        nuevoTexto.setText(lineas.get(index).getLinea());
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nuevoTexto.getText().toString().length()==0){
                    nuevoTexto.setText(" ");
                }

                lineas.set(index,
                        new Linea(
                                lineas.get(index).getTipo(),
                                nuevoTexto.getText().toString()
                        )
                );
                adaptador.notifyDataSetChanged();
                dc.dismis();
            }
        });

        dc.show();
    }

    public void cambioLista(final int index, final String texto){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.selec_tipo, null);
        // builder.setView(v);
        final AlertDialog ad = builder.create();
        ad.setView(v);
        ad.show();

        Button btnAcorde = (Button) v.findViewById(R.id.acorde_tipo_button);
        Button btnLetra = (Button) v.findViewById(R.id.letra_tipo_button);
        Button btnBlanco = (Button) v.findViewById(R.id.blanco_tipo_button);
        Button btnEliminar = (Button) v.findViewById(R.id.eliminada_tipo_button);

        btnAcorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineas.set(index,new Linea('A',texto));
                adaptador.notifyDataSetChanged();
                ad.dismiss();
            }
        });

        btnLetra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineas.set(index,new Linea('L',texto));
                adaptador.notifyDataSetChanged();
                ad.dismiss();
            }
        });

        btnBlanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineas.set(index,new Linea('B',texto));
                adaptador.notifyDataSetChanged();
                ad.dismiss();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineas.set(index,new Linea('E',texto));
                adaptador.notifyDataSetChanged();
                ad.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.identificar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_ayuda:
                Dialogos.ok(
                        getActivity(),
                        getString(R.string.ayuda_nueva_letra),
                        getString(R.string.ayuda_identificar_letra)
                );
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_siguiente_editar_chord)
    public void siguiente(){
        siguienteRecrearLista();
    }

    private void siguienteRecrearLista(){
        ArrayList<Linea> lineasNueva = new ArrayList<>();
        for(Linea l : lineas){
            if(l.getTipo() != 'E'){
                lineasNueva.add(l);
            }
        }
        Comunicator.setLineas(lineasNueva);
        postEvent(3);
    }

    private void postEvent(Integer id){
        FragmentEventChanged fce = new FragmentEventChanged();
        fce.setId(id);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(fce);
    }
}
