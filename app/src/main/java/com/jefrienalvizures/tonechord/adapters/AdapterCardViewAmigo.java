package com.jefrienalvizures.tonechord.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.SolicitudDeAmistad;
import com.jefrienalvizures.tonechord.events.ResponderSolicitudDialogEvent;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 16/1/2017.
 */
public class AdapterCardViewAmigo
        extends RecyclerView.Adapter<AdapterCardViewAmigo.AmigoViewHolder>
        implements View.OnClickListener {

    private List<SolicitudDeAmistad> items;
    Context context;

    public static class AmigoViewHolder extends RecyclerView.ViewHolder {
        // Campos del item
        public TextView _nombre;
        public ImageView _imagen;

        public AmigoViewHolder(View v){
            super(v);
            _nombre = (TextView) v.findViewById(R.id.item_amigo_nombre);
            _imagen = (ImageView) v.findViewById(R.id.item_solicitud_imagen);
        }
    }

    public AdapterCardViewAmigo(Context c,List<SolicitudDeAmistad> items){
        this.items = items;
        this.context = c;
        Log.e("Adaptador","Entro al constructor");
    }

    @Override
    public AmigoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_amigo, parent, false);
        return new AmigoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AmigoViewHolder holder, int position) {
        Log.e("PRUEBA",items.get(position).getUsuario1());
        Log.e("Tamaño Array",items.size()+"");
        holder._imagen.setImageBitmap(items.get(position).getImagen());
        holder._nombre.setText(items.get(position).getUsuario1());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSolicitudes(List<SolicitudDeAmistad> l){
        items = new ArrayList<>(l);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        postEvent(position);
    }

    private void postEvent(Integer position){
        ResponderSolicitudDialogEvent e = new ResponderSolicitudDialogEvent();
        e.setPosition(position);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(e);
    }
}