package com.jefrienalvizures.tonechord.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jefrienalvizures.tonechord.R;
import com.jefrienalvizures.tonechord.events.FragmentEventChanged;
import com.jefrienalvizures.tonechord.lib.Comunicator;
import com.jefrienalvizures.tonechord.lib.Dialogos;
import com.jefrienalvizures.tonechord.lib.EventBus;
import com.jefrienalvizures.tonechord.lib.GreenRobotEventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jefrien on 27/08/16.
 */
public class NuevaLetraFragment extends Fragment {
    Context c;
    //TextView tv = new TextView(c);
    @Bind(R.id.editor)
    EditText editor;
    @Bind(R.id.textoTemp) TextView temp;
    public NuevaLetraFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nueva_letra_fragment,container,false);
        ButterKnife.bind(this,view);
       /* WebView wv = (WebView) view.findViewById(R.id.webViewxD);
        wv.loadUrl("file:///android_asset/index.html");*/
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nueva_letra_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pegar:
                pegar();
                break;
            case R.id.menu_ayuda:
                Dialogos.ok(
                        getActivity(),
                        getString(R.string.ayuda_nueva_letra),
                        getString(R.string.ayuda_nueva_letra_txt)
                );
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_siguiente_editar_chord)
    public void siguiente(){
        if(editor.getText().toString().isEmpty()){
            editor.setError(getString(R.string.vacioError));
        } else {
            Comunicator.setLetra(editor.getText().toString());
            postEvent(433537801);
        }
    }



    public void pegar(){
        try {
            ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(getContext().CLIPBOARD_SERVICE);

            ClipData abc = myClipboard.getPrimaryClip();
            Log.e("COUNT", abc.getItemCount() + "");
            if (abc.getItemCount() == 1) {
                ClipData.Item item = abc.getItemAt(0);
                temp.setText(item.getText().toString());
                //tv.setText(text);
                editor.setText(temp.getText());
            } else {
                Toast.makeText(getContext(), getString(R.string.copiaAlgoPrimero), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Log.e("NuevaLetraFragment","Excepcion: "+e.getStackTrace());
        }

    }


    private void postEvent(Integer id){
        FragmentEventChanged fce = new FragmentEventChanged();
        fce.setId(id);
        EventBus event = GreenRobotEventBus.getInstance();
        event.post(fce);
    }

}
