package com.dtech.digitalstore.data;

/**
 * Created by aris on 23/08/17.
 */

public class DataMenu {

    String nama, keterangan, harga, foto;

    public DataMenu() {
    }

    public String getNama() {
        return nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setNama(String namaMenu) {
        this.nama = namaMenu;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
