package com.jefrienalvizures.tonechord.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.bean.Chord;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefrien on 8/1/2017.
 */
public class AdapterCardViewChord extends RecyclerView.Adapter<AdapterCardViewChord.ChordViewHolder> {
    private List<Chord> items;
    private List<Chord> itemsCopy;

    public static class ChordViewHolder extends RecyclerView.ViewHolder {
        // Campos del item
        public TextView _titulo,_autor;

        public ChordViewHolder(View v){
            super(v);
            _titulo = (TextView) v.findViewById(R.id.cardview_chord_titulo);
            _autor = (TextView) v.findViewById(R.id.cardview_chord_autor);
        }
    }

    public AdapterCardViewChord(List<Chord> items){
        this.items = new ArrayList<>(items);
//        this.itemsCopy.addAll(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ChordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_chord_item, parent, false);
        return new ChordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChordViewHolder holder, int position) {
        holder._titulo.setText(items.get(position).getTitulo());
        holder._titulo.setSelected(true);
        holder._titulo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder._titulo.setSingleLine(true);
        holder._autor.setText(items.get(position).getAutor());
    }

    public void setChords(List<Chord> chords){
        items = new ArrayList<>(chords);
    }

    // Animaciones
    public Chord removeItem(int position){
        final Chord chord = items.remove(position);
        notifyItemRemoved(position);
        return chord;
    }

    public void addItem(int position,Chord chord){
        items.add(position, chord);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        final Chord chord = items.remove(fromPosition);
        items.add(toPosition,chord);
        notifyItemMoved(fromPosition,toPosition);
    }

    public void animateTo(List<Chord> chords){
        applyAndAnimateRemovals(chords);
        applyAndAnimateAdditions(chords);
        applyAndAnimateMovedItems(chords);
    }

    private void applyAndAnimateRemovals(List<Chord> newChords){
        for(int i = items.size() - 1; i>=0; i--){
            final Chord chord = items.get(i);
            if(!newChords.contains(chord)){
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Chord> newChords){
        for(int i = 0, count = newChords.size(); i<count; i++){
            final Chord chord = newChords.get(i);
            if(!items.contains(chord)){
                addItem(i,chord);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Chord> newChords) {
        for(int toPosition = newChords.size() - 1; toPosition >= 0; toPosition--){
            final Chord chord = newChords.get(toPosition);
            final int fromPosition = items.indexOf(chord);
            if(fromPosition >= 0 && fromPosition != toPosition){
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
