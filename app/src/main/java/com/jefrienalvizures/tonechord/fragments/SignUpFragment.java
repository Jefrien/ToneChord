package com.jefrienalvizures.tonechord.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.RegisterEvent;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.net.Conexion;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien on 18/12/2016.
 */
public class SignUpFragment extends Fragment{

    /**
     * By Jefrien Armando Alvizures Martínez
     *
     * Registro Fragment
     */

    /** Declaro variables iniciales **/
    @Bind(R.id.txtNameReg)
    EditText _name;
    @Bind(R.id.txtEmailReg)
    EditText _email;
    @Bind(R.id.txtPasswordReg)
    EditText _password;
    Conexion conexion;
    Dialogos dialogos;

    public SignUpFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this,v);
        dialogos = new Dialogos(getContext());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.btnReg)
    public void registrar(){
        if(validar()){
            RegistrarTask rt = new RegistrarTask();
            rt.execute();
        }
    }

    public Usuario crearUsuario(){
        Usuario usuarioNuevo = new Usuario(
                0,
                _name.getText().toString(),
                _email.getText().toString(),
                _password.getText().toString(),
                ""
        );
        return usuarioNuevo;
    }

    private boolean validar(){
        boolean respuesta = false;
        String n = _name.getText().toString().trim();
        String e = _email.getText().toString().trim();
        String p = _password.getText().toString().trim();

        if(n.isEmpty() || n.length() < 4){
            _name.setError("Minimo 4 caracteres");
            respuesta = false;
        } else {
            _name.setError(null);
            respuesta = true;
            if(e.isEmpty() || e.length() < 4 || !e.contains("@")){
                _email.setError("Correo invalido");
                respuesta = false;
            } else {
                _email.setError(null);
                respuesta = true;
                if(p.isEmpty() || p.length() < 4){
                    _password.setError("Minimo 4 caracteres");
                    respuesta = false;
                } else {
                    _password.setError(null);
                    respuesta = true;
                }
            }
        }
        return respuesta;
    }

    private void postEvent(String res){
        RegisterEvent cse = new RegisterEvent(res);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(cse);
    }

    class RegistrarTask extends AsyncTask<Void,Void,Void> {

        Usuario user=null;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user =crearUsuario();
            dialogos.showProgressDialog("Registrando");
        }

        @Override
        protected Void doInBackground(Void... params) {
            conexion = Conexion.getInstancia();
            if(user!=null) {
                res = conexion.regitroUsuario(user);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Gson gson = new Gson();
            if(res!=null) {
                Response response = gson.fromJson(res, Response.class);
                if(response!=null) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    postEvent(_email.getText().toString());
                }
            }
            dialogos.hideProgressDialog();
        }
    }
}
