package com.dtech.digitalstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.config.PrefManager;
import com.dtech.digitalstore.data.DataMenu;
import com.dtech.digitalstore.data.DataPesan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    String scat, sbayar, sporsi;
    int stotal;
    TextView tdmenu, tdharga, tdtotal;
    EditText edcatatan, edbayar, edporsi;
    NumberPicker np;
    Button btproses;
    RadioGroup radioGroup;
    RadioButton rb1, rb2;

    String orderAktif;
    String nmmenu, sharga;
    String totalorder;
    DatabaseReference orderRef, totalorderRef, aktifOrderRef;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        prefManager = new PrefManager(this);
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);

        Intent confirm = getIntent();
        nmmenu = confirm.getStringExtra("nmmenu");
        sharga = confirm.getStringExtra("sharga");
        String prefMeja = (sharedPreferences.getString(Config.DECVALUE, ""));
        String preftoko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        totalorderRef = FirebaseDatabase.getInstance().getReference("warung/"+preftoko+"/"+prefMeja+"/totalorder");
        totalorderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //totalorder = (String) dataSnapshot.getValue();
                /*String stotalorder = dataSnapshot.getValue().toString();*/
                totalorder = String.valueOf(dataSnapshot.getValue());
                Log.d("Value totalorder ", String.valueOf(dataSnapshot.getValue()));
                /*if (stotalorder == "" || stotalorder == null || stotalorder.matches("")) {
                    totalorder = "0";
                } else {
                    totalorder = stotalorder;
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        orderRef= FirebaseDatabase.getInstance().getReference().child("warung").child(preftoko).child(prefMeja).child("order");
        aktifOrderRef = FirebaseDatabase.getInstance().getReference("warung/"+preftoko+"/"+prefMeja+"/aktifOrder");
        initUi();

    }

    private void initUi() {

        tdmenu = (TextView) findViewById(R.id.tdmenu);
        tdmenu.setText(nmmenu);
        tdharga = (TextView) findViewById(R.id.tdharga);
        tdharga.setText("Rp " + sharga);
        tdtotal = (TextView) findViewById(R.id.tdtotal);
        edcatatan = (EditText) findViewById(R.id.tdecatatan);
        edbayar = (EditText) findViewById(R.id.tdeuang);
        radioGroup = (RadioGroup) findViewById(R.id.rgroup);
        rb1 = (RadioButton) findViewById(R.id.rbcash);
        rb2 = (RadioButton) findViewById(R.id.rbkembalian);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbcash:
                        edbayar.setVisibility(View.GONE);
                        break;
                    case R.id.rbkembalian:
                        edbayar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        //edporsi = (EditText) dialogPesanan.findViewById(R.id.tdjml);
        btproses = (Button) findViewById(R.id.tdbtn);
        np = (NumberPicker) findViewById(R.id.tdnp);
        np.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(20);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                stotal = newVal * Integer.parseInt(sharga);
                tdtotal.setText("Rp " + stotal);
            }
        });
        btproses.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == rb1.getId()) {
            sbayar = String.valueOf(stotal);
        } else if (selectedId == rb2.getId()) {
            String sedbayar = edbayar.getText().toString();
            if (sedbayar == "") {
                Toast.makeText(ConfirmActivity.this, "Silahkan isi jumlah uang yang akan dibayar", Toast.LENGTH_SHORT);
                edbayar.requestFocus();
            } else {
                sbayar = sedbayar;
            }
        }
        pushRealDbase(nmmenu, String.valueOf(np.getValue()), edcatatan.getText().toString(), String.valueOf(stotal));
    }

    private void pushRealDbase(String namamenu, String jumlah, String keterangan, String total) {



        String key = orderRef.push().getKey();
        String keyOrder = aktifOrderRef.push().getKey();

        List<DataPesan> pesananEntries = new ArrayList<>();

        DataPesan dataPesan = new DataPesan();

        dataPesan.setNamamenu(namamenu);
        dataPesan.setJumlah(jumlah);
        dataPesan.setKeterangan(keterangan);
        dataPesan.setTotal(total);
        dataPesan.setKey(key);
        //dataPesan.setBayar(bayar);

        pesananEntries.add(dataPesan);

        for (DataPesan dataPesan1 : pesananEntries) {
            orderRef.child(key).setValue(dataPesan1);
        }

        int newTotal = Integer.parseInt(totalorder) + Integer.parseInt(total);
        totalorderRef.setValue(String.valueOf(newTotal));
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("keyvalue", key);

        //final String orderAktif;
        /*aktifOrderRef.keepSynced(true);
        aktifOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderAktif = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (orderAktif=="0") {
            aktifOrderRef.setValue(key + ",");
        } else {
            aktifOrderRef.setValue(orderAktif + key + ",");
        }*/
        aktifOrderRef.child(keyOrder).child("key").setValue(key);

        this.finish();
    }
}
