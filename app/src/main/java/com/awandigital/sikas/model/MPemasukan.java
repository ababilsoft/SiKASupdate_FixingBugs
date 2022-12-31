package com.awandigital.sikas.model;

public class MPemasukan {
    String TglTransaksi, TotalHarian, TujuanFinansial, Kategori, Catatan, Nominal;

    public MPemasukan(String tglTransaksi, String tujuanFinansial, String kategori, String nominal) {
        TglTransaksi = tglTransaksi;
        TujuanFinansial = tujuanFinansial;
        Kategori = kategori;
        Nominal = nominal;
    }

    public String getTujuanFinansial() {
        return TujuanFinansial;
    }

    public void setTujuanFinansial(String tujuanFinansial) {
        TujuanFinansial = tujuanFinansial;
    }

    public String getTglTransaksi() {
        return TglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        TglTransaksi = tglTransaksi;
    }

    public String getTotalHarian() {
        return TotalHarian;
    }

    public void setTotalHarian(String totalHarian) {
        TotalHarian = totalHarian;
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
        return Nominal;
    }

    public void setNominal(String nominal) {
        Nominal = nominal;
    }
}
