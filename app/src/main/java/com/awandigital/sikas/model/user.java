package com.awandigital.sikas.model;

public class user {

    int id_user;
    String nama;
    String no_hp;
    String email;
    int status_login;

    public user(int id_user, String nama, String no_hp, String email, int status_login) {
        this.id_user = id_user;
        this.nama = nama;
        this.no_hp = no_hp;
        this.email = email;
        this.status_login = status_login;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus_login() {
        return status_login;
    }

    public void setStatus_login(int status_login) {
        this.status_login = status_login;
    }

}
