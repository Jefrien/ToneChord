package com.jefrienalvizures.tonechord.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.util.Log;
import com.google.gson.Gson;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.Objeto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jefrien on 18/12/2016.
 */
public class Conexion {

    private static Conexion INSTANCIA = null;
    private Gson gson;
    private Conexion(){}

    private synchronized static void crearInstancia(){
        if(INSTANCIA == null){
            INSTANCIA = new Conexion();
        }
    }

    public static Conexion getInstancia(){
        crearInstancia();
        return INSTANCIA;
    }

    // Online
    //public static String BASE_URL = "http://www.estc.96.lt/index.php";
    //public static String BASE_URL_IMG = "http://www.estc.96.lt/uploads/";
    // Local
    public static String BASE_URL = "http://192.168.137.1/wsTC/index.php";
    public static String BASE_URL_IMG = "http://192.168.137.1/wsTC/uploads/";
    // paypal donation Button
    public static String DONATION_PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=ZESZJ64HPN27J";





    public String editoChord(Chord chord,Usuario usuario){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.e("Lineas enviado",chord.getLineas());
            Log.e("Chord id",chord.getId()+"");
            Log.e("JSON ENVIADO",chord.toJson());

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("18","UTF-8")
                    + "&l=" + URLEncoder.encode(chord.getLineas(),"UTF-8")
                    + "&id=" + URLEncoder.encode(chord.getId()+"","UTF-8")
                    + "&data=" + URLEncoder.encode(chord.toJson(),"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String eliminoChord(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.e("JSON ENVIADO",id+"");

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("19","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String enviarComentario(String dataStr){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("20","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String registroChord(Chord chord,Usuario usuario){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            Log.e("JSON ENVIADO",chord.toJson());

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("2","UTF-8")
                    + "&l=" + URLEncoder.encode(chord.getLineas(),"UTF-8")
                    + "&data=" + URLEncoder.encode(chord.toJson(),"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String cargarChordsById(String id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("3","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String cargarLineasByChord(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("6","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String cargarChordsPublicos(){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("4","UTF-8")
                    + "&data=" + URLEncoder.encode("","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String setFavoritoChord(boolean estado,int idUsuario,int idChord){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));
            String data="";
            if(estado) {
                // Construir los datos a enviar
                data = "action=" + URLEncoder.encode("8", "UTF-8")
                        + "&data=" + URLEncoder.encode("{\"usuario\":\""+idUsuario+"\",\"chord\":\""+idChord+"\"}", "UTF-8");
            } else {
                data = "action=" + URLEncoder.encode("9", "UTF-8")
                        + "&data=" + URLEncoder.encode("{\"usuario\":\""+idUsuario+"\",\"chord\":\""+idChord+"\"}", "UTF-8");
            }

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String getFavoritoChord(int idUsuario,int idChord){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));
            String data="";
                data = "action=" + URLEncoder.encode("10", "UTF-8")
                        + "&data=" + URLEncoder.encode("{\"usuario\":\""+idUsuario+"\",\"chord\":\""+idChord+"\"}", "UTF-8");

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String cargarChordsFavoritosById(String id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("11","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String buscarChordsCloud(String txt){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("12","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"txt\":\""+txt+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public Boolean verificarConexion(){
        Boolean res = false;
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            String data = "action=" + URLEncoder.encode("100","UTF-8")
                    + "&data=" + URLEncoder.encode("","UTF-8");

            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
            while(br.readLine() != null){
                respuesta = respuesta+br.readLine();
            }

        } catch (MalformedURLException mue){

        } catch (IOException e){
//            Log.e("Error",e.getMessage());
        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        if(respuesta.equals("ok")){
            res = true;
        }

        return res;
    }

    /** USUARIO **/

    public String regitroUsuario(Usuario usuario){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            Gson gson = new Gson();
            String usuarioStr = gson.toJson(usuario);
            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("0","UTF-8")
                    + "&data=" + URLEncoder.encode(usuarioStr,"UTF-8");

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String loginUsuario(String json){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("1","UTF-8")
                    + "&data=" + URLEncoder.encode(json,"UTF-8");

            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String getImagenUsuario(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            Log.e("JSON ENVIADO","{\"id\":\""+id+"\"}");

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("14","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");

            //Log.e("JSON USUSARIO",usuario.toJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String getInfoUsuario(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            Log.e("JSON ENVIADO","{\"id\":\""+id+"\"}");

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("17","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");

            //Log.e("JSON USUSARIO",usuario.toJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String setPasswordUsuario(Usuario usuario,String oldPass,String newPass){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            String json = "{\"id\":\""+usuario.getId()+"\"," +
                    "\"email\":\""+usuario.getEmail()+"\"," +
                    "\"password\":\""+oldPass+"\"," +
                    "\"newPassword\":\""+newPass+"\"}";
            Log.e("JSON ENVIADO PASS",json);
            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("15","UTF-8")
                    + "&data=" + URLEncoder.encode(json,"UTF-8");

            //Log.e("JSON USUSARIO",usuario.toJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String eliminarCuenta(int id,String email,String pass){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            String json = "{\"id\":\""+id+"\",\"email\":\""+email+"\",\"password\":\""+pass+"\"}";
            Log.e("JSON ENVIADO PASS",json);
            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("16","UTF-8")
                    + "&data=" + URLEncoder.encode(json,"UTF-8");

            //Log.e("JSON USUSARIO",usuario.toJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String editarPerfil(Usuario usuario){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("7","UTF-8")
                    + "&data=" + URLEncoder.encode(usuario.editToJson(),"UTF-8");

            Log.e("JSON USUSARIO EDIT",usuario.editToJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public Bitmap getImagen(String imagen){
        Bitmap resultado = null;

        try {
            URL urlImagen = new URL(BASE_URL_IMG + imagen);
            resultado = BitmapFactory.decodeStream(urlImagen.openConnection().getInputStream());
            if(resultado!=null){
                Log.e("Imagen","Obtenida con exito");
            }
        } catch (MalformedURLException mue){

        } catch (IOException e){

        }
        return resultado;
    }

    public String getNombreUsuarioById(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            Log.e("JSON ENVIADO","{\"id\":\""+id+"\"}");

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("28","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");

            //Log.e("JSON USUSARIO",usuario.toJson());
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    /** CHORDS **/

    public String cargarChordsPublicosById(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();


            //Log.e("JSON ENVIADO",chord.toJson(usuario.getId()+""));

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("30","UTF-8")
                    + "&data=" + URLEncoder.encode("{\"id\":\""+id+"\"}","UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    /** AMIGOS **/

    public String borrarSolicitudDeAmistad(SolicitudDeAmistad solicitud){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = solicitud.toJson();
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("31","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String enviarSolicitudDeAmistad(SolicitudDeAmistad solicitud){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = solicitud.toJson();
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("26","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String getSolicitudesDeAmistad(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"id\":\""+id+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("27","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String aceptarSolicitudDeAmistad(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"id\":\""+id+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("29","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    public String getAmigos(int id){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            String dataStr = "{\"id\":\""+id+"\"}";
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("32","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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

    /** MENSAJES **/

    public String enviarMensaje(Mensaje mensaje){
        String respuesta = "";
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            gson = new Gson();
            String dataStr = gson.toJson(mensaje);
            Log.e("JSON ENVIADO",dataStr);

            // Construir los datos a enviar
            String data = "action=" + URLEncoder.encode("21","UTF-8")
                    + "&data=" + URLEncoder.encode(dataStr,"UTF-8");


            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            out.write(data.getBytes());
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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
            BufferedReader  br = new BufferedReader(new InputStreamReader(in));
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
}
