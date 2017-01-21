package com.jefrienalvizures.tonechord;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.events.MensajesNuevosEvent;
import com.jefrienalvizures.tonechord.fragments.AmigosFragment;
import com.jefrienalvizures.tonechord.fragments.CloudFragment;
import com.jefrienalvizures.tonechord.fragments.FavoriteFragment;
import com.jefrienalvizures.tonechord.fragments.InicioFragment;
import com.jefrienalvizures.tonechord.fragments.MensajesFragment;
import com.jefrienalvizures.tonechord.fragments.SolicitudesFragment;
import com.jefrienalvizures.tonechord.interfaces.InterfaceMensajesNuevos;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MensajesActivity extends AppCompatActivity {

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
    MensajesFragment mf;


    private InterfaceMensajesNuevos listener;
    public void serListener(InterfaceMensajesNuevos l){
        this.listener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        ButterKnife.bind(this);
        loadUser();
        setupDrawer();
        setupToolbar();
        // setupAds();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.untegister(this);
    }

    @Subscribe
    public void onEventMainThread(MensajesNuevosEvent event){
        // your implementation
//        listener.onChangedListMensajes(event.isEstado());
    }

    /* public void setupAds(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.loadAd(adRequest);
    }*/

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

    public boolean setItemDrawer(int itemId) {
        String tag="";
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
        /*tabLayout.getTabAt(1).setIcon(R.drawable.music_box);
        tabLayout.getTabAt(0).setIcon(R.drawable.cloud);
        tabLayout.getTabAt(2).setIcon(R.drawable.star);
        tabLayout.getTabAt(1).select();*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mf = MensajesFragment.newInstance();
        adapter.addFragment(mf, "MENSAJES");
        adapter.addFragment(AmigosFragment.newInstance(), "AMIGOS");
        adapter.addFragment(SolicitudesFragment.newInstance(), "SOLICITUDES");
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
            BaseDeDatos.cerrarSesion(MensajesActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(MensajesActivity.this,MainActivity.class));
            MensajesActivity.this.finish();
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
            toolbar.setTitle(mFragmentTitleList.get(position).toString());
            return mFragmentTitleList.get(position).toString();
        }
    }
}
