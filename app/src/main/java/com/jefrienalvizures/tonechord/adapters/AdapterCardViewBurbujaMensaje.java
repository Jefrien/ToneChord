package com.jefrienalvizures.tonechord.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Mensaje;
import com.jefrienalvizures.tonechord.bean.Usuario;
import com.jefrienalvizures.tonechord.lib.BaseDeDatos;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 21/1/2017.
 */
public class AdapterCardViewBurbujaMensaje extends RecyclerView.Adapter<AdapterCardViewBurbujaMensaje.BurbujaViewHolder> {
    private List<Mensaje> items;
    Usuario usuarioActual;
    Activity activity;
    public static class BurbujaViewHolder extends RecyclerView.ViewHolder {
        // Campos del item
        public TextView _mensaje,_codigoChord;
        public LinearLayout _linear,_linearChord;


        public BurbujaViewHolder(View v){
            super(v);
            _mensaje = (TextView) v.findViewById(R.id.itemBurbujaTexto);
            _linear = (LinearLayout) v.findViewById(R.id.layoutBurbujaBG);
            _linearChord = (LinearLayout) v.findViewById(R.id.layoutBurbujaChord);
            _codigoChord = (TextView) v.findViewById(R.id.txtBurbujaCodigoChord);
        }
    }

    public AdapterCardViewBurbujaMensaje(Activity a,List<Mensaje> items){
        this.items = new ArrayList<>(items);
        this.activity = a;
        loadUser();
//        this.itemsCopy.addAll(items);
    }


    @Override
    public BurbujaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_burbuja, parent, false);
        return new BurbujaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BurbujaViewHolder holder, int position) {
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String textoMensaje = items.get(position).getMensaje();
        Boolean estado = textoMensaje.matches("chord_+\\d+#+\\d+#+\\d+.+\\d");
        if(estado){
            holder._linear.setVisibility(View.GONE);
            holder._linearChord.setVisibility(View.VISIBLE);
            holder._codigoChord.setText("Codigo: "+textoMensaje);
        } else {
            holder._linearChord.setVisibility(View.GONE);
            holder._linear.setVisibility(View.VISIBLE);
            if (usuarioActual.getEmail().equals(items.get(position).getUsuarioEnvia())) {
                holder._linear.setBackground(activity.getResources().getDrawable(R.drawable.chat_bubble_to));
                // params.setMargins(60, 0, 0, 0);
            } else {
                holder._linear.setBackground(activity.getResources().getDrawable(R.drawable.chat_bubble_me));
                //   params.setMargins(0, 0, 60, 0);
            }

            //holder._linear.setLayoutParams(params);
            holder._mensaje.setText(items.get(position).getMensaje());
        }
    }

    private void loadUser(){
        usuarioActual = BaseDeDatos.getUsuario(activity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setMensajes(List<Mensaje> mensajes){
        items = new ArrayList<>(mensajes);
    }

    // Animaciones
    public Mensaje removeItem(int position){
        final Mensaje item = items.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position,Mensaje mensaje){
        items.add(position, mensaje);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        final Mensaje mensaje = items.remove(fromPosition);
        items.add(toPosition,mensaje);
        notifyItemMoved(fromPosition,toPosition);
    }

    public void animateTo(List<Mensaje> mensajes){
        applyAndAnimateRemovals(mensajes);
        applyAndAnimateAdditions(mensajes);
        applyAndAnimateMovedItems(mensajes);
    }

    private void applyAndAnimateRemovals(List<Mensaje> newMensajes){
        for(int i = items.size() - 1; i>=0; i--){
            final Mensaje mensaje = items.get(i);
            if(!newMensajes.contains(mensaje)){
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Mensaje> newMensajes){
        for(int i = 0, count = newMensajes.size(); i<count; i++){
            final Mensaje mensaje = newMensajes.get(i);
            if(!items.contains(mensaje)){
                addItem(i,mensaje);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Mensaje> newMensaje) {
        for(int toPosition = newMensaje.size() - 1; toPosition >= 0; toPosition--){
            final Mensaje mensaje = newMensaje.get(toPosition);
            final int fromPosition = items.indexOf(mensaje);
            if(fromPosition >= 0 && fromPosition != toPosition){
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
