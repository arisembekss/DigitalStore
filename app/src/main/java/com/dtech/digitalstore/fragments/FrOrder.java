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

public class FrOrder extends Fragment {

    View view;

    public static FrOrder newInstance() {
        FrOrder fragment = new FrOrder();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_order, container, false);

        return view;
    }
}

