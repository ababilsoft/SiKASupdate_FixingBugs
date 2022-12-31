package com.awandigital.sikas.model;

public class MCatatanKeuanganKeluar {
    String TglTransaksi, DiambilDari, Kategori, Catatan, Nominal, JenisTransaksi;

    public MCatatanKeuanganKeluar(String tglTransaksi, String diambilDari,
                                  String kategori, String catatan, String nominal, String jenisTransaksi) {
        TglTransaksi = tglTransaksi;
        DiambilDari = diambilDari;
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

    public String getDiambilDari() {
        return DiambilDari;
    }

    public void setDiambilDari(String diambilDari) {
        DiambilDari = diambilDari;
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
