package com.jefrienalvizures.tonechord;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.fragments.DetalleChordFragment;
import com.jefrienalvizures.tonechord.fragments.EditarChordFragment;
import com.jefrienalvizures.tonechord.fragments.EditarPasoFinalChordFragment;
import com.jefrienalvizures.tonechord.fragments.NuevaLetraFragment;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.DialogoCreator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.net.Conexion;

import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalleChordActivity extends AppCompatActivity {

    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    Chord chordActual;
    String lineas;
    int posicion=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_chord);
        ButterKnife.bind(this);
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        chordActual = new Gson().fromJson(bundle.getString("chord"),Chord.class);
        chordActual.setId(bundle.getInt("id"));
        if(chordActual!=null){
            lineas = bundle.getString("lineas");
            setFragment(1);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.untegister(this);
    }

    @Subscribe
    public void onEventMainThread(FragmentEventChanged event){
        setFragment(event.getId());
    }

    public void setFragment(int valor){
        Fragment fragment = null;
        Gson gson = new Gson();
        String tag = "";
        boolean fragmentTransaction = false;
        switch (valor){
            case 1:
                fragment = DetalleChordFragment.newInstance(chordActual.toJson(),chordActual.getId());
                fragmentTransaction = true;
                tag = "inicioDetalle";
                break;
            case 2:
                fragment = EditarChordFragment.newInstance(lineas);
                fragmentTransaction = true;
                posicion++;
                break;
            case 3:
                fragment = EditarPasoFinalChordFragment.newInstance(chordActual.toJson(),chordActual.getId()
                );
                fragmentTransaction = true;
                posicion++;
                break;
            case 4:
                Intent i = new Intent(this,HomeActivity.class);
                i.putExtra("accion","update");
                startActivity(i);
                this.finish();
                break;
        }

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out)
                    .replace(R.id.fragment_detalle_container, fragment,tag).addToBackStack("tonechord")
                    .commit();
        }
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DetalleChordFragment f = (DetalleChordFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detalle_chord);
        if(posicion==1){
                DetalleChordActivity.this.finish();
                Log.e("Estas","Fragment 1");

        } else {
            super.onBackPressed();
            posicion--;
            Log.e("Estas","Otro p:"+posicion);
        }
    }



}
