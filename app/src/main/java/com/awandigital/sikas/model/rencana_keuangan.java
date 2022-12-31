package com.awandigital.sikas.model;

public class rencana_keuangan {
    String id_renkeu;
    String tujuan;
    String kategori;
    String tanggal;
    String metodeTransaksi;
    String catatan;
    String danaTerkumpul;
    String targetDana;

    public rencana_keuangan(String id_renkeu, String tujuan, String danaTerkumpul, String targetDana) {
        this.id_renkeu = id_renkeu;
        this.tujuan = tujuan;
        this.danaTerkumpul = danaTerkumpul;
        this.targetDana = targetDana;
    }

    public String getTargetDana() {
        return targetDana;
    }

    public void setTargetDana(String targetDana) {
        this.targetDana = targetDana;
    }

    public String getId_renkeu() {
        return id_renkeu;
    }

    public void setId_renkeu(String id_renkeu) {
        this.id_renkeu = id_renkeu;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getMetodeTransaksi() {
        return metodeTransaksi;
    }

    public void setMetodeTransaksi(String metodeTransaksi) {
        this.metodeTransaksi = metodeTransaksi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getDanaTerkumpul() {
        return danaTerkumpul;
    }

    public void setDanaTerkumpul(String danaTerkumpul) {
        this.danaTerkumpul = danaTerkumpul;
    }
}

