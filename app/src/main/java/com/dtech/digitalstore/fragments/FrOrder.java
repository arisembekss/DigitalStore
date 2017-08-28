package com.dtech.digitalstore.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtech.digitalstore.R;
import com.dtech.digitalstore.adapter.AdapterOrder;
import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.data.DataPesan;
import com.dtech.digitalstore.data.FieldPesan;
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

public class FrOrder extends Fragment {

    RecyclerView recyclerView;
    String prefToko, prefMeja;
    SharedPreferences sharedPreferences;
    DatabaseReference myRef;
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

        initUi();
        initRealdbase();

        return view;
    }

    private void initUi() {
        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerorder);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        prefMeja = (sharedPreferences.getString(Config.DECVALUE, ""));
    }

    private void initRealdbase() {
        myRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("order");
        final List<FieldPesan> fieldPesen = new ArrayList<>();
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fieldPesen.clear();
                Log.d("count : ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childs : dataSnapshot.getChildren()) {
                    DataPesan dataPesan = childs.getValue(DataPesan.class);
                    FieldPesan fieldData = new FieldPesan();
                    fieldData.namamenu = String.valueOf(dataPesan.getNamamenu());
                    fieldData.jumlah = String.valueOf(dataPesan.getJumlah());
                    fieldData.keterangan = String.valueOf(dataPesan.getKeterangan());
                    fieldData.key = String.valueOf(dataPesan.getKey());
                    fieldPesen.add(fieldData);
                }

                AdapterOrder adapter = new AdapterOrder(getActivity(), fieldPesen);
                recyclerView.setAdapter(adapter);
                recyclerView.invalidate();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

