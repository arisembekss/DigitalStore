package com.dtech.digitalstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.config.PrefManager;
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
        pushRealDbase();
    }

    private void pushRealDbase() {
        String prefMeja = (sharedPreferences.getString(Config.NOMOR_MEJA, ""));
        orderRef= FirebaseDatabase.getInstance().getReference().child("warung").child(prefMeja).child("order");

        List<DataPesan> pesananEntries = new ArrayList<>();

        DataPesan dataPesan = new DataPesan();

    }
}
