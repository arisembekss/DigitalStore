package com.dtech.digitalstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.dtech.digitalstore.config.Config;
import com.dtech.digitalstore.config.PrefManager;
import com.dtech.digitalstore.fragments.FrBeverage;
import com.dtech.digitalstore.fragments.FrDummy;
import com.dtech.digitalstore.fragments.FrFood;
import com.dtech.digitalstore.fragments.FrOrder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    BottomBar bottomBar;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Fragment fragment;
    private boolean change_fragment=false;
    String prefMeja, prefToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);


        /*fragment = new FrFood();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frfood, fragment);
                    fragmentTransaction.commit();
*/
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frfood, FrDummy.newInstance());
            transaction.commit();
        }
        //initComponent();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("");
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

        initComponent();
    }

    private void initComponent() {
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefToko = (sharedPreferences.getString(Config.NAMA_TOKO, ""));
        prefMeja = (sharedPreferences.getString(Config.NOMOR_MEJA, ""));

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                //fragmentManager = getSupportFragmentManager();
                Fragment sfragment=null;
                /*fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();*/
                if (tabId == R.id.tab_food) {
                    sfragment = FrFood.newInstance();
                    /*fragmentTransaction.replace(R.id.frfood, fragment);
                    fragmentTransaction.commit();*/
                } else if (tabId == R.id.tab_beverage) {
                    sfragment = FrBeverage.newInstance();
                    /*fragmentTransaction.replace(R.id.frfood, fragment);
                    fragmentTransaction.commit();*/
                } else if (tabId == R.id.tab_order) {
                    sfragment = FrOrder.newInstance();
                    /*fragmentTransaction.replace(R.id.frfood, fragment);
                    fragmentTransaction.commit();*/
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frfood, sfragment);
                transaction.commit();
                /*fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();*/
                /*fragmentTransaction.replace(R.id.frfood, fragment);
                fragmentTransaction.commitAllowingStateLoss();*/

            }
        });
        t1 = (TextView) findViewById(R.id.t1);

        t1.setText("Selamat Datang di "+prefToko+"\nAnda berada di : "+prefMeja);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        change_fragment = true;
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
        if (decvalue.contains("meja")) {
            String[] qrarray = decvalue.split(",");
            String namaToko = qrarray[1];
            String nomorMeja = qrarray[0];
            String qrtoko = namaToko + "," + qrarray[2];
            String realDbaseToko = "";
            byte[] data;
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
            //initComponent();

        } else {
            AlertDialog alertDialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Digistore");
            builder.setMessage("QR Code yang anda scan bukan untuk Digital Store");
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Intent backtomain = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(backtomain);
                }
            });
        }

        //t1.setText("Selamat Datang di "+namaToko+" ("+realDbaseToko+")"+"\nAnda berada di : "+nomorMeja);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (change_fragment) {
            change_fragment = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frfood, FrFood.newInstance());
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
