package com.jefrienalvizures.tonechord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.interfaces.InterfaceSolicitudChanged;
import com.jefrienalvizures.tonechord.lib.Comunicator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jefrien on 15/1/2017.
 */
public class InfoUserProfileFragment extends Fragment implements InterfaceSolicitudChanged {

    Gson gson;
    Usuario usuario;
    /** VISTAS **/
    @Bind(R.id.perfilNombreCompleto)
    TextView _nombre;
    @Bind(R.id.perfilCorreoELectronico)
    TextView _email;
    @Bind(R.id.btnSolicitudEstadoUserProfile)
    Button btnEstadoSolicitd;
    String[] datos;

    public static InfoUserProfileFragment newInstance(String[] d){
        InfoUserProfileFragment f = new InfoUserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("datos",d);
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info_user_profile
                        ,container,false);
        ButterKnife.bind(this,rootView);

        gson = new Gson();
        setupInfoUser();
        return rootView;
    }

    public void edit(int estado){
        if(estado == 0){
            btnEstadoSolicitd.setText("Agregar a mis amigos");
        } else if(estado==1) {
            btnEstadoSolicitd.setText("Cancelar solicitud");
        } else {
            btnEstadoSolicitd.setText("Eliminar de mis amigos");
        }
    }


    private void setupInfoUser(){
        try {
            datos = getArguments().getStringArray("datos");
            usuario = new Usuario();
            usuario.setId(Integer.parseInt(datos[0]));
            usuario.setName(datos[1]);
            usuario.setEmail(datos[2]);
            usuario.setImagen(datos[3]);

            _nombre.setText(usuario.getName());
            _email.setText(usuario.getEmail());
        } catch (NullPointerException e){
            Log.e("Excepcion setup info","NullPointer: "+e.getStackTrace());
        }
    }

    @Override
    public void onSendRequest(int estado) {
        if(estado == 0){
            btnEstadoSolicitd.setText("Agregar a mis amigos");
        } else if(estado==1) {
            btnEstadoSolicitd.setText("Cancelar solicitud");
        } else {
            btnEstadoSolicitd.setText("Eliminar de mis amigos");
        }
    }
}
