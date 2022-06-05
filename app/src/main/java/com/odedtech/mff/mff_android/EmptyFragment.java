package com.odedtech.mff.mff_android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.odedtech.mff.mffapp.R;

import base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends BaseFragment {


    public EmptyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

}
