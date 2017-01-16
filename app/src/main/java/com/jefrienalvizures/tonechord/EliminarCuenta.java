package com.jefrienalvizures.tonechord;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.net.Conexion;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EliminarCuenta extends AppCompatActivity {

    @Bind(R.id.versionAppAboutEliminar)
    TextView aboutApp;
    @Bind(R.id.comentarioEliminarCuenta)
    EditText comentario;
    Dialogos dialogos;
    String email,nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);
        ButterKnife.bind(this);
        dialogos = new Dialogos(this);
        aboutApp.setText(String.format(
                getResources().getString(R.string.version_app),
                BuildConfig.VERSION_NAME));
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        nombre = bundle.getString("nombre");

    }

    @OnClick(R.id.btnEnviarComentarioEliminar)
    public void hecho(){
        validar();
        new EnviarComentarioTask().execute();
    }

    public void validar(){
        if(comentario.getText().toString().trim().isEmpty()
                || comentario.getText().toString().trim().length() == 0){
            comentario.setText("Sin comentarios.");
        }
    }

    class EnviarComentarioTask extends AsyncTask<Void,Void,Void> {

        String comentarioTxt = "";
        String respuesta;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Enviando");
            comentarioTxt = comentario.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String jsonComentario = "{" +
                    "\"email\":\""+email+"\"," +
                    "\"nombre\":\""+nombre+"\"," +
                    "\"comentario\":\""+comentarioTxt+"\"" +
                    "}";

            respuesta = Conexion.getInstancia().enviarComentario(jsonComentario);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialogos.hideProgressDialog();

            if(respuesta!=null){
                Toast.makeText(EliminarCuenta.this,"Comentario enviado",Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(EliminarCuenta.this,MainActivity.class));
            EliminarCuenta.this.finish();
            super.onPostExecute(aVoid);
        }
    }
}
