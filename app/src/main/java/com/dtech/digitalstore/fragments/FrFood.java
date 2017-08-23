package com.dtech.digitalstore.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.config.Config;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by lenovo on 23/08/2017.
 */

public class FrFood extends Fragment {

    View view;
    String prefToko;
    SharedPreferences sharedPreferences;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_food, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.ENC_NAMA_TOKO, ""));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(prefToko + "/makanan");
        return view;
    }
}

