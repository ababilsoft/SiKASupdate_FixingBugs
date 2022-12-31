package com.awandigital.sikas.model;

public class MPengeluaran {
    String id_pengeluaran;
    String nominal;
    String diambilDari;
    String kategori;
    String tglTransaksi;
    String metodeTransaksi;
    String hari;
    String bulan;
    String tahun;
    String catatan;
    String created_at;

    public MPengeluaran(String nominal, String diambilDari, String kategori, String tglTransaksi) {
        this.nominal = nominal;
        this.diambilDari = diambilDari;
        this.kategori = kategori;
        this.tglTransaksi = tglTransaksi;
    }

    public String getId_pengeluaran() {
        return id_pengeluaran;
    }

    public void setId_pengeluaran(String id_pengeluaran) {
        this.id_pengeluaran = id_pengeluaran;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getDiambilDari() {
        return diambilDari;
    }

    public void setDiambilDari(String diambilDari) {
        this.diambilDari = diambilDari;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public String getMetodeTransaksi() {
        return metodeTransaksi;
    }

    public void setMetodeTransaksi(String metodeTransaksi) {
        this.metodeTransaksi = metodeTransaksi;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
