package com.jefrienalvizures.tonechord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.ActivityChange;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.fragments.IdentificarFragment;
import com.jefrienalvizures.tonechord.fragments.NuevaLetraFragment;
import com.jefrienalvizures.tonechord.fragments.PasoFinalFragment;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;

import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NuevaActivity extends AppCompatActivity {


    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    Usuario usuarioActual=null;

    // Declarando variables de vistas xml

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navview)
    NavigationView navView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private int estadoDonacion=0;
    AdView mAdView;

    // Declarando variables globales normales
    String tag = "inicio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva);
        ButterKnife.bind(this);
        loadUser();
        setupDrawer();
        setupToolbar();
        setItemDrawer(R.id.drawer_menu_nueva_letra);
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
        estadoDonacion = BaseDeDatos.getEstadoDonacion(this);
        mAdView = (AdView) findViewById(R.id.adView);
        if(estadoDonacion==1){
            Log.e("No ha donado","Muestro publicidad");
            mAdView.setVisibility(View.VISIBLE);
            setupAds();
        } else if(estadoDonacion==2){
            mAdView.setVisibility(View.GONE);
            Log.e("Ya dono","Oculto publicidad");
        } else {
            Log.e("Desconocido","Estado es desconocido");
            mAdView.setVisibility(View.VISIBLE);
            setupAds();
        }
    }

    public void setupAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.untegister(this);
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
    }

    @Subscribe
    public void onEventMainThread(FragmentEventChanged event){
        // your implementation
        //Toast.makeText(this, "ID:"+event.getId(), Toast.LENGTH_SHORT).show();
        setItemDrawer(event.getId().intValue());
    }

    @Subscribe
    public void onEventMainThread(ActivityChange event){
        // your implementation
        //Toast.makeText(this, "ID:"+event.getId(), Toast.LENGTH_SHORT).show();
        switch (event.getId()){
            case 3:
                /*Intent intent = new Intent(NuevaActivity.this, VerChordActivity.class);
                intent.putExtra("archivo",event.getParam());
                startActivity(intent);*/
                break;
        }
    }

    private void setupDrawer() {
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if(setItemDrawer(menuItem.getItemId())){
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        View header = navView.getHeaderView(0);
        if(usuarioActual!=null) {
            ((ImageView) header.findViewById(R.id.imageProfileNavViewHeader))
                    .setImageBitmap(Objeto.readImage(this, usuarioActual.getImagen()));

            ((TextView) header.findViewById(R.id.txtNavHeaderUser)).setText(usuarioActual.getName());
            ((TextView) header.findViewById(R.id.txtNavHeaderEmail)).setText(usuarioActual.getEmail());
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean setItemDrawer(int itemId) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        switch (itemId) {
            // Si se presiona en SALIR
            case R.id.drawer_menu_logout:
                new CerrarSesionTask().execute();
                break;
            // Si se presiona en inicio, Es el default
            case R.id.drawer_menu_inicio:
                startActivity(new Intent(this,HomeActivity.class));
                this.finish();
                tag = "inicio";
                break;
            // Si se presiona en Nueva Letra
            case R.id.drawer_menu_nueva_letra:
                // viewPager.setVisibility(View.GONE);
                fragment = new NuevaLetraFragment();
                fragmentTransaction = true;
                tag = "nueva";
                break;
            // Si se presiona en Perfil
            case R.id.drawer_menu_perfil:
                startActivity(new Intent(this,ProfileActivity.class));
                this.finish();
                //startActivity(new Intent(this, PerfilActivity.class));
                break;
            case R.id.drawer_menu_inbox:
                startActivity(new Intent(this,MensajesActivity.class));
                this.finish();
                break;
            case R.id.drawer_menu_acercade:
                new Dialogos().aboutDialog(this);
                break;
            case R.id.drawer_menu_donar:
                startActivity(new Intent(this,DonateActivity.class));
                break;
            // Si se presiona siguiente en nueva letra
            case 433537801:
                fragment = new IdentificarFragment();
                fragmentTransaction = true;
                tag = "identificar";
                break;
            // Si se presiona siguiente en identificar
            case 433537802:
                fragment = new PasoFinalFragment();
                fragmentTransaction = true;
                tag = "pasofinal";
                break;
        }


        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out)
                    .replace(R.id.content_layout, fragment,tag).addToBackStack("tonechord")
                    .commit();

        }
        return fragmentTransaction;
    }



    public void dialogoAtras() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.atras_dialog, null);
        // builder.setView(v);
        final AlertDialog ad = builder.create();
        ad.setView(v);
        Button btn0 = (Button) v.findViewById(R.id.btnSi);
        Button btn1 = (Button) v.findViewById(R.id.btnNo);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
                setItemDrawer(R.id.drawer_menu_inicio);
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

    class CerrarSesionTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            BaseDeDatos.cerrarSesion(NuevaActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(NuevaActivity.this,MainActivity.class));
            NuevaActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {

            NuevaLetraFragment nuevaletrafragment = (NuevaLetraFragment) getSupportFragmentManager().findFragmentByTag("nueva");
            if(nuevaletrafragment != null && nuevaletrafragment.isVisible())
            {
                dialogoAtras();

            }else {
                super.onBackPressed();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }




}
