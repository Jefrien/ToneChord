package com.jefrienalvizures.tonechord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.Comunicator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jefrien on 15/1/2017.
 */
public class InfoUserProfileFragment extends Fragment {

    Gson gson;
    Usuario usuario;
    /** VISTAS **/
    @Bind(R.id.perfilNombreCompleto)
    TextView _nombre;
    @Bind(R.id.perfilCorreoELectronico)
    TextView _email;

    public static InfoUserProfileFragment newInstance(){
        InfoUserProfileFragment f = new InfoUserProfileFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info_user_profile
                        ,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        gson = new Gson();
        setupInfoUser();
        super.onCreate(savedInstanceState);
    }

    private void setupInfoUser(){
        try {
            usuario = Comunicator.getUsuario();
            _nombre.setText(usuario.getName());
            _email.setText(usuario.getEmail());
        } catch (NullPointerException e){
            Log.e("Excepcion","NullPointer: "+e.getStackTrace());
        }
    }
}
