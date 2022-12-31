package com.awandigital.sikas.model;

public class MCatatanKeuanganSemua {
    String IdCatatanKeuangan, TglTransaksi, Kategori, Catatan,
            Nominal, JenisTransaksi, TujuanTabungan, DiambilDari;

    public MCatatanKeuanganSemua(String idCatatanKeuangan, String tglTransaksi, String kategori, String catatan, String nominal, String jenisTransaksi, String tujuanTabungan, String diambilDari) {
        IdCatatanKeuangan = idCatatanKeuangan;
        TglTransaksi = tglTransaksi;
        Kategori = kategori;
        Catatan = catatan;
        Nominal = nominal;
        JenisTransaksi = jenisTransaksi;
        TujuanTabungan = tujuanTabungan;
        DiambilDari = diambilDari;
    }

    public String getIdCatatanKeuangan() {
        return IdCatatanKeuangan;
    }

    public void setIdCatatanKeuangan(String idCatatanKeuangan) {
        IdCatatanKeuangan = idCatatanKeuangan;
    }

    public String getTujuanTabungan() {
        return TujuanTabungan;
    }

    public void setTujuanTabungan(String tujuanTabungan) {
        TujuanTabungan = tujuanTabungan;
    }

    public String getDiambilDari() {
        return DiambilDari;
    }

    public void setDiambilDari(String diambilDari) {
        DiambilDari = diambilDari;
    }

    public String getJenisTransaksi() {
        return JenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        JenisTransaksi = jenisTransaksi;
    }

    public String getTglTransaksi() {
        return TglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        TglTransaksi = tglTransaksi;
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
