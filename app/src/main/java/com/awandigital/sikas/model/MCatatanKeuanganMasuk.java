package com.awandigital.sikas.model;

public class MCatatanKeuanganMasuk {
    String TglTransaksi, TujuanFinansial, Kategori, Catatan, Nominal, JenisTransaksi;

    public MCatatanKeuanganMasuk(String tglTransaksi, String tujuanFinansial,
                                 String kategori, String catatan, String nominal, String jenisTransaksi) {
        TglTransaksi = tglTransaksi;
        TujuanFinansial = tujuanFinansial;
        Kategori = kategori;
        Catatan = catatan;
        Nominal = nominal;
        JenisTransaksi = jenisTransaksi;
    }

    public String getTglTransaksi() {
        return TglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        TglTransaksi = tglTransaksi;
    }

    public String getTujuanFinansial() {
        return TujuanFinansial;
    }

    public void setTujuanFinansial(String tujuanFinansial) {
        TujuanFinansial = tujuanFinansial;
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

    public String getJenisTransaksi() {
        return JenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        JenisTransaksi = jenisTransaksi;
    }
}
