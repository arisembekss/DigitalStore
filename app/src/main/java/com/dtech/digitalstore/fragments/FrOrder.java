package com.dtech.digitalstore.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView totalOrder;
    Button btnOrder;
    String prefToko, prefMeja;
    SharedPreferences sharedPreferences;
    DatabaseReference myRef, totalRef, viewRef, pembayaranRef, statusRef, aktifOrderRef;
    View view;
    RelativeLayout rel1, rel2;

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
        /*statusRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("statuspesan");
        statusRef.keepSynced(true);
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String statusOrder = String.valueOf(dataSnapshot.getValue());
                if (statusOrder.matches("1")) {
                    rel2.setVisibility(View.VISIBLE);
                    rel1.setVisibility(View.GONE);
                } else {
                    rel1.setVisibility(View.VISIBLE);
                    rel2.setVisibility(View.GONE);
                    initRealdbase();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        initRealdbase();

        return view;
    }

    private void initUi() {
        sharedPreferences = getActivity().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerorder);
        totalOrder = (TextView) view.findViewById(R.id.orderTotal);
        btnOrder = (Button) view.findViewById(R.id.btnOrder);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        prefMeja = (sharedPreferences.getString(Config.DECVALUE, ""));
        viewRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("view");
        pembayaranRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("pembayaran");
        aktifOrderRef = FirebaseDatabase.getInstance().getReference("warung/"+prefToko+"/"+prefMeja+"/aktifOrder");
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewRef.setValue("1");
                String tOrder = totalOrder.getText().toString();
                openDialog(tOrder);
            }
        });
        rel1 = (RelativeLayout) view.findViewById(R.id.rel1);
        rel2 = (RelativeLayout) view.findViewById(R.id.rel2);
    }

    private void openDialog(final String orderTotal) {
        final Dialog dialogBuy = new Dialog(getActivity());
        dialogBuy.setContentView(R.layout.custom_dialog_order_menu);
        dialogBuy.setTitle("Konfirmasi Pembayaran");

        final RadioGroup rgroup = (RadioGroup) dialogBuy.findViewById(R.id.rgroup);
        RadioButton rb1 = (RadioButton) dialogBuy.findViewById(R.id.rbcash);
        RadioButton rb2 = (RadioButton) dialogBuy.findViewById(R.id.rbkembalian);
        final EditText duang = (EditText) dialogBuy.findViewById(R.id.deuang);
        final Button btn = (Button) dialogBuy.findViewById(R.id.dbtn);

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbcash:
                        duang.setVisibility(View.GONE);
                        break;
                    case R.id.rbkembalian:
                        duang.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedid = rgroup.getCheckedRadioButtonId();
                if (selectedid == R.id.rbcash) {
                    pembayaranRef.setValue(orderTotal);
                    viewRef.setValue("1");
                    //initRealdbase();
                    aktifOrderRef.removeValue();
                    btnOrder.setVisibility(View.INVISIBLE);
                    dialogBuy.dismiss();
                } else {
                    String totBayar = duang.getText().toString();
                    if (totBayar.matches("")) {
                        Toast.makeText(getActivity(), "SIlahkan input jumlah uang yang ingin di bayar", Toast.LENGTH_LONG).show();
                        duang.requestFocus();
                    } else {
                        pembayaranRef.setValue(totBayar);
                        viewRef.setValue("1");
                        //initRealdbase();
                        aktifOrderRef.removeValue();
                        btnOrder.setVisibility(View.INVISIBLE);
                        dialogBuy.dismiss();
                    }
                }
                //statusRef.setValue("1");
            }
        });

        dialogBuy.show();
    }

    private void initRealdbase() {

        /*if (status == 0) {

        } else {

        }*/
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

        totalRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("totalorder");
        totalRef.keepSynced(true);
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalOrder.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void prosesdbase() {

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

        totalRef = FirebaseDatabase.getInstance().getReference().child("warung").child(prefToko).child(prefMeja).child("totalorder");
        totalRef.keepSynced(true);
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalOrder.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

