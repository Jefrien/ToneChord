package com.jefrienalvizures.tonechord.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Mensaje;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 13/1/2017.
 */
public class AdapterCardViewMensaje extends RecyclerView.Adapter<AdapterCardViewMensaje.MensajeViewHolder> {
    private List<Mensaje> items;

    public static class MensajeViewHolder extends RecyclerView.ViewHolder {
        // Campos del item
        public TextView _mensaje,_emisor;

        public MensajeViewHolder(View v){
            super(v);
            _emisor = (TextView) v.findViewById(R.id.cardview_mensaje_de);
            _mensaje = (TextView) v.findViewById(R.id.cardview_mensaje_txt);
        }
    }

    public AdapterCardViewMensaje(List<Mensaje> items){
        this.items = new ArrayList<>(items);
//        this.itemsCopy.addAll(items);
    }

    @Override
    public MensajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new MensajeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajeViewHolder holder, int position) {
        String emisor = "";
        if(items.get(position).getNombreEnvia()!=null){
            emisor = items.get(position).getNombreEnvia();
        } else {
            emisor = items.get(position).getUsuarioEnvia();
        }
        holder._emisor.setText(emisor);
        holder._mensaje.setText(items.get(position).getMensaje());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
