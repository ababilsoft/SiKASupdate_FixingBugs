package com.awandigital.sikas.model;

public class MKontakTerbaru {
    String Id;
    String NamaKontak;

    public MKontakTerbaru(String id, String namaKontak) {
        Id = id;
        NamaKontak = namaKontak;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNamaKontak() {
        return NamaKontak;
    }

    public void setNamaKontak(String namaKontak) {
        NamaKontak = namaKontak;
    }
}
