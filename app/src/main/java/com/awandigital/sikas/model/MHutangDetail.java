package com.awandigital.sikas.model;

public class MHutangDetail {
    String id_hutang_detail;
    String id_hp;
    String tgl_transaksi;
    String nominal;
    String sisa_bayar;
    String jml_cicil;
    String catatan;
    String status_transaksi;

    public MHutangDetail(String id_hutang_detail, String id_hp, String tgl_transaksi, String nominal, String sisa_bayar, String jml_cicil, String catatan, String status_transaksi) {
        this.id_hutang_detail = id_hutang_detail;
        this.id_hp = id_hp;
        this.tgl_transaksi = tgl_transaksi;
        this.nominal = nominal;
        this.sisa_bayar = sisa_bayar;
        this.jml_cicil = jml_cicil;
        this.catatan = catatan;
        this.status_transaksi = status_transaksi;
    }

    public String getSisa_bayar() {
        return sisa_bayar;
    }

    public void setSisa_bayar(String sisa_bayar) {
        this.sisa_bayar = sisa_bayar;
    }

    public String getJml_cicil() {
        return jml_cicil;
    }

    public void setJml_cicil(String jml_cicil) {
        this.jml_cicil = jml_cicil;
    }

    public String getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(String status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getId_hutang_detail() {
        return id_hutang_detail;
    }

    public void setId_hutang_detail(String id_hutang_detail) {
        this.id_hutang_detail = id_hutang_detail;
    }

    public String getId_hp() {
        return id_hp;
    }

    public void setId_hp(String id_hp) {
        this.id_hp = id_hp;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
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
