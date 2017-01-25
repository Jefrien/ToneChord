package com.jefrienalvizures.tonechord.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;
import com.jefrienalvizures.tonechord.bean.Conversacion;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 13/1/2017.
 */
public class AdapterCardViewMensaje extends RecyclerView.Adapter<AdapterCardViewMensaje.MensajeViewHolder> {
    private List<Conversacion> items;
    private Activity activity;
    private Usuario usuarioActual;
    public static class MensajeViewHolder extends RecyclerView.ViewHolder {
        // Campos del item
        public TextView _mensaje,_emisor;

        public MensajeViewHolder(View v){
            super(v);
            _emisor = (TextView) v.findViewById(R.id.cardview_mensaje_de);
            _mensaje = (TextView) v.findViewById(R.id.cardview_mensaje_txt);
        }
    }

    public AdapterCardViewMensaje(Activity a,List<Conversacion> items){
        this.items = new ArrayList<>(items);
        this.activity = a;
        loadUser();
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
        if(items.get(position).getUsuario1()!=null){
            if(usuarioActual.getName().equals(items.get(position).getUsuario1())) {
                emisor = items.get(position).getUsuario2();
            } else {
                emisor = items.get(position).getUsuario1();
            }
        } else {
            emisor = items.get(position).getUsuario1();
        }
        holder._emisor.setText(emisor);
        holder._mensaje.setText(items.get(position).getMensaje());
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(activity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
