package com.dtech.digitalstore.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtech.digitalstore.R;

/**
 * Created by lenovo on 23/08/2017.
 */

public class FrBeverage extends Fragment {

    View view;

    public static FrBeverage newInstance() {
        FrBeverage fragment = new FrBeverage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_bev, container, false);

        return view;
    }
}

