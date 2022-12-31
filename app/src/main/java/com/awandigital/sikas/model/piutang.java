package com.awandigital.sikas.model;

public class piutang {
    int id_piutang;
    String nama_penghutang;
    String nominal;
    String tgl_hutang;
    String tgl_jatuhtempo;
    String status;
    String created_at;

    public piutang(int id_piutang, String nama_penghutang, String nominal, String tgl_hutang, String tgl_jatuhtempo, String status, String created_at) {
        this.id_piutang = id_piutang;
        this.nama_penghutang = nama_penghutang;
        this.nominal = nominal;
        this.tgl_hutang = tgl_hutang;
        this.tgl_jatuhtempo = tgl_jatuhtempo;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId_piutang() {
        return id_piutang;
    }

    public void setId_piutang(int id_piutang) {
        this.id_piutang = id_piutang;
    }

    public String getNama_penghutang() {
        return nama_penghutang;
    }

    public void setNama_penghutang(String nama_penghutang) {
        this.nama_penghutang = nama_penghutang;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTgl_hutang() {
        return tgl_hutang;
    }

    public void setTgl_hutang(String tgl_hutang) {
        this.tgl_hutang = tgl_hutang;
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
