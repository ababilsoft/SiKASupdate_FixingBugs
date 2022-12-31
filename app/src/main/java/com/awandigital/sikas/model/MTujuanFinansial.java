package com.awandigital.sikas.model;

public class MTujuanFinansial {
    String id;
    String nama_rencana_keuangan;
    String target_dana;
    String dana_terkumpul;
    String created_at;

    public MTujuanFinansial(String id, String nama_rencana_keuangan, String target_dana, String dana_terkumpul, String created_at) {
        this.id = id;
        this.nama_rencana_keuangan = nama_rencana_keuangan;
        this.target_dana = target_dana;
        this.dana_terkumpul = dana_terkumpul;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_rencana_keuangan() {
        return nama_rencana_keuangan;
    }

    public void setNama_rencana_keuangan(String nama_rencana_keuangan) {
        this.nama_rencana_keuangan = nama_rencana_keuangan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDana_terkumpul() {
        return dana_terkumpul;
    }

    public void setDana_terkumpul(String dana_terkumpul) {
        this.dana_terkumpul = dana_terkumpul;
    }

    public String getTarget_dana() {
        return target_dana;
    }

    public void setTarget_dana(String target_dana) {
        this.target_dana = target_dana;
    }
}
