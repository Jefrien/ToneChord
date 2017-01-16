package com.jefrienalvizures.tonechord.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.net.Conexion;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class PasoFinalFragment extends Fragment {


    @Bind(R.id.tituloPasoFinal) EditText tituloFinal;
    @Bind(R.id.autorPasoFinal) EditText autorFinal;
    @Bind(R.id.switchPublico) SwitchCompat publico;
    @Bind(R.id.switchAnonimo) SwitchCompat anonimo;
    private static View vista;
    private Usuario usuarioActual = null;
    Dialogos dialogos;

    public PasoFinalFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pasofinal,container,false);
        ButterKnife.bind(this,rootView);
        dialogos = new Dialogos(getContext());
        vista = rootView;
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUser();
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void postEvent(Integer id){
        FragmentEventChanged fce = new FragmentEventChanged();
        fce.setId(id);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(fce);
    }

    @OnClick(R.id.button_siguiente_chord)
    public void dialogoSiguente() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.atras_dialog, null);
        final AlertDialog ad = builder.create();
        ad.setView(v);
        Button btn0 = (Button) v.findViewById(R.id.btnSi);
        Button btn1 = (Button) v.findViewById(R.id.btnNo);
        TextView txt = (TextView) v.findViewById(R.id.tituloDialog);
        txt.setText("SEGURO?");
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasoFinalTask pft = new PasoFinalTask();
                pft.execute();
                ad.dismiss();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });
        ad.show();
    }


    class PasoFinalTask extends AsyncTask<Void,Void,Void>{
        String _titulo,_autor;
        Boolean _anonimo,_publico;
        Chord c = null;
        String respuesta;
        Response response;
        Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            this._titulo = tituloFinal.getText().toString();
            this._autor = autorFinal.getText().toString();
            this._anonimo = anonimo.isChecked();
            this._publico = publico.isChecked();
            dialogos.showProgressDialog("Creando chord");
        }

        @Override
        protected Void doInBackground(Void... params) {

            String lineas = gson.toJson(Comunicator.getLineas());

            if(_titulo.isEmpty()){
                _titulo = "Desconocido";
            }
            if(_autor.isEmpty()){
                _autor = "Desconocido";
            }

            c = new Chord(
                    0,
                    _autor,
                    _titulo,
                    _publico,
                    _anonimo,
                    lineas,
                    usuarioActual.getId()
            );

            Conexion conexion = Conexion.getInstancia();
            respuesta = conexion.registroChord(c,usuarioActual);
            response = gson.fromJson(respuesta, Response.class);
           if(response!=null) {
               if(response.getMessage()!=null && response.getMessage().contains(":")) {
                   String[] parts = response.getMessage().split(":");
                   c.setId(Integer.parseInt(parts[1]));
                   BaseDeDatos.guardarChordIf(getActivity(), c);
               }
           }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialogos.hideProgressDialog();
            try {
                if (respuesta != null && response != null) {

                    if (response.getMessage() != null) {
                        String[] parts = response.getMessage().split(":");
                        Toast.makeText(getContext(), parts[0], Toast.LENGTH_SHORT).show();
                    }
                    postEvent(R.id.drawer_menu_inicio);
                }
            } catch (NullPointerException e){
            }
        }
    }

}
