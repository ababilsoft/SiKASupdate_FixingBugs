package com.awandigital.sikas.model;

public class MDetailDataRencanaKeuangan {
    String Id, TglTransaksi, JenisTransaksi,Kategori,Catatan, nominal;

    public MDetailDataRencanaKeuangan(String id, String tglTransaksi, String jenisTransaksi, String kategori, String catatan, String nominal) {
        Id = id;
        TglTransaksi = tglTransaksi;
        JenisTransaksi = jenisTransaksi;
        Kategori = kategori;
        Catatan = catatan;
        this.nominal = nominal;
    }

    public String getTglTransaksi() {
        return TglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        TglTransaksi = tglTransaksi;
    }

    public String getJenisTransaksi() {
        return JenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        JenisTransaksi = jenisTransaksi;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getCatatan() {
        return Catatan;
    }

    public void setCatatan(String catatan) {
        Catatan = catatan;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
