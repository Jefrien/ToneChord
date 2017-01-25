package com.jefrienalvizures.tonechord;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.ListaChordsChanged;
import com.jefrienalvizures.tonechord.events.ShowHideToolbarEvent;
import com.jefrienalvizures.tonechord.fragments.CloudFragment;
import com.jefrienalvizures.tonechord.fragments.FavoriteFragment;
import com.jefrienalvizures.tonechord.fragments.InicioFragment;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.servicios.MensajeService;

import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    // Declarando variables de vistas xml
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navview)
    NavigationView navView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    Usuario usuarioActual=null;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    private int estadoDonacion=0;
    AdView mAdView;
    private String accionInicioFragment = "null";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mAdView = (AdView) findViewById(R.id.adView);
        try {
            Bundle bundle = getIntent().getExtras();
            if (!bundle.getString("accion").isEmpty()) {
                accionInicioFragment = bundle.getString("accion");
            }
        } catch (NullPointerException e){
            Log.e("Error bundle IF","Bundle nullo: "+e.getStackTrace());
        }

        loadUser();
        setupDrawer();
        setupToolbar();
        estadoDonacion = BaseDeDatos.getEstadoDonacion(this);
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

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
        Comunicator.setEmailUser(usuarioActual.getEmail());
        Comunicator.setActivity(this);
        startService(new Intent(this, MensajeService.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.untegister(this);
    }

    public void setupAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Subscribe
    public void onEventMainThread(ListaChordsChanged event){
        // your implementation
        if(event.isCambio()){
            FragmentManager fm = getSupportFragmentManager();
            FavoriteFragment ff = (FavoriteFragment) fm.findFragmentById(R.id.fragmentFavorite);
            if(ff!=null){
                ff.actualizarLista();
            }
        }
    }


    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
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
                    .setImageBitmap(Objeto.readImage(this,usuarioActual.getImagen()));

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
        String tag="";
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        switch (itemId) {
            // Si se presiona en SALIR
            case R.id.drawer_menu_logout:
                stopService(new Intent(this,MensajeService.class));
                new CerrarSesionTask().execute();
                break;
            // Si se presiona en inicio, Es el default
            case R.id.drawer_menu_inicio:
               // fragment = new InicioFragment();
                tabLayout.getTabAt(1).select();
                break;
            case R.id.drawer_menu_nueva_letra:
                // Nueva letra
                startActivity(new Intent(this,NuevaActivity.class));
                this.finish();
                break;
            // Si se presiona en Perfil
            case R.id.drawer_menu_perfil:
                startActivity(new Intent(this,ProfileActivity.class));
                this.finish();
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
        }
        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_layout, fragment,tag).addToBackStack("tonechord")
                    .commit();
        }
        return fragmentTransaction;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(1).setIcon(R.drawable.music_box);
        tabLayout.getTabAt(0).setIcon(R.drawable.cloud);
        tabLayout.getTabAt(2).setIcon(R.drawable.star);
        tabLayout.getTabAt(1).select();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        final CloudFragment cf = new CloudFragment();
        adapter.addFragment(cf, "CLOUD");
        adapter.addFragment(InicioFragment.newInstance(accionInicioFragment), "INICIO");
        adapter.addFragment(new FavoriteFragment(), "FAVORITOS");
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

    class CerrarSesionTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            BaseDeDatos.cerrarSesion(HomeActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            HomeActivity.this.finish();
        }
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

            //return mFragmentTitleList.get(position).toString();
            return null;
        }
    }
}
