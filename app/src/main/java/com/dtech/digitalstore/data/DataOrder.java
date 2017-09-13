package com.dtech.digitalstore.data;

/**
 * Created by lenovo on 28/08/2017.
 */

public class DataOrder {
    String namamenu, jumlah, keterangan, total, key, meja;

    public DataOrder() {

    }

    public String getNamamenu() {
        return namamenu;
    }

    public void setNamamenu(String namamenu) {
        this.namamenu = namamenu;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMeja() {
        return meja;
    }

    public void setMeja(String meja) {
        this.meja = meja;
    }

    /*public String getBayar() {
        return bayar;
    }

    public void setBayar(String bayar) {
        this.bayar = bayar;
    }*/

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
