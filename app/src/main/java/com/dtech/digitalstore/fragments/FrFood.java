package com.dtech.digitalstore.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.adapter.AdapterMenu;
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
    //List<FieldMenu> fieldMenus;
    //AdapterMenu mAdapter;
    RecyclerView recyclerView;
    TextView tmenus;

    public static FrFood newInstance() {
        FrFood fragment = new FrFood();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initUi();
        //initRealDbase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_food, container, false);

        /*sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));*/


        initUi();
        initRealDbase();

        return view;
    }

    private void initUi() {
        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        tmenus = (TextView) view.findViewById(R.id.menus);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclermenu);

        if (prefToko != "") {
            //initRealDbase();
        }

    }

    private void initRealDbase() {
        //database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("warung").child(prefToko).child("makanan");
        myRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child("makanan");
        final List<FieldMenu> fieldMenus = new ArrayList<>();
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fieldMenus.clear();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childs : dataSnapshot.getChildren()) {
                    DataMenu dataMenu = childs.getValue(DataMenu.class);
                    FieldMenu fieldData = new FieldMenu();
                    fieldData.nama = String.valueOf(dataMenu.getNama());
                    fieldData.keterangan = String.valueOf(dataMenu.getKeterangan());
                    fieldData.harga = String.valueOf(dataMenu.getHarga());
                    fieldData.foto = String.valueOf(dataMenu.getFoto());
                    fieldMenus.add(fieldData);

                    Log.d("datas : ", String.valueOf(dataMenu.getNama()));
                }
                AdapterMenu mAdapter = new AdapterMenu(getActivity(), fieldMenus);
                recyclerView.setAdapter(mAdapter);
                recyclerView.invalidate();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                /*String listmenus = TextUtils.join("\\n", fieldMenus);
                tmenus.setText(listmenus);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

