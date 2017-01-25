package com.jefrienalvizures.tonechord;


import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jefrienalvizures.tonechord.events.RegisterEvent;
import com.jefrienalvizures.tonechord.fragments.LoginFragment;
import com.jefrienalvizures.tonechord.fragments.SignUpFragment;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.net.Conexion;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    /**
     * By Jefrien Armando Alvizures Martínez
     *
     * Actividad Login
     */

    /** Declaro las variables iniciales **/
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    @Bind(R.id.loginCoordinator)
    CoordinatorLayout coordinator;
    Snackbar snackbar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);                         // Seteo Butterknife
        setupToolbar();                                 // Configuro la toolbar
        setupTabs();                                    // Configuro las tabs

        eventBus = GreenRobotEventBus.getInstance();    // Obtengo una instancia de eventbus
        eventBus.register(this);                        // Registro los eventos
        setupAds();                                     // Configuro la publicidad
        hiloVerificarConexion();                        // Hilo de verificación de conexión
        verificarConexion();                            // Verificación de conexión inicial
    }

    /** Metodo para configurar publicidad **/
    public void setupAds(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.loadAd(adRequest);
    }

    /** Metodo para verificar la conexion al webservice **/
    public void verificarConexion(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hiloVerificarConexion();
                handler.postDelayed(this, 4000); //now is every 2 minutes
            }
        }, 4000);
    }

    /** Metodo para verificar la conexión constantemente **/
    public void hiloVerificarConexion(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                if(!Conexion.getInstancia().verificarConexion()){
                    showNoInternet();
                } else {
                    if(snackbar!=null){
                        snackbar.dismiss();
                    }
                }
            }
        };
        thread.start();
    }

    /** Metodo para mostrar un snackbar de error de conexión **/
    public void showNoInternet(){
        if(snackbar==null || !snackbar.isShown()) {
            snackbar = Snackbar
                    .make(coordinator, "Sin conexion a internet!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hiloVerificarConexion();
                        }
                    });
            snackbar.show();
        }
    }

    /** Evento eventbus cuando un usuario se registra **/
    @Subscribe
    public void onEventMainThread(RegisterEvent event){
        // your implementation
        viewPager.setCurrentItem(0);
        LoginFragment lf = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLogin);
        if(lf!=null){
            lf.desdeRegistro(event.getEmail());
        }
    }

    /** Metodo para configurar el toolbar **/
    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /** Metodo para configurar las tabs **/
    private void setupTabs(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /** Metodo para configurar el ViewPager de las tabs **/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "INICIAR SESIÓN");
        adapter.addFragment(new SignUpFragment(), "REGISTRO");
        viewPager.setAdapter(adapter);
    }

    /** Clase para controlar el cambio de tabs **/
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
            return mFragmentTitleList.get(position);
        }
    }

}

