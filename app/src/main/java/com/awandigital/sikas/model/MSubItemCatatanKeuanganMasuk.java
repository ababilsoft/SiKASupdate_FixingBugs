package com.awandigital.sikas.model;

public class MSubItemCatatanKeuanganMasuk {
    String TujuanFinansial, Kategori, Tgl;
    int Nominal;

    public MSubItemCatatanKeuanganMasuk(String tujuanFinansial, String kategori, int nominal) {
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

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public int getNominal() {
        return Nominal;
    }

    public void setNominal(int nominal) {
        Nominal = nominal;
    }
}
