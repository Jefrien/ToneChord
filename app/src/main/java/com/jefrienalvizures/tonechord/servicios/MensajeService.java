package com.jefrienalvizures.tonechord.servicios;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Response;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.events.MensajesNuevosEvent;
import com.jefrienalvizures.tonechord.interfaces.InterfaceServiceMensajes;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;
import com.jefrienalvizures.tonechord.lib.Objeto;
import com.jefrienalvizures.tonechord.net.Conexion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 12/1/2017.
 */
public class MensajeService extends Service {

    List<Mensaje> mensajesNuevos;
    String email;
    Gson gson;
    Notification.Builder builder = new Notification.Builder(this);
    NotificationManager notifManager;
    int notif_ref = 1;
    Activity actividad;
    public static String BASE_URL = "http://192.168.137.1/wsTC/index.php";
    boolean estado = true;

    @Override
    public void onCreate() {
        Log.e("Servicio","Iniciado");
        gson = new Gson();
        notifManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        email = Comunicator.getEmailUser();
        actividad = Comunicator.getActivity();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            final Handler handler1 = new Handler();
            Runnable run = null;
        final Runnable finalRun = run;
        handler1.postDelayed(run = new Runnable() {
                public void run() {
                    if(estado) {
                        Log.e("Servicio", "Verificando red");
                        hiloVerificarConexion();
                        if (Comunicator.isInternet()) {
                            Log.e("Servicio", "Obteniendo mensajes");

                           // MensajesNuevosTask mensajesTask = new MensajesNuevosTask();
                            //mensajesTask.execute();
                        }
                    } else {
                        handler1.removeCallbacks(finalRun);
                    }
                    handler1.postDelayed(this, 5000); //now is every 2 minutes
                }
            }, 5000);


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.e("Servicio","Parando");
        estado = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /** Metodo para verificar la conexión constantemente **/
    public void hiloVerificarConexion(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                if(!Conexion.getInstancia().verificarConexion()){
                    Comunicator.setInternet(false);
                } else {
                    Comunicator.setInternet(true);
                }
            }
        };
        thread.start();
    }

    private Notification showNotificationMensaje(Notification.Builder builder,List<Mensaje> lista){

        String mensajestr = "";
        String titulostr = "";
        if(lista.size() == 1){
            mensajestr = "mensaje";
            titulostr = lista.get(0).getNombreEnvia();
        } else {
            mensajestr = "mensajes";
            titulostr = "Mensajes nuevos";
        }


        BitmapDrawable largeIconDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.launcher);
        Bitmap largeIcon = largeIconDrawable.getBitmap();
        builder
                .setContentTitle(titulostr)
                .setContentText(lista.get(0).getMensaje())
                .setContentInfo("ToneChord")
                .setTicker("ToneChord")
                .setTicker("Mensaje nuevo")
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.status_bar_icon);

        Notification.InboxStyle n = new Notification.InboxStyle(builder)
                .setBigContentTitle(titulostr)
                .setSummaryText("Tienes "+lista.size()+" "+mensajestr);

        for(Mensaje m: lista){
            if(lista.size() == 1) {
                n.addLine(m.getMensaje());
            } else {
                String[] nombre = m.getNombreEnvia().split(" ");
                n.addLine(Html.fromHtml("<strong>"+nombre[0]+"</strong>") + ":"+m.getMensaje());
            }
            BaseDeDatos.guardarMensajeIf(actividad,m);
        }

        builder.setPriority(Notification.PRIORITY_HIGH);
        // Sonido por defecto de notificaciones, podemos usar otro
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Uso en API 11 o mayor
        builder.setSound(defaultSound);
        postEvent(true);
        return n.build();
    }

    class MensajesNuevosTask extends AsyncTask<Void,Void,Void> {
        String res;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (email != null || !email.isEmpty()) {
                    res = getMensaje(email);
                    if (res != null) {
                        Log.e("RESPUESTA", res);
                        if (!res.equals("-")) {
                            try {
                                List<Mensaje> tmp = gson.fromJson(res, new TypeToken<List<Mensaje>>() {
                                }.getType());

                                if (tmp != null) {
                                    if (tmp.size() > 0) {

                                        for(Mensaje m: tmp){
                                            String nombre = getNombreEnvia(m.getUsuarioEnvia());
                                            if(nombre!=null){
                                                try {
                                                    Response response = gson.fromJson(nombre,Response.class);
                                                    if(response!=null){
                                                        if(response.getStatus().equals("ok")){
                                                            nombre = response.getMessage();
                                                            m.setNombreEnvia(nombre);
                                                        }
                                                    }
                                                } catch (JsonSyntaxException e){
                                                    Log.e("Excepcion getNombre","JsonSyntax: "+e.getStackTrace());
                                                }
                                            }
                                        }

                                        notifManager.notify(notif_ref, showNotificationMensaje(builder, tmp));
                                        for(Mensaje m : tmp) {
                                            setMensajeRecibido(m.getId());
                                        }
                                    } else {
                                        Log.e("Vacio", "Lista vacia;");
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                Log.e("Error servicio", "JsonSyntax: " + e.getStackTrace());
                            } catch (NullPointerException e) {
                                Log.e("Error servicio", "NullPointer: " + e.getStackTrace());
                            }
                        } else {
                            Log.e("Respuesta", "No hay mensajes");
                            postEvent(false);
                        }
                    }
                }
            } catch (NullPointerException e){

            }
            return null;
        }
    }

    public String getMensaje(String email){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"email\":\""+email+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("22","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while(br.readLine() != null){
                respuesta = respuesta+br.readLine();
            }

        } catch (MalformedURLException mue){

        } catch (IOException e){

        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return respuesta;
    }

    public String getNombreEnvia(String email){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"email\":\""+email+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("24","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while(br.readLine() != null){
                respuesta = respuesta+br.readLine();
            }

        } catch (MalformedURLException mue){

        } catch (IOException e){

        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return respuesta;
    }

    public String setMensajeRecibido(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"id\":\""+id+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("23","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while(br.readLine() != null){
                respuesta = respuesta+br.readLine();
            }

        } catch (MalformedURLException mue){

        } catch (IOException e){

        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return respuesta;
    }

    private void postEvent(boolean estado){
        MensajesNuevosEvent e = new MensajesNuevosEvent();
        e.setEstado(estado);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(e);
    }


}
