package com.awandigital.sikas.model;

public class piutang_detail {
    int id_piutang_detail;
    int id_piutang;
    String tanggal;
    String nominal;
    String catatan;

    public piutang_detail(int id_piutang_detail, int id_piutang, String tanggal, String nominal, String catatan) {
        this.id_piutang_detail = id_piutang_detail;
        this.id_piutang = id_piutang;
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.catatan = catatan;
    }

    public int getId_piutang_detail() {
        return id_piutang_detail;
    }

    public void setId_piutang_detail(int id_piutang_detail) {
        this.id_piutang_detail = id_piutang_detail;
    }

    public int getId_piutang() {
        return id_piutang;
    }

    public void setId_piutang(int id_piutang) {
        this.id_piutang = id_piutang;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
