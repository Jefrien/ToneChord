package com.jefrienalvizures.tonechord;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.HttpFileUploader;
import com.jefrienalvizures.tonechord.lib.ImageHelper;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.net.Conexion;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout ctl;
    @Bind(R.id.perfilCorreoELectronico) TextView correoPerfil;
    @Bind(R.id.perfilNombreCompleto) TextView nombrePerfil;
    @Bind(R.id.perfilTeRegistrasteCOn) TextView proveedorPerfil;
    @Bind(R.id.numeroAgregadasPerfil) TextView numeroAgregadas;
    @Bind(R.id.perfilActivityImagen) ImageView imagenPerfil;
    @Bind(R.id.numeroFavoritasPerfilActivity)
    TextView numeroFavoritas;
    Usuario usuarioActual = null;
    int contadorGiro = 1;
    private static final int IMAGE_CAMERA_CODE = 1;
    private static final int CROP_PIC = 2;
    private static final int IMAGE_REQUEST_CODE = 10;
    Bitmap bitmap=null;
    ImageView imagenPerfilEdit;
    Uri desdeCamara;
    String pathImage;
    Bitmap comprimida;
    File file;
    Dialogos dialogos;
    EditText passEliminar;
    boolean respuestaVerificarInternet=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupToolbar();
        loadUser();
        verificoInternet();
        dialogos = new Dialogos(this);
        new PerfilTask().execute();
    }

    @OnClick(R.id.btnCambiarPassProfile)
    public void cambiarPass(){
        if(verificoInternet()) {
            editPasswordDialog();
        } else {
            noInternetDialog();
        }
    }

    @OnClick(R.id.btnEliminarCuentaProfile)
    public void eliminarCuenta(){
        if(verificoInternet()) {
            eliminarCuentaDialog();
        } else {
            noInternetDialog();
        }
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
        int chordAdd=0;
        for(String s: BaseDeDatos.cargarChords(ProfileActivity.this)){
            chordAdd++;
        }
        proveedorPerfil.setText("Correo Electrónico");
        numeroAgregadas.setText(chordAdd+"");
        ctl.setTitle(usuarioActual.getName());
        nombrePerfil.setText(usuarioActual.getName());
        correoPerfil.setText(usuarioActual.getEmail());
        imagenPerfil.setImageBitmap(Objeto.readImage(this,usuarioActual.getImagen()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_perfil_add:
                if (verificoInternet()){
                    editDialog();
                } else{
                    noInternetDialog();
                }
                break;
            case R.id.menu_perfil_imagen:
                if(verificoInternet()) {
                    editPhotoDialog();
                } else {
                    noInternetDialog();
                }
                break;
            case R.id.home:
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.atras_white);
            ab.setDisplayHomeAsUpEnabled(true);

        }
        toolbar.setNavigationIcon(R.drawable.atras_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                ProfileActivity.this.finish();
            }
        });
    }


    public void editDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_profile_dialog, null);
        final AlertDialog ad = builder.create();
        ad.setView(v);
        final EditText txtEmail = (EditText) v.findViewById(R.id.txtEmailEdit);
        final EditText txtEstado = (EditText) v.findViewById(R.id.txtEstadoEdit);
        final EditText txtName = (EditText) v.findViewById(R.id.txtNameEdit);
        Button btnRegistro = (Button) v.findViewById(R.id.btnEditProfile);
        if(usuarioActual!=null) {
            txtEmail.setText(usuarioActual.getEmail());
            txtEstado.setText(getResources().getString(R.string.estadoDefaultUsuario));
            txtName.setText(usuarioActual.getName());
        }
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String estado = txtEstado.getText().toString();
                String name = txtName.getText().toString();

                if(name.isEmpty() || name.length() < 3){
                    txtName.setError("Minimo 3 caracteres");
                } else {
                    if(email.isEmpty() || email.length() < 5 || !email.contains("@")){
                        txtEmail.setError("Correo no valido");
                    } else {
                        Usuario[] lista = {new Usuario(
                                usuarioActual.getId(),
                                name,
                                email,
                                "",
                                ""
                                )};
                        new EditPerfilTask().execute(lista);
                    }
                }
                ad.dismiss();
            }
        });

        ad.show();
    }

    public void editPasswordDialog() {
        final DialogoCreator dc = new DialogoCreator(this,this,R.layout.cambiar_contrasena_dialog);
        View v = dc.getView();
        final EditText oldPass = (EditText) v.findViewById(R.id.txtOldPass);
        final EditText newPass = (EditText) v.findViewById(R.id.txtNewPass);
        final EditText newPassConfirm = (EditText) v.findViewById(R.id.txtNewPassConfirm);
        Button btnEditPass = (Button) v.findViewById(R.id.btnEditPassword);

        btnEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPass.getText().toString().isEmpty() ||
                        oldPass.getText().toString().length() < 6){
                    oldPass.setError("Contraseña invalida");
                } else if(newPass.getText().toString().isEmpty()
                        || newPass.getText().toString().length() < 6){
                    oldPass.setError(null);
                    newPass.setError("Contraseña invalida");
                } else if(
                        newPassConfirm.getText().toString().isEmpty()
                                || newPassConfirm.getText().toString().length()<6
                        ){
                    newPass.setError(null);
                    newPassConfirm.setError("Contraseña invalida");
                } else if(newPass.getText().toString().equals(newPassConfirm.getText().toString())){
                    newPassConfirm.setError(null);
                    String[] params = {
                            oldPass.getText().toString().trim(),
                            newPass.getText().toString().trim()
                    };
                    new CambiarPassTask().execute(params);
                    dc.dismis();
                } else {
                    newPassConfirm.setError("Contraseñas no conciden");
                }
            }
        });
        dc.show();
    }

    public void eliminarCuentaDialog(){
        final DialogoCreator dc = new DialogoCreator(this,this,R.layout.eliminar_cuenta_dialog);
        View v = dc.getView();

        Button btnNo = (Button) v.findViewById(R.id.btnEliminarCuentaNo);
        Button btnSi = (Button) v.findViewById(R.id.btnEliminarCuentaSi);
        passEliminar = (EditText) v.findViewById(R.id.txtEscribeContrasenaEliminar);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
            }
        });

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passEliminar.getText().toString().isEmpty() ||
                        passEliminar.getText().toString().length() < 6){
                    passEliminar.setError("Contraseña invalida");
                } else {
                    passEliminar.setError(null);
                    new EliminarCuentaTask().execute();
                }
                dc.dismis();
            }
        });

        dc.show();
    }

    private boolean verificoInternet(){

        Thread thread = new Thread() {
            @Override
            public void run() {
                if(!Conexion.getInstancia().verificarConexion()){
                    respuestaVerificarInternet = false;
                } else {
                    respuestaVerificarInternet = true;
                }
            }
        };
        thread.start();
        return respuestaVerificarInternet;
    }

    private void noInternetDialog(){
        final DialogoCreator dc = new DialogoCreator(this,this,R.layout.no_internet_dialog);
        View v = dc.getView();

        Button btnNoInternetHecho = (Button) v.findViewById(R.id.hechoNoInternerDialog);
        btnNoInternetHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
            }
        });

        dc.show();
    }

    public void editPhotoDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_photo_dialog, null);
        final AlertDialog ad = builder.create();
        ad.setView(v);

        ImageButton btnSeleccionar = (ImageButton) v.findViewById(R.id.btnSeleccionarEditProfile);
        Button btnSubirImagen = (Button) v.findViewById(R.id.btnSubirEditProfile);
        Button btnCancelar = (Button) v.findViewById(R.id.btnCancelarEditProfile);
        ImageButton btnTomarFoto = (ImageButton) v.findViewById(R.id.btnFotoEditProfile);
        imagenPerfilEdit = (ImageView) v.findViewById(R.id.imagenEditProfile);
        Button btnGirar = (Button) v.findViewById(R.id.btnGirarEditProfile);

        Log.e("IMAGEN",usuarioActual.getImagen());
        if(usuarioActual.getImagen()!=null){
            if(usuarioActual.getImagen()!="null" || !usuarioActual.getImagen().isEmpty()){
                imagenPerfilEdit.setImageDrawable(imagenPerfil.getDrawable());
            }
        }

        btnGirar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imagenPerfilEdit.setRotation(contadorGiro * 90);
                if(contadorGiro==4) {
                    contadorGiro = 1;
                } else{
                    contadorGiro++;
                }
            }
        });

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureFromCamera();
            }
        });

        btnSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubirImagen().execute();
                ad.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

        ad.show();
    }

    public void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    public void captureFromCamera(){
        file = new File(ProfileActivity.this.getExternalFilesDir("img"), "tmp.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        desdeCamara = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desdeCamara);
        startActivityForResult(intent,IMAGE_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String picturePath="";
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageHelper ih = new ImageHelper(picturePath);
            comprimida = ih.procesoImagen();
            picUri = data.getData();
            performCrop();


        } else if(requestCode == IMAGE_CAMERA_CODE){
            ContentResolver cr = this.getContentResolver();
            try {
                comprimida = android.provider.MediaStore.Images.Media.getBitmap(cr, desdeCamara);
                ImageHelper ih = new ImageHelper(desdeCamara.getPath());
                comprimida = ih.procesoImagen();
                picUri = desdeCamara;
                performCrop();
            }catch (Exception e){}
        }else {
            try {
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                comprimida = extras.getParcelable("data");
                imagenPerfilEdit.setImageBitmap(comprimida);
            } catch (NullPointerException e){
                Log.e("Exception","Imagen null");
            }
        }
    }
    Uri picUri;
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
        this.finish();
    }

    class EditPerfilTask extends AsyncTask<Usuario,Void,Void> {
        String respuesta = "";
        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Modificando perfil");
        }

        @Override
        protected Void doInBackground(Usuario... params) {
            respuesta = Conexion.getInstancia().editarPerfil(params[0]);
            usuarioActual.setName(params[0].getName());
            usuarioActual.setEmail(params[0].getEmail());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("RESPUESTA",respuesta);
            if(respuesta!=""){
                Response response = new Gson().fromJson(respuesta,Response.class);
                if(response!=null){
                    if (response.getStatus().equals("ok")) {
                        Toast.makeText(ProfileActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        BaseDeDatos.modificarPerfil(ProfileActivity.this,usuarioActual);
                        new PerfilTask().execute();
                    } else {
                        Toast.makeText(ProfileActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
            dialogos.hideProgressDialog();
        }
    }

    class PerfilTask extends AsyncTask<Void,Void,Void> {
        int chordAdd=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(String s: BaseDeDatos.cargarChords(ProfileActivity.this)){
                chordAdd++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
                proveedorPerfil.setText("Correo Electrónico");
                numeroAgregadas.setText(chordAdd+"");
                ctl.setTitle(usuarioActual.getName());

        }
    }

    class SubirImagen extends AsyncTask<Void,Void,Void>{
        private String BASE_URL = Conexion.BASE_URL;
        private ProgressDialog progreso;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso = new ProgressDialog(ProfileActivity.this);
            progreso.setMessage("Comprimiendo");
            progreso.show();
            if(comprimida!=null){
                Objeto.writeImage(ProfileActivity.this,"tmp.jpg",comprimida);
                File f = new File(ProfileActivity.this.getExternalFilesDir("img"), "tmp.jpg");
                if(f.exists()){
                    pathImage = f.getAbsolutePath();
                } else {
                    pathImage = null;
                }
            } else {
                Drawable drawable = imagenPerfilEdit.getDrawable();
                comprimida = ((BitmapDrawable)drawable).getBitmap();
                Objeto.writeImage(ProfileActivity.this,"tmp.jpg",comprimida);
                File f = new File(ProfileActivity.this.getExternalFilesDir("img"), "tmp.jpg");
                if(f.exists()){
                    pathImage = f.getAbsolutePath();
                } else {
                    pathImage = null;
                }
            }
            progreso.setMessage("Subiendo");
        }

        @Override
        protected Void doInBackground(Void... params) {


            String[] fileName = pathImage.split("/");

            HttpFileUploader uploader = new HttpFileUploader(BASE_URL,fileName[fileName.length-1],usuarioActual.getId());
            if(pathImage!=null) {
                try {
                    Log.e("PATH",pathImage);
                    uploader.doStart(new FileInputStream(pathImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            String imgUrl = Conexion.getInstancia().getImagenUsuario(usuarioActual.getId());
            if(imgUrl!=null) {
                BaseDeDatos.setImage(ProfileActivity.this, imgUrl);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ProfileActivity.this,"Imagen subida con exito",Toast.LENGTH_SHORT).show();
            File f = new File(ProfileActivity.this.getExternalFilesDir("img"), "tmp.jpg");
            f.delete();
            imagenPerfil.setImageBitmap(Objeto.readImage(ProfileActivity.this,usuarioActual.getImagen()));
            progreso.hide();
        }
    }

    class CambiarPassTask extends AsyncTask<String,Void,Void> {
        String respuesta;
        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Modificando contraseña");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            respuesta = Conexion.getInstancia().setPasswordUsuario(
                    usuarioActual,
                    params[0],
                    params[1]
            );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(respuesta!=null){
                if(respuesta!=""){
                    Response response = new Gson().fromJson(respuesta,Response.class);
                    Toast.makeText(ProfileActivity.this,response.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(ProfileActivity.this,"Error respuesta vacia",
                        Toast.LENGTH_SHORT).show();
            }
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

    class EliminarCuentaTask extends AsyncTask<Void,Void,Void> {
        String respuesta = null;
        String pass;
        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Eliminando cuenta");
            pass = passEliminar.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            respuesta = Conexion.getInstancia().eliminarCuenta(
                    usuarioActual.getId(),
                    usuarioActual.getEmail(),
                    pass
            );
            if(respuesta!=null){
                if(respuesta!=""){
                    final Response response = new Gson().fromJson(respuesta,Response.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this,response.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(respuesta!=null){
                try {
                    final Response response = new Gson().fromJson(respuesta, Response.class);
                    if (!response.getStatus().equals("error")) {
                        dialogos.changeTextProgressDialog("Cerrando sesión");
                        String email = usuarioActual.getEmail();
                        String nombre = usuarioActual.getName();
                        BaseDeDatos.cerrarSesion(ProfileActivity.this);
                        Intent i = new Intent(ProfileActivity.this, EliminarCuenta.class);
                        i.putExtra("email", email);
                        i.putExtra("nombre", nombre);
                        startActivity(i);
                        ProfileActivity.this.finish();
                    }
                } catch (JsonSyntaxException e){
                    Log.e("Error","JsonSyntax: "+respuesta);
                }
            }
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

}
