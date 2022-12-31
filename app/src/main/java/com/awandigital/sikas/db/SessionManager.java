package com.awandigital.sikas.db;


import static com.awandigital.sikas.db.DatabaseHelper.email;
import static com.awandigital.sikas.db.DatabaseHelper.nama;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.awamdigital.sikas";

    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String REGISTRASI_STATUS = "RegStatus";

    private static final String LOGIN_TOKO = "LoginOutlet";

    public static final String FIRSTLOOK = "firstLook";

    public static final String KEY_NAMA = "name";

    public static final String NAMATOKO = "nama_toko";

    public static final String EMAIL_TOKO = "EMAIL_TOKO";

    public static final String KODE_OWNERUTAMA = "owner_utama";

    public static final String KODE_USERS = "kode_user";

    public static final String NamaMenuActive = "menuActive";

    public static final String HAK_AKSES = "hak_akses";

    public static final String BUKA_KASIR = "status_kasir";

    public static final String KODE_REKAPKAS = "kode_rekapkas";

    public static final String KODE_IDHP = "id_hp";


//----


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String setEmail, String setNama) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(email, setEmail);

        editor.putString(nama, setNama);

        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status

        boolean stLogin = true;

        if (!this.isLoggedIn()) {

            stLogin = false;
        }

        return stLogin;

    }

    public void setLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public void setLoginOutlet() {
        editor.putBoolean(LOGIN_TOKO, true);
        editor.commit();
    }

    public void setFirstlook() {

        editor.putBoolean(FIRSTLOOK, true);
        editor.commit();
    }

    public void setRegistrasi(String val) {
        editor.putString(REGISTRASI_STATUS, val);
        editor.commit();
    }


    public boolean checkFirstLook() {
        // Check login status

        boolean stLook = true;

        if (!this.isFirstLook()) {

            stLook = false;
        }

        return stLook;

    }


    public void logoutUser() {
        // Clearing all data from Shared Preferences
        boolean look = false;
        if (this.checkFirstLook()) {
            look = true;
        }

        String emailToko = this.getEmailToko();
        String regStatus = this.getRegistrasi();

        editor.clear();
        editor.commit();

        this.setEmailToko(emailToko);
        this.setRegistrasi(regStatus);

        if (look) {
            this.setFirstlook();
        }

    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, true);
    }

    public void isLoggedOut() {
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }

    public boolean isLoginOutlet() {
        return pref.getBoolean(LOGIN_TOKO, true);
    }

    public boolean isFirstLook() {
        return pref.getBoolean(FIRSTLOOK, false);
    }

    public String getNama() {
        return pref.getString(nama, null);
    }

    public String getNamatoko() {
        return pref.getString(NAMATOKO, null);
    }

    public String getNamaMenuActive() {
        return pref.getString(NamaMenuActive, null);
    }

    public String getEmailToko() {
        return pref.getString(EMAIL_TOKO, null);
    }

    public String getKodeOwnerutama() {
        return pref.getString(KODE_OWNERUTAMA, null);
    }

    public String getKodeUsers() {
        return pref.getString(KODE_USERS, null);
    }

    public String getBukaKasir() {
        return pref.getString(BUKA_KASIR, null);
    }

    public String getKodeIdhp() {
        return pref.getString(KODE_IDHP, null);
    }

    public void setKodeIdhp(String val) {
        editor.putString(KODE_IDHP, val);
        editor.commit();
    }

    public void setKodeOwnerutama(String val) {
        editor.putString(KODE_OWNERUTAMA, val);
        editor.commit();
    }


    public void setKodeUsers(String val) {
        editor.putString(KODE_USERS, val);
        editor.commit();
    }

    public void setNamaMenuActive(String val) {
        editor.putString(NamaMenuActive, val);
        editor.commit();
    }

    public void setNama(String nama) {
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

    public void setNamatoko(String namatoko) {
        editor.putString(NAMATOKO, namatoko);
        editor.commit();
    }

    public void setEmailToko(String value) {
        editor.putString(EMAIL_TOKO, value);
        editor.commit();
    }

    public void setBukaKasir(String bukaKasir) {
        editor.putString(BUKA_KASIR, bukaKasir);
        editor.commit();
    }


    public void setPenerimaanAktual(String val, String val2) {
        editor.putString("tunai", val);
        editor.putString("non_tunai", val2);
        editor.commit();
    }

    public String getKodeRekapkas() {
        return pref.getString(KODE_REKAPKAS, null);
    }

    public void setKodeRekapkas(String val) {
        editor.putString(KODE_REKAPKAS, val);
        editor.commit();
    }


    public void logOuts() {
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }

    public void setHakAkses(String val) {
        editor.putString(HAK_AKSES, val);
        editor.commit();
    }


    public String getHakAkses() {
        return pref.getString(HAK_AKSES, null);
    }

    public String getRegistrasi() {
        return pref.getString(REGISTRASI_STATUS, null);
    }

    //Printer Setting
    public static final String NAMA_PRINTER = "nama_printer";

    public static final String ALAMAT_PRINTER = "alamat_printer";

    public static final String STS_STRUK = "status_cetak_struk";

    public static final String STS_DAPUR = "status_cetak_dapur";

    public static final String TOTAL_STRUK = "banyak_struk";

    public static final String TOTAL_STRUK_DAPUR = "banyak_struk_dapur";

    public static final String UKURAN_STRUK = "ukuran_struk";

    public static final String TIPE_PRINTER = "tipe_printer";

    public static final String PRINTER = "printer";

    public String getNamaPrinter() {
        return pref.getString(NAMA_PRINTER, null);
    }

    public String getAlamatPrinter() {
        return pref.getString(ALAMAT_PRINTER, null);
    }

    public String getStsStruk() {
        return pref.getString(STS_STRUK, "1");
    }

    public String getStsDapur() {
        return pref.getString(STS_DAPUR, "1");
    }

    public String getTotalStruk() {
        return pref.getString(TOTAL_STRUK, "1");
    }

    public String getTotalStrukDapur() {
        return pref.getString(TOTAL_STRUK_DAPUR, null);
    }

    public String getUkuranStruk() {
        return pref.getString(UKURAN_STRUK, null);
    }

    public String getTipePrinter() {
        return pref.getString(TIPE_PRINTER, null);
    }

    public String getPrinter() {
        return pref.getString(PRINTER, null);
    }

    public void setNAMA_PRINTER(String nm) {
        editor.putString(NAMA_PRINTER, nm);
        editor.commit();
    }

    public void setALAMAT_PRINTER(String almt) {
        editor.putString(ALAMAT_PRINTER, almt);
        editor.commit();
    }

    public void setSTS_STRUK(String sts_struk) {
        editor.putString(STS_STRUK, sts_struk);
        editor.commit();
    }

    public void setSTS_DAPUR(String sts_dapur) {
        editor.putString(STS_DAPUR, sts_dapur);
        editor.commit();
    }

    public void setTOTAL_STRUK(String totalStruk) {
        editor.putString(TOTAL_STRUK, totalStruk);
        editor.commit();
    }

    public void setTOTAL_STRUK_DAPUR(String totalStrukDapur) {
        editor.putString(TOTAL_STRUK_DAPUR, totalStrukDapur);
        editor.commit();
    }

    public void setUKURAN_STRUK(String ukuranStruk) {
        editor.putString(UKURAN_STRUK, ukuranStruk);
        editor.commit();
    }

    public void setTIPE_PRINTER(String tipe) {
        editor.putString(TIPE_PRINTER, tipe);
        editor.commit();
    }

    public void setPRINTER(String printer) {
        editor.putString(PRINTER, printer);
        editor.commit();
    }



}