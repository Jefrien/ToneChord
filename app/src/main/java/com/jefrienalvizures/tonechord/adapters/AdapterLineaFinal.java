package com.jefrienalvizures.tonechord.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Linea;

import java.util.List;

/**
 * Created by Jefrien Alvizures on 17/09/16.
 */
public class AdapterLineaFinal extends ArrayAdapter<Linea> {
    public AdapterLineaFinal(Context context, List<Linea> l) {
        super(context,0,l);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_linea_final,parent,false);
        }

        TextView l = (TextView) convertView.findViewById(R.id.item_texto_linea);

        Linea linea = getItem(position);
        l.setText(linea.getLinea());
        return convertView;
    }
}
