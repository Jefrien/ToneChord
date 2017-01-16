package com.jefrienalvizures.tonechord.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jefrien on 14/1/2017.
 */
public class AmigosFragment extends Fragment {

    public static AmigosFragment newInstance(){
        AmigosFragment af = new AmigosFragment();

        return af;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(0,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
