package com.dtech.digitalstore.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aris on 23/08/17.
 */

public class PrefManager {

    SharedPreferences pref;
    public SharedPreferences.Editor editor;

    Context _context;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPrefToko(String namaToko) {
        editor.putString(Config.NAMA_TOKO, namaToko);
        editor.commit();
    }

    public void setPrefMeja(String nomorMeja) {
        editor.putString(Config.NOMOR_MEJA, nomorMeja);
        editor.commit();
    }

    public void setPrefDbaseMeja(String encMeja) {
        editor.putString(Config.ENC_NOMOR_MEJA, encMeja);
        editor.commit();
    }

    public void setPrefDbaseToko(String encToko) {
        editor.putString(Config.ENC_NAMA_TOKO, encToko);
        editor.commit();
    }
}
