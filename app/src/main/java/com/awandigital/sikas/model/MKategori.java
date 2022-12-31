package com.awandigital.sikas.model;

public class MKategori {
    int id_kategori;
    String kategori;
    String created_at;

    public MKategori(int id_kategori, String kategori, String created_at) {
        this.id_kategori = id_kategori;
        this.kategori = kategori;
        this.created_at = created_at;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
