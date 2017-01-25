package com.jefrienalvizures.tonechord.fragments;

import android.content.Intent;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.HomeActivity;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.net.Conexion;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jefrien on 18/12/2016.
 */
public class LoginFragment extends Fragment {

    /**
     * By Jefrien Armando Alvizures Martínez
     *
     * Login Fragment
     */

    /** Declaro las variables iniciales **/
    @Bind(R.id.txtEmailLog)
    EditText _email;
    @Bind(R.id.txtPasswordLog)
    EditText _password;
    @Bind(R.id.btnLogin)
    Button boton;
    Conexion conexion;
    Usuario usuarioLog;

    Dialogos dialogos;

    /** Constructor vacio de la clase **/
    public LoginFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_login, container, false);      // Inflo el layout del fragmento
        ButterKnife.bind(this,v);                                                   // Configuro Butterknife
        dialogos = new Dialogos(getContext());
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** Click en el boton de login **/
    @OnClick(R.id.btnLogin)
    public void login(){
        if(validar()){
            LoginTask lt = new LoginTask();
            lt.execute();
        }
    }

    /** Metodo a ejcutar despues de un registro **/
    public void desdeRegistro(String txt){
        _email.setText(txt);
    }

    /** Metodo para validar los campos **/
    private boolean validar(){
        boolean respuesta = false;
        String e = _email.getText().toString().trim();
        String p = _password.getText().toString().trim();

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

        return respuesta;
    }

    /** AsyncTask para loguear a un usuario **/
    class LoginTask extends AsyncTask<Void,Void,Void> {

        String email,password,res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogos.showProgressDialog("Iniciando sesion");
            this.email = _email.getText().toString();
            this.password = _password.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            conexion = Conexion.getInstancia();
            if(conexion.verificarConexion()) {
                if (email != null || password != null) {
                    String strUsuario = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
                    res = conexion.loginUsuario(strUsuario);


                }
            } else {
                res = "nc";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Gson gson = new Gson();
            if(res!=null) {
                if(res.equals("nc")){
                    dialogos.hideProgressDialog();
                    Toast.makeText(getContext(), "Parece que no tienes internet", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("RESPUESTA JSON",res);
                    Response response = gson.fromJson(res, Response.class);
                    if (response != null) {
                        Log.e("ESTADO", response.getStatus());
                        if (response.getStatus().equals("ok")) {

                            usuarioLog = gson.fromJson(response.getMessage(), Usuario.class);
                            BaseDeDatos.iniciarSesion(getActivity(), usuarioLog);
                            dialogos.changeTextProgressDialog("Cargando chords");
                            LoginGetInfoTask lgit = new LoginGetInfoTask();
                            lgit.execute();
                        } else {
                            dialogos.hideProgressDialog();
                            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }

        }
    }

    /** Metodo para obtener los datos del usuario **/
    class LoginGetInfoTask extends AsyncTask<Void, Void, Void> {

        String res;
        String resFav="";
        String statusDonationStr;
        Response donacion;
        Gson gson = new Gson();


        @Override
        protected Void doInBackground(Void... params) {
            if(usuarioLog!=null){
                res = conexion.cargarChordsById(usuarioLog.getId()+"");
                resFav = conexion.cargarChordsFavoritosById(usuarioLog.getId()+"");
                statusDonationStr = Conexion.getInstancia().getStatusDonation(usuarioLog.getId());
                if(statusDonationStr!=null){
                    donacion = gson.fromJson(statusDonationStr,Response.class);
                }
                try {
                    BaseDeDatos.setEstadoDonacion(getActivity(), Integer.parseInt(donacion.getMessage()));
                } catch (NullPointerException e){
                    e.printStackTrace();
                }

                String resImg = conexion.getImagenUsuario(usuarioLog.getId());
                if(res!=null) {
                    if(resImg!=null){
                        BaseDeDatos.setImage(getActivity(),resImg);
                    }

                    if(!res.equals("-")) {
                        try {
                            Gson gson = new Gson();
                            Log.e("Error json", res);
                            List<Chord> lista = gson.fromJson(res, new TypeToken<List<Chord>>() {
                            }.getType());

                            for (Chord c : lista) {
                                String lineas = conexion.cargarLineasByChord(c.getId());
                                c.setLineas(lineas);
                                BaseDeDatos.guardarChordIf(getActivity(), c);
                            }

                            if (!resFav.equals("-")) {
                                List<Chord> listaF = gson.fromJson(resFav, new TypeToken<List<Chord>>() {
                                }.getType());

                                for (Chord c : listaF) {
                                    String lineas = conexion.cargarLineasByChord(c.getId());
                                    c.setLineas(lineas);
                                    Log.e("MANDO DESDE LOGIN", c.getTitulo());
                                    BaseDeDatos.guardarChordFav(getActivity(), c);
                                }
                            }
                        } catch (JsonSyntaxException e){
                            Log.e("Cargo chords","Syntax json error");
                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(res!=null){
                if(res.equals("-")){
                    Toast.makeText(getContext(),"Bienvenido a ToneChord", Toast.LENGTH_SHORT).show();
                }
                dialogos.hideProgressDialog();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            } else {
                dialogos.hideProgressDialog();
            }
        }
    }
}
