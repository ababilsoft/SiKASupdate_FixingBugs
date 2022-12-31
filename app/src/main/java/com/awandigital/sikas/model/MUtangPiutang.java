package com.awandigital.sikas.model;

public class MUtangPiutang {
    String id_hp;
    String dipinjamkan_ke;
    String dipinjamkan_oleh;
    String nominal;
    String tgl_transaksi;
    String tgl_jatuhtempo;
    String status;
    String created_at;
    String sisa_bayar;
    String jenis_transaksi;

    public MUtangPiutang(String id_hp, String dipinjamkan_ke, String dipinjamkan_oleh, String nominal, String tgl_transaksi, String status, String jenis_transaksi) {
        this.id_hp = id_hp;
        this.dipinjamkan_ke = dipinjamkan_ke;
        this.dipinjamkan_oleh = dipinjamkan_oleh;
        this.nominal = nominal;
        this.tgl_transaksi = tgl_transaksi;
        this.status = status;
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getJenis_transaksi() {
        return jenis_transaksi;
    }

    public void setJenis_transaksi(String jenis_transaksi) {
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getId_hp() {
        return id_hp;
    }

    public void setId_hp(String id_hp) {
        this.id_hp = id_hp;
    }

    public String getDipinjamkan_ke() {
        return dipinjamkan_ke;
    }

    public void setDipinjamkan_ke(String dipinjamkan_ke) {
        this.dipinjamkan_ke = dipinjamkan_ke;
    }

    public String getDipinjamkan_oleh() {
        return dipinjamkan_oleh;
    }

    public void setDipinjamkan_oleh(String dipinjamkan_oleh) {
        this.dipinjamkan_oleh = dipinjamkan_oleh;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }

    public String getSisa_bayar() {
        return sisa_bayar;
    }

    public void setSisa_bayar(String sisa_bayar) {
        this.sisa_bayar = sisa_bayar;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTgl_jatuhtempo() {
        return tgl_jatuhtempo;
    }

    public void setTgl_jatuhtempo(String tgl_jatuhtempo) {
        this.tgl_jatuhtempo = tgl_jatuhtempo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
