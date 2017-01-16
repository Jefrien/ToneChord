package com.jefrienalvizures.tonechord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jefrien on 15/1/2017.
 */
public class SolicitudesFragment extends Fragment {

    public static SolicitudesFragment newInstance(){
        SolicitudesFragment sf = new SolicitudesFragment();

        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
