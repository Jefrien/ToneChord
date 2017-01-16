package com.jefrienalvizures.tonechord;

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
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.fragments.ChordUserProfileFragment;
import com.jefrienalvizures.tonechord.fragments.CloudFragment;
import com.jefrienalvizures.tonechord.fragments.FavoriteFragment;
import com.jefrienalvizures.tonechord.fragments.InfoUserProfileFragment;
import com.jefrienalvizures.tonechord.fragments.InicioFragment;
import com.jefrienalvizures.tonechord.fragments.SolicitudesFragment;
import com.jefrienalvizures.tonechord.lib.Comunicator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        gson = new Gson();
        dialogos = new Dialogos(this);
        setupUserPerfil();
        GetDataProfileUser gdpu = new GetDataProfileUser();
        gdpu.execute();
        setupToolbar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupUserPerfil(){
        Bundle bundle = getIntent().getExtras();
        try {
            usuarioPerfil = gson.fromJson(bundle.getString("usuario"),Usuario.class);
            getSupportActionBar().setTitle(usuarioPerfil.getName());
            ctl.setTitle(usuarioPerfil.getName());
            Comunicator.setUsuario(usuarioPerfil);
        } catch (NullPointerException e){}
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

    private void setupTabIcons() {
        //tabLayout.getTabAt(1).setIcon(R.drawable.music_box);
        //tabLayout.getTabAt(0).setIcon(R.drawable.cloud);
        //tabLayout.getTabAt(2).setIcon(R.drawable.star);
        tabLayout.getTabAt(0).select();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ChordUserProfileFragment.newInstance(), "CHORDS");
        adapter.addFragment(InfoUserProfileFragment.newInstance(), "INFORMACIÓN");
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

        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Cargando perfil");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                imagen = Conexion.getInstancia().getImagen(usuarioPerfil.getImagen());
            }catch (NullPointerException e){
                Log.e("Exception UserProfile","NullPointer: "+e.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imagenPerfil.setImageBitmap(imagen);
            dialogos.hideProgressDialog();
            super.onPostExecute(aVoid);
        }
    }
}
