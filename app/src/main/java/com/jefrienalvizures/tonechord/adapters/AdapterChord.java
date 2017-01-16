package com.jefrienalvizures.tonechord.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jefrien on 20/12/2016.
 */
public class AdapterChord extends ArrayAdapter<Chord> {
    Context context;
    ArrayList<Chord> listaBuscar;

    public AdapterChord(Context context, List<Chord> lc) {
        super(context,0,lc);
        this.context = context;
        this.listaBuscar = new ArrayList<Chord>();
        this.listaBuscar.addAll(lc);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_chord,parent,false);
        }

        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagenItemChord);
        imagen.setImageDrawable(getImage());
        TextView titulo = (TextView) convertView.findViewById(R.id.itemChord_titulo);
        TextView autor = (TextView) convertView.findViewById(R.id.itemChord_autor);

        Chord chord = getItem(position);
        titulo.setText(chord.getTitulo());
        autor.setText(chord.getAutor());

        return convertView;
    }

    public Drawable getImage(){
        //extraemos el drawable en un bitmap
        Drawable originalDrawable = context.getResources().getDrawable(R.drawable.item_chord_image);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        return roundedDrawable;
    }

    public void filter(String texto){
        texto = texto.toLowerCase(Locale.getDefault());
        this.clear();
        if(texto.length()==0){
            this.addAll(listaBuscar);
        } else {
            for(Chord chord : listaBuscar){
                if(texto.length() != 0 && chord.getTitulo().toLowerCase(Locale.getDefault()).contains(texto)){
                    this.add(chord);
                } else if(texto.length() != 0 && chord.getAutor().toLowerCase(Locale.getDefault()).contains(texto)) {
                    this.add(chord);
                }
            }
        }
        this.notifyDataSetChanged();
    }


}
