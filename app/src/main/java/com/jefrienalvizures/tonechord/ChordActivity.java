package com.jefrienalvizures.tonechord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Linea;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.events.ListaChordsChanged;
import com.jefrienalvizures.tonechord.events.WebAppChordEvent;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.lib.WebAppInterface;
import com.jefrienalvizures.tonechord.net.Conexion;

import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChordActivity extends AppCompatActivity {

    Chord chordActual;
    @Bind(R.id.wbChordActivity)
    WebView wb;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private com.jefrienalvizures.tonechord.lib.EventBus eventBus;
    public int chordOrigen = 0;
    Usuario usuarioActual = null;
    private MenuItem itemFav=null;
    Dialogos dialogos;
    private long lastClick;
    int toolbarStatus = 1;
    boolean bloquear=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);
        ButterKnife.bind(this);
        setupToolbar();
        loadUser();
        dialogos = new Dialogos(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("origen").equals("local")){
            chordLocal(bundle.getInt("id"));
            chordOrigen = 0;
        } else if(bundle.getString("origen").equals("cloud")){
            chordOrigen = 1;
            this.chordActual = new Gson().fromJson(bundle.getString("chord"),Chord.class);
            if(chordActual!=null){
                chordActual.setId(bundle.getInt("id"));
                new LoadCloudLinesTask().execute();
            }
        } else {
            chordOrigen = 2;
            chordLocal(bundle.getInt("id"));
            Log.e("ID FAV",bundle.getInt("id")+"");
        }
        eventBus = GreenRobotEventBus.getInstance();
        eventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.untegister(this);
    }


    @Subscribe
    public void onEventMainThread(final WebAppChordEvent event){
        // your implementation
        //Toast.makeText(this, "ID:"+event.getId(), Toast.LENGTH_SHORT).show();
        if(event.isEjecutar()){
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lanzar(event.getChord());
                }
            });
        }
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(this);
    }

    public void lanzar(String a){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.ver_chord_dialogo, null);
        // builder.setView(v);
        final AlertDialog ad = builder.create();
        ad.setView(v);

        TextView txtChord = (TextView) v.findViewById(R.id.textoChordDialogo);
        txtChord.setText(a.substring(0,1).toUpperCase()+a.substring(1).toLowerCase());

        WebView wv = (WebView) v.findViewById(R.id.webView);
        wv.addJavascriptInterface(new WebAppInterface(this),"Android");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/www/index.html?a="+a);

        ad.show();
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setShowHideAnimationEnabled(true);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(
                R.menu.menu_chord, menu);
        return true;
    }

    private boolean isChecked = false;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.checkable_menu);

        this.itemFav = menu.findItem(R.id.checkable_menu);
        if(chordOrigen==0){
            itemFav.setVisible(false);
        } else if(chordOrigen==2){
            isChecked = true;
        }
        checkable.setChecked(isChecked);
        estadoChecke();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.checkable_menu:
                isChecked = !isChecked;
                item.setChecked(isChecked);
                estadoChecke();
                new ChangedFavoriteTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void estadoChecke()
    {
        if(isChecked){
            itemFav.setIcon(R.drawable.ic_favorite_red_24dp);
        } else {
            itemFav.setIcon(R.drawable.ic_favorite_white_24dp);
        }
    }

    public void chordLocal(int id){
        chordActual =  Objeto.read(this,String.valueOf(id));
        chordActual.setId(id);
        mostrarLineas();
    }

    public void mostrarLineas(){
        String contenidoFinal = "";



        if(chordActual!=null){
            Gson gson = new Gson();
            Log.e("LINEAS",chordActual.getLineas());
            ArrayList<Linea> lineas = gson.fromJson(chordActual.getLineas(),new TypeToken<ArrayList<Linea>>() {
            }.getType());
            contenidoFinal = cuerpoContenidoHtml(chordActual);
            for(Linea l : lineas){
                contenidoFinal = contenidoFinal + procesoLinea(l.getLinea(),l.getTipo());
            }

            String scripts = "<script>function getChord(var1){\n" +
                    "\t\t\tAndroid.setChord(var1);" +
                    "alert(var1);\n" +
                    "\t\t}</script>" +
                    "" +
                    "<script type=\"text/javascript\" src=\"www/js/jquery-1.9.1.min.js\"></script>" +
                    "<script type=\"text/javascript\" src=\"www/js/letra.js\"></script>";

            String finalHtml = "</div>\n" +
                    "" +
                    "<div class=\"toolbox_options\">\n" +
                    "\t\t\t<table style=\"width:100%;height: 40px;\">\n" +
                    "\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t<td width=\"20%\" onclick=\"playinterval(0)\"><center><span>0</span></center></td>\n" +
                    "\t\t\t\t\t<td width=\"20%\" onclick=\"playinterval(1)\"><center><span>1</span></center></td>\n" +
                    "\t\t\t\t\t<td width=\"20%\" onclick=\"playinterval(2)\"><center><span>2</span></center></td>\n" +
                    "\t\t\t\t\t<td width=\"20%\" onclick=\"playinterval(3)\"><center><span>3</span></center></td>\n" +
                    "\t\t\t\t\t<td width=\"20%\" onclick=\"playinterval(4)\"><center><span>4</span></center></td>\n" +
                    "\t\t\t\t</tr>\n" +
                    "\t\t\t</table>\n" +
                    "\t\t</div>" +
                    "" +
                    "<div class=\"toolbox\">\n" +
                    "\t\t\t<table style=\"width:100%;height: 65px;\">\n" +
                    "\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t<td style=\"background-color: rgba(255,255,255,0.3);color: white; font-size:12px; height: 12px\">\n" +
                    "\t\t\t\t\t\t<center>Info</center>\n" +
                    "\t\t\t\t\t</td>\n" +
                    "\t\t\t\t\t<td style=\"background-color: rgba(255,255,255,0.3);color: white; font-size:12px; height: 12px\">\n" +
                    "\t\t\t\t\t\t<center>Auto</center>\n" +
                    "\t\t\t\t\t</td>\n" +
                    "\t\t\t\t\t<td colspan=\"2\" style=\"background-color: rgba(255,255,255,0.3);color: white; font-size:12px; height: 12px\">\n" +
                    "\t\t\t\t\t\t<center>Tonalidad</center>\n" +
                    "\t\t\t\t\t</td>\n" +
                    "\t\t\t\t</tr>\n" +
                    "\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t<td width=\"25%\"><span onclick=\"infoChord()\"><center><img src=\"www/img/info.png\" height=\"40px\"></center></span></td>\n" +
                    "\t\t\t\t\t<td width=\"25%\"><span onclick=\"autoChord()\"><center><img src=\"www/img/auto.png\" height=\"40px\"></center></span></td>\n" +
                    "\t\t\t\t\t<td width=\"25%\"><span onclick=\"subirTono()\"><center><img src=\"www/img/flecha_arriba_normal.png\" height=\"40px\"></center></span></td>\n" +
                    "\t\t\t\t\t<td width=\"25%\"><span onclick=\"bajarTono()\"><center><img src=\"www/img/flecha_abajo_normal.png\" height=\"40px\"></center></span></td>\n" +
                    "\t\t\t\t</tr>\n" +
                    "\t\t\t</table>\n" +
                    "\t\t</div>" +
                    "\t</body>\n" +
                    "</html>";

            contenidoFinal = contenidoFinal +scripts+ finalHtml;
            wb.getSettings().setJavaScriptEnabled(true);
            wb.addJavascriptInterface(new WebAppInterface(this,chordActual),"Android");
            wb.loadDataWithBaseURL("file:///android_asset/", contenidoFinal, "text/html", "utf-8", null);
           // wb.loadData(prueba, "text/html; charset=utf-8", "UTF-8");

            toolbar.setTitle(chordActual.getTitulo());

        }
    }

    public String cuerpoContenidoHtml(Chord c){
        String resultado = "";

        String meta ="<html>"
                        +"<head>"
                            +"<title>Prueba</title>"
                            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">"
                            + "";
        String estilo = "<link type=\"text/css\" rel=\"stylesheet\" href=\"www/css/letra.css\"/>";

        String medioHead = "</head>\n" +
                "\t<body>\n" +
                "\t\n" +
                "\t\t<div class=\"header\">\n" +
                "\t\t\t<table>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<t>TITULO: </t>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<span>"+c.getTitulo()+"</span>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<t>AUTOR: </t>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<span>"+c.getAutor()+"</span>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\t<div class=\"letra\" onclick=\"eventToolbox()\">";

        resultado = meta + estilo + medioHead;

        return resultado;
    }

    public String procesoLinea(String linea,char tipo){
        String lineaFinal = "";
        boolean palabra = false;
        String acordeStr = "";
        Log.e("LINEA",linea);
        if(tipo=='A'){
            for(int x=0; x<linea.length(); x++){
                if(linea.charAt(x)==' ') {
                    if(palabra){
                        lineaFinal = lineaFinal + "<span>" + acordeStr + "</span>";
                        palabra = false;
                        acordeStr="";
                    }
                    lineaFinal = lineaFinal + "&nbsp;";
                } else {
                    palabra = true;
                    if(palabra) {
                        acordeStr = acordeStr + linea.charAt(x);
                    }
                }

                if(x==linea.length()-1){
                    //Log.e("FINAL","TRUE");
                    lineaFinal = lineaFinal + "<br />";
                }
            }
        } else {
            for(int x=0; x<linea.length(); x++){
                if(linea.charAt(x)==' ') {
                    lineaFinal = lineaFinal + "&nbsp;";
                } else {
                    lineaFinal = lineaFinal +linea.charAt(x);
                }

                if(x==linea.length()-1){
                    //Log.e("FINAL","TRUE");
                    lineaFinal = lineaFinal + "<br />";
                }
            }
        }
        return lineaFinal;
    }


    private void postEvent(boolean estado){
        ListaChordsChanged lcc = new ListaChordsChanged();
        lcc.setCambio(estado);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(lcc);
    }

    class ChangedFavoriteTask extends AsyncTask<Void,Void,Void> {
        String respuesta = "";

        @Override
        protected Void doInBackground(Void... params) {
            if(usuarioActual!=null || chordActual!=null) {
                if(isChecked){
                    BaseDeDatos.guardarChordFav(ChordActivity.this,chordActual);
                } else {
                    BaseDeDatos.setFavoritoFalse(ChordActivity.this,chordActual.getId());
                }

                respuesta = Conexion.getInstancia().setFavoritoChord(isChecked, usuarioActual.getId(), chordActual.getId());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(respuesta!=""){
                Response response = new Gson().fromJson(respuesta,Response.class);
                if(response!=null){
                    if(response.getStatus().equals("error")){
                        Toast.makeText(ChordActivity.this,response.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        postEvent(true);
                    }
                }
            }
        }
    }

    class LoadCloudLinesTask extends AsyncTask<Void,Void,Void> {
        Conexion conexion = Conexion.getInstancia();
        String estadoFav="";
        @Override
        protected void onPreExecute() {
            dialogos.showProgressDialog("Leyendo chord");
        }

        @Override
        protected Void doInBackground(Void... params) {
            String lineas = "";

            Log.e("CHORD ACTUAL ID",chordActual.getId()+"");
            lineas = conexion.cargarLineasByChord(chordActual.getId());
            estadoFav = conexion.getFavoritoChord(usuarioActual.getId(),chordActual.getId());
            if(lineas!="") {
                chordActual.setLineas(lineas);
            } else {
                chordActual.setLineas(conexion.cargarLineasByChord(chordActual.getId()));
            }
           Log.e("LINEAS OBTENIDAS",chordActual.getLineas());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(chordActual!=null){
                if(chordActual.getLineas()!=""){
                    //Log.e("LINEAS OBTENIDAS",chordActual.getLineas());
                    if(estadoFav!=""){
                        Response response = new Gson().fromJson(estadoFav,Response.class);
                        if(response!=null){
                            if(response.getStatus().equals("error")){
                                Toast.makeText(ChordActivity.this,response.getMessage(),Toast.LENGTH_SHORT).show();
                            } else {
                                isChecked = Boolean.parseBoolean(response.getMessage());
                                estadoChecke();
                            }
                        }
                    }
                    try {
                        if(!chordActual.getLineas().equals("Vacio")) {
                            mostrarLineas();
                        }
                    } catch (NullPointerException e){
                        Log.e("AsynckTask Lineas","NullPointer");
                    }
                }
            }
            dialogos.hideProgressDialog();
        }
    }
}
