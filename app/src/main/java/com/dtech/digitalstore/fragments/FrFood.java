package com.dtech.digitalstore.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.data.DataMenu;
import com.dtech.digitalstore.data.FieldMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 23/08/2017.
 */

public class FrFood extends Fragment {

    View view;
    String prefToko;
    SharedPreferences sharedPreferences;
    DatabaseReference myRef;
    FirebaseDatabase database;
    List<FieldMenu> fieldMenus;

    TextView tmenus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_food, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("warung").child(prefToko).child("makanan");
        myRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child("makanan/");
        fieldMenus = new ArrayList<>();
        initRealDbase();
        initUi();
        return view;
    }

    private void initUi() {
        tmenus = (TextView) view.findViewById(R.id.menus);
    }

    private void initRealDbase() {
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fieldMenus.clear();
                for (DataSnapshot childs : dataSnapshot.getChildren()) {
                    DataMenu dataMenu = childs.getValue(DataMenu.class);
                    FieldMenu fieldData = new FieldMenu();
                    fieldData.nm_menu = String.valueOf(dataMenu.getNamaMenu());
                    fieldData.keterangan = String.valueOf(dataMenu.getKeterangan());
                    fieldData.harga = String.valueOf(dataMenu.getHarga());
                    fieldMenus.add(fieldData);
                }
                String listmenus = TextUtils.join("\\n", fieldMenus);
                tmenus.setText(listmenus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

