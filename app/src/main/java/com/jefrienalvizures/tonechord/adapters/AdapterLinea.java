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
public class AdapterLinea extends ArrayAdapter<Linea> {
    public AdapterLinea(Context context, List<Linea> l) {
        super(context,0,l);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_linea,parent,false);
        }

        TextView t = (TextView) convertView.findViewById(R.id.item_tipo_linea);
        TextView l = (TextView) convertView.findViewById(R.id.item_texto_linea);

        Linea linea = getItem(position);
        t.setText(linea.getTipo()+"");

        if(linea.getTipo() == 'T'){
            t.setBackgroundColor(Color.BLACK);
        } else if(linea.getTipo() == 'A'){
            t.setBackgroundColor(Color.rgb(0,159,239));
        } else if(linea.getTipo() == 'L'){
            t.setBackgroundColor(Color.rgb(55,146,52));
        } else if(linea.getTipo() == 'B'){
            t.setBackgroundColor(Color.rgb(192,187,38));
        } else if(linea.getTipo() == 'E'){
            t.setBackgroundColor(Color.rgb(193,113,113));
        }
        l.setText(linea.getLinea());
        return convertView;
    }
}
