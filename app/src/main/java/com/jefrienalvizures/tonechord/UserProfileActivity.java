package com.jefrienalvizures.tonechord;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.fragments.ChordUserProfileFragment;
import com.jefrienalvizures.tonechord.fragments.CloudFragment;
import com.jefrienalvizures.tonechord.fragments.FavoriteFragment;
import com.jefrienalvizures.tonechord.fragments.InfoUserProfileFragment;
import com.jefrienalvizures.tonechord.fragments.InicioFragment;
import com.jefrienalvizures.tonechord.fragments.SolicitudesFragment;
import com.jefrienalvizures.tonechord.interfaces.InterfaceSolicitudChanged;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.perfilActivityImagen)
    ImageView imagenPerfil;
    Usuario usuarioPerfil;
    Gson gson;
    Dialogos dialogos;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout ctl;
    String[] datos;
    MenuItem itemAddFriend;
    int estadoRequest=0;
    Usuario usuarioActual;
    boolean isEnviada=false;
    Response responseEstado;

    /** INTERFACES **/
    private InterfaceSolicitudChanged listener;

    public void setListener(InterfaceSolicitudChanged i){
        this.listener = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        gson = new Gson();
        dialogos = new Dialogos(this);
        setupUserPerfil();
        loadUser();
        GetDataProfileUser gdpu = new GetDataProfileUser();
        gdpu.execute();
        setupToolbar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupUserPerfil(){
        Bundle bundle = getIntent().getExtras();


        datos = bundle.getStringArray("usuario");
        Log.e("Recibido json",datos.length+"");
        for(String s:datos){
            Log.e("String",s);
        }
        usuarioPerfil = new Usuario();
        usuarioPerfil.setId(Integer.parseInt(datos[0]));
        usuarioPerfil.setName(datos[1]);
        usuarioPerfil.setEmail(datos[2]);
        usuarioPerfil.setImagen(datos[3]);
        ctl.setTitle(usuarioPerfil.getName());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);

        }

    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(
                R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        itemAddFriend = menu.findItem(R.id.menu_perfil_add_friend);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                UserProfileActivity.this.finish();
                break;
            case R.id.menu_perfil_add_friend:
                SetSolicitudEstatus setSolicitudEstatus = new SetSolicitudEstatus();
                if(responseEstado.getMessage().equals("2")) {
                    shoDialogEliminarAmigo();
                } else {
                    if (isEnviada) {
                        isEnviada = !isEnviada;
                        itemAddFriend.setIcon(R.drawable.ic_person_add_white);
                        // Toast.makeText(this,"Solicitud de amistad enviada",Toast.LENGTH_SHORT).show();
                    } else {
                        isEnviada = !isEnviada;
                        itemAddFriend.setIcon(R.drawable.account_remove);
                        //Toast.makeText(this,"Solicitud de amistad cancelada",Toast.LENGTH_SHORT).show();
                    }
                    setSolicitudEstatus.execute();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shoDialogEliminarAmigo(){
        final DialogoCreator dc = new DialogoCreator(this,this,R.layout.eliminar_amigo_dialog);
        View v = dc.getView();

        Button btnSiEliminarAmigo = (Button) v.findViewById(R.id.btnEliminarAmigoDialogSi);
        Button btnNoEliminarAmigo = (Button) v.findViewById(R.id.btnEliminarAmigoDialogNo);

        btnNoEliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc.dismis();
            }
        });

        btnSiEliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarAmigo eliminarAmigo = new EliminarAmigo();
                eliminarAmigo.execute();
                dc.dismis();
            }
        });

        dc.show();
    }

    private void setupTabIcons() {
        //tabLayout.getTabAt(1).setIcon(R.drawable.music_box);
        //tabLayout.getTabAt(0).setIcon(R.drawable.cloud);
        //tabLayout.getTabAt(2).setIcon(R.drawable.star);
        tabLayout.getTabAt(0).select();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ChordUserProfileFragment.newInstance(datos), "CHORDS");
        adapter.addFragment(InfoUserProfileFragment.newInstance(datos), "INFORMACIÓN");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position).toString();
        }
    }

    /** AsynckTasks **/

    class GetDataProfileUser extends AsyncTask<Void,Void,Void> {

        Bitmap imagen;
        String estadoAmistad;

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Cargando perfil");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                estadoAmistad = Conexion.getInstancia().getEstadoAmistad(usuarioPerfil.getEmail(),usuarioActual.getEmail());
                responseEstado = gson.fromJson(estadoAmistad,Response.class);

                imagen = Conexion.getInstancia().getImagen(usuarioPerfil.getImagen());
            }catch (NullPointerException e){
                Log.e("Exception UserProfile","NullPointer: "+e.getStackTrace());
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(responseEstado.getStatus().equals("error")){
                Log.e("Error estado response",responseEstado.getMessage());
            } else {
                if(responseEstado.getMessage().equals("1")){
                    itemAddFriend.setIcon(R.drawable.account_remove);
                } else if(responseEstado.getMessage().equals("2")){
                    itemAddFriend.setIcon(R.drawable.account_check);
                } else {
                    itemAddFriend.setIcon(R.drawable.ic_person_add_white);
                }
            }
            imagenPerfil.setImageBitmap(imagen);
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }

    class SetSolicitudEstatus extends AsyncTask<Void,Void,Void> {

        String res;
        Response response;

        @Override
        protected Void doInBackground(Void... params) {
            SolicitudDeAmistad solicitud = new SolicitudDeAmistad(
                    0,
                    usuarioActual.getEmail(),
                    usuarioPerfil.getEmail(),
                    1,
                    null,
                    null,
                    null
            );

            try {
                Log.e("Estado",""+isEnviada);
                if(!isEnviada) {
                    Log.e("LANZANDO","eliminar");
                    res = Conexion.getInstancia().borrarSolicitudDeAmistad(solicitud);
                } else {
                    Log.e("LANZANDO","enviar");
                    res = Conexion.getInstancia().enviarSolicitudDeAmistad(solicitud);
                }
                if (res != null) {
                    response = gson.fromJson(res, Response.class);
                }
            } catch (NullPointerException e){

            } catch (JsonSyntaxException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                Toast.makeText(UserProfileActivity.this,
                        response.getMessage(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this,
                        "Ocurrio un error",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }
    }

    class EliminarAmigo extends AsyncTask<Void,Void,Void> {

        Response response;

        @Override
        protected Void doInBackground(Void... params) {
            String respuesta = Conexion.getInstancia()
                    .eliminarAmigo(
                            usuarioPerfil.getEmail(),
                            usuarioActual.getEmail()
                    );

            try {
                response = gson.fromJson(respuesta, Response.class);
            } catch (JsonSyntaxException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response!=null){
                if(response.getStatus().equals("ok")){
                    itemAddFriend.setIcon(R.drawable.ic_person_add_white);
                    Toast.makeText(UserProfileActivity.this,
                    response.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileActivity.this,
                            response.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(aVoid);
        }
    }
}
