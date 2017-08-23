package com.dtech.digitalstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.config.PrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;

    String prefMeja, prefToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);

        //initComponent();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("");
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void initComponent() {
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        prefMeja = (sharedPreferences.getString(Config.NOMOR_MEJA, ""));
        t1 = (TextView) findViewById(R.id.t1);

        t1.setText("Selamat Datang di "+prefToko+"\nAnda berada di : "+prefMeja);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                prosesQr(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void prosesQr(String qrmeja) {
        byte[] decoded = Base64.decode(qrmeja, Base64.DEFAULT);
        String decvalue = new String(decoded);
        String[] qrarray = decvalue.split(",");
        String namaToko = qrarray[1];
        String nomorMeja = qrarray[0];
        String qrtoko = namaToko + "," + qrarray[2];
        String realDbaseToko = "";
        byte[] data = new byte[0];
        try {
            data = qrtoko.getBytes("UTF-8");
            realDbaseToko = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        prefManager.setPrefDbaseToko(realDbaseToko);
        prefManager.setPrefDbaseMeja(qrmeja);
        prefManager.setPrefToko(namaToko);
        prefManager.setPrefMeja(nomorMeja);
        //t1.setText(result.getContents());
        initComponent();
        //t1.setText("Selamat Datang di "+namaToko+" ("+realDbaseToko+")"+"\nAnda berada di : "+nomorMeja);
    }
}
