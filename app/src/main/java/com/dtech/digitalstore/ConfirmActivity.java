package com.dtech.digitalstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    String scat, sbayar, sporsi;
    int stotal;
    TextView tdmenu, tdharga, tdtotal;
    EditText edcatatan, edbayar, edporsi;
    NumberPicker np;
    Button btproses;
    RadioGroup radioGroup;
    RadioButton rb1, rb2;

    String nmmenu, sharga;
    DatabaseReference orderRef;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        prefManager = new PrefManager(this);

        Intent confirm = getIntent();
        nmmenu = confirm.getStringExtra("nmmenu");
        sharga = confirm.getStringExtra("sharga");
        initUi();

    }

    private void initUi() {
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
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
        pushRealDbase(nmmenu, String.valueOf(np.getValue()), edcatatan.getText().toString(), String.valueOf(stotal), sbayar);
    }

    private void pushRealDbase(String namamenu, String jumlah, String keterangan, String total, String bayar ) {
        String prefMeja = (sharedPreferences.getString(Config.DECVALUE, ""));
        String preftoko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        orderRef= FirebaseDatabase.getInstance().getReference().child("warung").child(preftoko).child(prefMeja).child("order");

        List<DataPesan> pesananEntries = new ArrayList<>();

        DataPesan dataPesan = new DataPesan();

        dataPesan.setNamamenu(namamenu);
        dataPesan.setJumlah(jumlah);
        dataPesan.setKeterangan(keterangan);
        dataPesan.setTotal(total);
        dataPesan.setBayar(bayar);

        pesananEntries.add(dataPesan);

        for (DataPesan dataPesan1 : pesananEntries) {
            String key = orderRef.push().getKey();
            orderRef.child(key).setValue(dataPesan1);
        }

        this.finish();
    }
}
