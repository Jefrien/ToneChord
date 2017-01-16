package com.jefrienalvizures.tonechord.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.net.Conexion;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien on 10/1/2017.
 */
public class EditarPasoFinalChordFragment extends Fragment {

    @Bind(R.id.tituloEditarPasoFinal)
    EditText tituloFinal;
    @Bind(R.id.autorEditarPasoFinal) EditText autorFinal;
    @Bind(R.id.switchEditarPublico)
    SwitchCompat publico;
    @Bind(R.id.switchEditarAnonimo) SwitchCompat anonimo;
    private Usuario usuarioActual = null;
    Dialogos dialogos;
    Chord chordActual;

    public static EditarPasoFinalChordFragment newInstance(String chord,int id){
        EditarPasoFinalChordFragment epfcf = new EditarPasoFinalChordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("chord",chord);
        bundle.putInt("id",id);
        epfcf.setArguments(bundle);
        return epfcf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar_pasofinal,container,false);
        ButterKnife.bind(this,rootView);
        loadUser();
        Gson gson = new Gson();
        dialogos = new Dialogos(getContext());
        try {
            chordActual = gson.fromJson(getArguments().getString("chord"), Chord.class);
            loadActual();
        } catch (JsonSyntaxException e){
            Log.e("PasoFinalEditar","Exception: "+e.getStackTrace());
        } catch (ClassCastException e){
            Log.e("PasoFinalEditar","Exception: "+e.getStackTrace());
        }
        return rootView;
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(getActivity());
    }

    private void loadActual(){
        tituloFinal.setText(chordActual.getTitulo());
        autorFinal.setText(chordActual.getAutor());
        publico.setChecked(chordActual.isPublico());
        anonimo.setChecked(chordActual.isAnonimo());
    }

    @OnClick(R.id.button_siguiente_editar_chord)
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

    class PasoFinalTask extends AsyncTask<Void,Void,Void> {
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
                    getArguments().getInt("id"),
                    _autor,
                    _titulo,
                    _publico,
                    _anonimo,
                    lineas,
                    chordActual.getIdUsuario()
            );
            Conexion conexion = Conexion.getInstancia();
            respuesta = conexion.editoChord(c,usuarioActual);
            response = gson.fromJson(respuesta, Response.class);
            if(response!=null) {
                    Objeto.edit(getContext(),c.getId()+"",c);

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
                    postEvent(4);
                }
            } catch (NullPointerException e){
            }
        }

        private void postEvent(Integer id){
            FragmentEventChanged fce = new FragmentEventChanged();
            fce.setId(id);
            EventBus event = GreenRobotEventBus.getInstance();
            event.post(fce);
        }
    }

}
