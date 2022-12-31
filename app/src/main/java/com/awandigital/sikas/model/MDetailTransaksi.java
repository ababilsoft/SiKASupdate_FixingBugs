package com.awandigital.sikas.model;

public class MDetailTransaksi {
    String StatusPelunasan, JenisTransaksi, Tgl, Kategori, Catatan1, Catatan2, JmlCost;

    public MDetailTransaksi(String statusPelunasan, String jenisTransaksi, String tgl, String kategori, String catatan1, String catatan2, String jmlCost) {
        StatusPelunasan = statusPelunasan;
        JenisTransaksi = jenisTransaksi;
        Tgl = tgl;
        Kategori = kategori;
        Catatan1 = catatan1;
        Catatan2 = catatan2;
        JmlCost = jmlCost;
    }

    public String getStatusPelunasan() {
        return StatusPelunasan;
    }

    public void setStatusPelunasan(String statusPelunasan) {
        StatusPelunasan = statusPelunasan;
    }

    public String getJenisTransaksi() {
        return JenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        JenisTransaksi = jenisTransaksi;
    }

    public String getTgl() {
        return Tgl;
    }

    public void setTgl(String tgl) {
        Tgl = tgl;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getCatatan1() {
        return Catatan1;
    }

    public void setCatatan1(String catatan1) {
        Catatan1 = catatan1;
    }

    public String getCatatan2() {
        return Catatan2;
    }

    public void setCatatan2(String catatan2) {
        Catatan2 = catatan2;
    }

    public String getJmlCost() {
        return JmlCost;
    }

    public void setJmlCost(String jmlCost) {
        JmlCost = jmlCost;
    }
}
