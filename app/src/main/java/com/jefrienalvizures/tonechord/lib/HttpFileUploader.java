package com.jefrienalvizures.tonechord.lib;

/**
 * Created by Jefrien on 4/1/2017.
 */

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpFileUploader {

    URL connectURL;
    String responseString;
    String fileName;
    byte[] dataToServer;
    String idUser;

    public HttpFileUploader(String urlString, String fileName, int idUser){
        try{
            connectURL = new URL(urlString);
        }catch(Exception ex){
            Log.i("URL FORMATION","MALFORMATED URL");
        }

        this.fileName = fileName;
        this.idUser = "{\"id\":\""+idUser+"\"}";

    }

    public void doStart(FileInputStream stream){
        fileInputStream = stream;
        thirdTry();
    }

    FileInputStream fileInputStream = null;
    void thirdTry() {
        String existingFileName = fileName;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="3rd";
        try
        {
            //------------------ CLIENT REQUEST

            Log.e(Tag,"Starting to bad things");

            // PHP Service connection
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"action\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("13");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"data\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(idUser);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag,"Headers are written");

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 2024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Close input stream
            Log.e(Tag,"File is written");
            fileInputStream.close();
            dos.flush();

            InputStream is = conn.getInputStream();
            // retrieve the response from server
            int ch;

            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            String s=b.toString();
            Log.i("Response",s);
            dos.close();

        }
        catch (MalformedURLException ex)
        {
            Log.e(Tag, "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe)
        {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        }
    }
}