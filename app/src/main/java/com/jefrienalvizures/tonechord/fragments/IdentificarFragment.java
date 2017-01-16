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
import android.widget.ListView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.adapters.AdapterLinea;
import com.jefrienalvizures.tonechord.bean.Linea;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jefrien on 17/09/16.
 */
public class IdentificarFragment extends Fragment{

    ArrayList<Linea> lineas = new ArrayList<>();
    AdapterLinea adaptador;

    public IdentificarFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_identificar, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this,rootView);
        String letra = Comunicator.getLetra();
        InputStream is = new ByteArrayInputStream(letra.getBytes());
        cargoLetra(is);
        cargoLista(rootView);
        Dialogos.ok(getActivity(),getString(R.string.info),getString(R.string.info_identificar_letra));
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

    public void cargoLetra(InputStream is){
        BufferedReader br = null;
        String line;
        try{
            br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine()) != null){
                detectarLinea(line);
            }

        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(br != null){
                try{
                    br.close();

                } catch(IOException e){
                    e.printStackTrace();

                }
            }
        }
    }

    private void detectarLinea(String s) {
        int espacios = 0;
        ArrayList<String> palabras = new ArrayList<String>();
        String tmp ="";
        s = s + " ";
        int t = s.length();
        for(int o =0; o<t; o++){
            char c = s.charAt(o);
            if(c == ' '){
                espacios += 1;
                System.out.println("+1 Espacio");
                if(tmp != ""){
                    palabras.add(tmp);
                    tmp = "";
                }
            } else {
                tmp = tmp + c;
            }
        }

        System.out.println("Numero de palabras: "+palabras.size());
        int cont = 1;
        String[] ar = {"C","D","E","F","G","A","B","Cm","Dm",
                "Em","Fm","Gm","Am","Bm","C#","D#","E#","F#",
                "G#","A#","B#","Cb","Db","Eb","Fb","Gb","Ab","Bb"};
        String[] noA= {"h","i","j","k","l","n","ñ","o","p",
                "q","r","s","t","u","v","w","x","y","z"};

        int acordesE = 0;
        boolean esAcorde = true;

        for(String ss:palabras){
            System.out.println(cont +". "+ss);
            cont++;
            for(int i =  0; i<ar.length;i++){
                //System.out.println("COMPRUEBO "+ar[i]);
                if(ar[i].toString().equals(ss)){
                   // System.out.println("SE ENCONTRO "+ar[i]+" CUANDO "+ss);
                    acordesE++;
                }

            }
            for(int x =0; x<noA.length;x++){
                //System.out.println("COMPRUEBO "+noA[x]+" CON "+ss.toLowerCase());

                if(ss.toLowerCase().contains(noA[x])){
                    //System.out.println("SE ENCONTRO "+noA[x]+" Y NO ES ACORDE = la linea es LETRA ");
                    esAcorde = false;
                }

            }

        }

        if(s.trim().isEmpty()){
            lineas.add(new Linea('B',s));
        } else {
            if (palabras.size() >= 0) {
                if (esAcorde == true) {
                    if (acordesE >= 0 && espacios > 5 || espacios <= 1) {
                        lineas.add(new Linea('A', s));
                    } else if (acordesE > 1) {
                        lineas.add(new Linea('A', s));
                    }
                } else {
                    lineas.add(new Linea('L', s));
                }
            }
        }


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
        postEvent(433537802);
    }

    private void postEvent(Integer id){
        FragmentEventChanged fce = new FragmentEventChanged();
        fce.setId(id);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(fce);
    }
}
