package com.awandigital.sikas.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.MSubItemCatatanKeuanganMasuk;
import com.awandigital.sikas.utils.AndLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String database_name = "sikas.db";
    Calendar c = Calendar.getInstance();
    SimpleDateFormat formatlengkap = new SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm", new Locale("id", "ID"));
    SimpleDateFormat created_at = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", new Locale("id", "ID"));
    SimpleDateFormat formattgl = new SimpleDateFormat("dd-MM-yyyy", new Locale("id", "ID"));
    SimpleDateFormat formatbulan = new SimpleDateFormat("MM", new Locale("id", "ID"));
    SimpleDateFormat formattahun = new SimpleDateFormat("yyyy", new Locale("id", "ID"));
    SimpleDateFormat formatwaktu = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));
    SimpleDateFormat formatDay = new SimpleDateFormat("dd", new Locale("id", "ID"));
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    String fday = formatDay.format(c.getTime());
    String fday2nd = sdf.format(c.getTime());
    String ftanggal = formattgl.format(c.getTime());
    String fbulan = formatbulan.format(c.getTime());
    String ftahun = formattahun.format(c.getTime());
    String fwaktu = formatwaktu.format(c.getTime());
    String fwaktulengkap = formatlengkap.format(c.getTime());
    String fcreated_at = created_at.format(c.getTime());

    //umum
    public static final String hari = "hari";
    public static final String bulan = "bulan";
    public static final String tahun = "tahun";

    //user
    public static final String id_user = "id_user";
    public static final String nama = "nama_lengkap";
    public static final String username = "username";
    public static final String password = "password";
    public static final String email = "email";
    public static final String status_login = "status_login";
    public static final String image_profile = "image_profile";

    //rencana_keuangan
    public static final String id_renkeu = "id_renkeu";
    public static final String nama_rencana = "nama_rencana";
    public static final String budget_rencana = "budget";
    public static final String saldo_sekarang = "saldo_sekarang";
    public static final String created_at_rencana = "created_at";

    //piutang_detail
    public static final String id_piutang_detail = "id_piutang_detail";
    public static final String id_piutang_piutang_detail = "id_piutang";
    public static final String tanggal_piutang_detail = "tanggal";
    public static final String nominal_piutang_detail = "nominal";
    public static final String catatan_piutang_detail = "catatan";

    //piutang
    public static final String id_piutang = "id_piutang";
    public static final String nama_penghutang = "nama_penghutang";
    public static final String nominal_piutang = "nominal";
    public static final String tgl_hutang_piutang = "tgl_hutang";
    public static final String tgl_jatuhtempo = "tgl_jatuhtempo";
    public static final String status_piutang = "status";
    public static final String created_at_piutang = "created_at";

    //pengeluaran
    public static final String id_pengeluaran = "id_pengeluaran";
    public static final String kategori_pengeluaran = "kategori";
    public static final String nama_rencana_pengeluaran = "nama_rencana";
    public static final String tgl_transaksi_pengeluaran = "tgl_transaksi";
    public static final String nominal_pengeluaran = "nominal";
    public static final String diambil_dari = "diambil_dari";
    public static final String metode_transaksi_pengeluaran = "metode_transaksi";
    public static final String catatan_pengeluaran = "catatan";
    public static final String created_at_pengeluaran = "created_at";

    //pemasukan
    public static final String id_pemasukan = "id_pemasukan";
    public static final String kategori_pemasukan = "kategori";
    public static final String tujuan_tabungan = "tujuan_tabungan";
    public static final String nominal_pemasukan = "nominal";
    public static final String metode_transaksi_pemasukan = "metode_transaksi";
    public static final String tgl_transaksi_pemasukan = "tgl_transaksi";
    public static final String catatan_pemasukan = "catatan";
    public static final String created_at_pemasukan = "created_at";

    //kategori
    public static final String id_kategori = "id_kategori";
    public static final String kategori = "kategori";
    public static final String created_at_kategori = "created_at";

    //hutang_detail
    public static final String id_hutang_detail = "id_hutang_detail";
    public static final String id_hutang_hutang_detail = "id_hutang";
    public static final String tanggal_hutang_detail = "tanggal";
    public static final String nominal_hutang_detail = "nominal";
    public static final String catatan_hutang_detail = "catatan";

    //hutang
    public static final String id_hutang = "id_hutang";
    public static final String hutang_ke = "hutang_ke";
    public static final String nominal_hutang = "nominal";
    public static final String tgl_hutang = "tgl_hutang";
    public static final String tgl_jatuhtempo_hutang = "tgl_jatuhtempo";
    public static final String status_hutang = "status_hutang";
    public static final String created_at_hutang = "created_at";
    public static final String updated_at_hutang = "updated_at";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 7);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String pengguna = "CREATE TABLE user (" +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama_lengkap TEXT(200)," +
                "email TEXT(100)," +
                "username TEXT(100)," +
                "password TEXT(100)," +
                "status_login TEXT(100)," +
                "image_profile BLOB," +
                "updated_at TEXT(100)" +
                ")";

        String kategori = "CREATE TABLE kategori (" +
                "id_kategori INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kategori TEXT(100)," +
                "created_at TEXT" +
                ")";


        String kontak = "CREATE TABLE kontak (" +
                "id_kontak INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama_kontak TEXT(100)," +
                "no_hp TEXT(100)," +
                "alamat TEXT(100)," +
                "image_profile BLOB," +
                "created_at TEXT" +
                ")";

        String rencana_keuangan = "CREATE TABLE rencana_keuangan (" +
                "id_renkeu INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama_rencana TEXT," +
                "target_dana TEXT(100)," +
                "dana_terkumpul TEXT(100)," +
                "target_waktu TEXT(100)," +
                "status TEXT(100)," +
                "realisasi_dana TEXT(100)," +
                "created_at TEXT," +
                "updated_at TEXT" +
                ")";

        String saldo_umum = "CREATE TABLE saldo_umum (" +
                "id_saldo_umum INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nominal TEXT(100)," +
                "catatan TEXT(200)," +
                "updated_at TEXT" +
                ")";

        String catatan_transaksi = "CREATE TABLE catatan_transaksi (" +
                "id_catatan_transaksi INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nominal TEXT(200)," +
                "tujuan_tabungan TEXT(200)," +
                "diambil_dari TEXT(200)," +
                "kategori TEXT(200)," +
                "tgl_transaksi TEXT(200)," +
                "metode_transaksi TEXT(100)," +
                "jenis_transaksi TEXT(100)," +
                "status_transaksi TEXT(100)," +
                "hari TEXT(100)," +
                "bulan TEXT(100)," +
                "tahun TEXT(100)," +
                "catatan TEXT(100)," +
                "created_at TEXT," +
                "nama_transaksi TEXT(200)" +
                ")";

        String hutang_piutang = "CREATE TABLE hutang_piutang (" +
                "id_hp INTEGER PRIMARY KEY AUTOINCREMENT," +
                "jenis_transaksi TEXT(200)," +
                "dipinjamkan_oleh TEXT(200)," +
                "dipinjamkan_ke TEXT(200)," +
                "nominal TEXT(200)," +
                "tgl_transaksi TEXT," +
                "tgl_jatuhtempo TEXT," +
                "sisa_bayar TEXT(100)," +
                "status_transaksi TEXT(100)," +
                "catatan TEXT(100)," +
                "created_at TEXT," +
                "updated_at TEXT" +
                ")";

        String hutang_piutang_detail = "CREATE TABLE hutang_piutang_detail (" +
                "id_hp_detail INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_hp INTEGER(100)," +
                "tgl_transaksi TEXT," +
                "nominal TEXT(100)," +
                "catatan TEXT(200)," +
                "created_at TEXT" +
                ")";


        db.execSQL(kategori);
        db.execSQL(catatan_transaksi);
        db.execSQL(hutang_piutang);
        db.execSQL(hutang_piutang_detail);
        db.execSQL(rencana_keuangan);
        db.execSQL(pengguna);
        db.execSQL(saldo_umum);
        db.execSQL(kontak);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS hutang_piutang");
        db.execSQL("DROP TABLE IF EXISTS hutang_piutang_detail");
        db.execSQL("DROP TABLE IF EXISTS kategori");
        db.execSQL("DROP TABLE IF EXISTS catatan_transaksi");
        db.execSQL("DROP TABLE IF EXISTS rencana_keuangan");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS saldo_umum");
        db.execSQL("DROP TABLE IF EXISTS kontak");

        // create raw query to delete user where id = 1
        String query = "DELETE FROM user WHERE id_user = 1";

        this.onCreate(db);
    }


    //getAllData

    public Cursor getAllUtangPiutang() {
        Cursor cur = db.rawQuery("SELECT * FROM hutang_piutang", null);
        return cur;
    }

    public Cursor getAllHutangDetailById(String idhp) {
        String query = "SELECT * FROM hutang_piutang WHERE id_hp = '" + idhp + "'";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("teskdetail", query);
        return cur;
    }

    public Cursor getAllHutangDetailByIdHp(String idhp) {
        String query = "SELECT hutang_piutang_detail.id_hp_detail, hutang_piutang_detail.id_hp, hutang_piutang_detail.tgl_transaksi,\n" +
                "hutang_piutang_detail.nominal AS jml_cicil, hutang_piutang_detail.catatan, hutang_piutang_detail.created_at,\n " +
                "hutang_piutang.nominal, hutang_piutang.sisa_bayar, hutang_piutang.status_transaksi \n" +
                "FROM hutang_piutang_detail LEFT JOIN hutang_piutang\n" +
                "ON hutang_piutang_detail.id_hp = hutang_piutang.id_hp\n" +
                "WHERE hutang_piutang_detail.id_hp = '" + idhp + "' ORDER BY hutang_piutang_detail.created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getUtangSaya() {
        String query = "SELECT SUM(sisa_bayar) utangsaya FROM hutang_piutang WHERE jenis_transaksi = 'Utang'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getUtangTeman() {
        String query = "SELECT SUM(sisa_bayar) utangteman FROM hutang_piutang WHERE jenis_transaksi = 'Piutang'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getListHutangPiutangWhere(String jtransaksi) {
        String query = "SELECT * FROM hutang_piutang WHERE jenis_transaksi = '" + jtransaksi + "'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getListHutangPiutangLunas(String status_hutang) {
        String query = "SELECT * FROM hutang_piutang WHERE status_transaksi = '" + status_hutang + "'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }


    public Cursor getAllKategori() {
        Cursor cur = db.rawQuery("SELECT * FROM kategori", null);
        return cur;
    }

    public Cursor getAllPemasukan() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' ORDER BY created_at DESC", null);
        return cur;
    }

    public Cursor getAllPemasukanChart() {
        Cursor cur = db.rawQuery("SELECT id_catatan_transaksi, SUM(nominal), tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' GROUP BY created_at ORDER BY created_at DESC", null);
        return cur;
    }

    public Cursor getAllCatatan() {
        String query = "SELECT * FROM catatan_transaksi ORDER BY created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("queryAllCatatan", query);
        return cur;
    }

    public Cursor getAllCatatanById(String id) {
        String query = "SELECT * FROM catatan_transaksi WHERE id_catatan_transaksi = '" + id + "'";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("queryAllCatatanById", query);
        return cur;
    }

    public Cursor MonthlygetAllCatatan() {
        String query = "SELECT * FROM catatan_transaksi WHERE created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("queryAllCatatan", query);
        return cur;
    }

    public Cursor getPemasukanDailyLineChart() {
        String query = "SELECT id_catatan_transaksi, SUM(nominal), tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND hari BETWEEN " + "'" + fday + "'" + " AND " + "'" + fday + "'" + " AND bulan = " + "'" + fbulan + "'" + "GROUP BY created_at ORDER BY created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getFilterPemasukan", query);
        return cur;
    }

    public Cursor getPemasukanHarianFinancialPlanning(String nama_transaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE nama_transaksi = '" + nama_transaksi + "'  AND jenis_transaksi = 'pemasukan' AND hari BETWEEN '" + fday + "' AND '" + fday + "' AND bulan = '" + fbulan + "'GROUP BY created_at ORDER BY created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getPemasukanHarianFinancialPlanning", query);
        return cur;
    }


    public Cursor getNominalPemasukan() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' ORDER BY created_at DESC limit 1", null);
        return cur;
    }

    public Cursor getFilterPemasukan() {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal,tgl_transaksi FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND hari BETWEEN " + "'" + fday + "'" + " AND " + "'" + fday + "'" + " AND bulan = " + "'" + fbulan + "'";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getFilterPemasukan", query);
        return cur;
    }

    public Cursor getAllPemasukanWhereTgl(String v) {
        Cursor cur = db.rawQuery("SELECT tgl_transaksi, tujuan_tabungan, kategori, nominal FROM pemasukan WHERE tgl_transaksi = " + "'" + v + "'", null);
        AndLog.ShowLog("getAllPemasukanWhereTgl", "SELECT tgl_transaksi, tujuan_tabungan, kategori, nominal FROM pemasukan WHERE tgl_transaksi = " + "'" + v + "'");
        return cur;
    }

    public Cursor getFilterPemasukanDay() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND tgl_transaksi = " + "'" + fday2nd + "'", null);
        return cur;
    }

    public Cursor getFilterPemasukanDay2nd(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND tgl_transaksi = " + "'" + fday2nd + "'";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getDay", query);
        return cur;
    }

    public Cursor getFilterPengeluaranDay() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND tgl_transaksi = " + "'" + fday2nd + "'", null);
        return cur;
    }

    public Cursor getPengeluaranDailyLineChart() {
        String query = "SELECT id_catatan_transaksi, SUM(nominal), tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND hari BETWEEN " + "'" + fday + "'" + " AND " + "'" + fday + "'" + " AND bulan = " + "'" + fbulan + "'" + "GROUP BY created_at ORDER BY created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }


    public Cursor getAllCatatanDay() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE tgl_transaksi = " + "'" + fday2nd + "'", null);
        return cur;
    }

    public Cursor getPengeluaranWeeklyLineChart() {
        Cursor cur = db.rawQuery("SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND created_at >= datetime('now', '-7 days') GROUP BY hari,bulan,tahun ORDER BY created_at ASC", null);
        return cur;
    }

    public Cursor getPengeluaranMonthlyLineChart() {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND created_at >= datetime('now', '-30 days') GROUP BY hari,bulan,tahun ORDER BY created_at ASC";
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }


    public Cursor getFilterPengeluaranDay2nd(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND tgl_transaksi = " + "'" + fday2nd + "'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getFilterPemasukanWeekly() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')", null);
        return cur;
    }

    public Cursor getPemasukanWeeklyLineChart() {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime') GROUP BY tgl_transaksi";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getPemasukanWeeklyLineChart", query);
        return cur;
    }

    public Cursor getPemasukanMingguanFinancialPlanning(String nama_transaksi) {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE nama_transaksi = '" + nama_transaksi + "' AND jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime') GROUP BY tgl_transaksi";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getPemasukanMingguanFinancialPlanning", query);
        return cur;
    }

    public Cursor getAllCatatanWeekly() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')", null);
        return cur;
    }

    public Cursor getFilterPemasukanWeekly2nd(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getFilterPengeluaranWeekly() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')", null);
        return cur;
    }

    public Cursor getFilterPengeluaranWeekly2nd(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND created_at BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getFilterPemasukanMonthly() {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }

    public Cursor getPemasukanMonthlyLineChart() {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime') GROUP BY bulan";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getPemasukanMonthlyLineChart", query);
        return cur;
    }

    public Cursor getPemasukanFinancialPlanning(String nama_transaksi) {
        String query = "SELECT id_catatan_transaksi,SUM(nominal) AS nominal, tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE nama_transaksi = '" + nama_transaksi + "' AND jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime') GROUP BY bulan";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("getPemasukanFinancialPlanning", query);
        return cur;
    }

    public Cursor getPemasukanBulananFinancialPlanning(String nama_transaksi) {
        String query = " SELECT * FROM catatan_transaksi WHERE nama_transaksi = '" + nama_transaksi + "' AND jenis_transaksi = 'pemasukan' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime') GROUP BY bulan";
        Cursor cur = db.rawQuery(query, null);

        return cur;
    }


    public Cursor getFilterPemasukanMonthlyBy(String jt) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jt + "' AND bulan = '" + fbulan + "'";
        Cursor cur = db.rawQuery(query, null);
//        AndLog.ShowLog("asdasdasf", query);
        return cur;
    }

    public Cursor getFilterPemasukanMonthly2nd(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getFilterPengeluaranMonthly() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')", null);
        return cur;
    }

    public Cursor getFilterPengeluaranMonthly(String jenistransaksi) {
        String query = "SELECT * FROM catatan_transaksi WHERE jenis_transaksi = '" + jenistransaksi + "' AND created_at BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getAllPengeluaran() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' ORDER BY created_at", null);
        return cur;
    }

    public Cursor getAllPengeluaranChart() {
        Cursor cur = db.rawQuery("SELECT id_catatan_transaksi, SUM(nominal), tujuan_tabungan,diambil_dari,kategori,tgl_transaksi,metode_transaksi,jenis_transaksi,hari,bulan,tahun,catatan,status_transaksi,created_at FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' GROUP BY created_at ORDER BY created_at DESC ", null);
        return cur;
    }

    public Cursor getNominalPengeluaran() {
        Cursor cur = db.rawQuery("SELECT * FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' ORDER BY created_at DESC limit 1", null);
        return cur;
    }

    public Cursor getAllPiutang() {
        Cursor cur = db.rawQuery("SELECT * FROM piutang", null);
        return cur;
    }

    public Cursor getAllPiutangDetail() {
        Cursor cur = db.rawQuery("SELECT * FROM piutang_detail", null);
        return cur;
    }

    public Cursor getDetailRencanaKeuangan(String v) {
        String query = "SELECT * FROM catatan_transaksi WHERE nama_transaksi = '" + v + "' ORDER BY created_at DESC";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getAllRencanaKeuangan() {
        Cursor cur = db.rawQuery("SELECT * FROM rencana_keuangan ORDER BY created_at DESC", null);
        return cur;
    }

    public Cursor getAllUser() {
        Cursor cur = db.rawQuery("SELECT * FROM user", null);
        return cur;
    }

    public Cursor dataKasUmum() {

        Cursor cur = db.rawQuery("SELECT * FROM saldo_umum", null);

        return cur;
    }

    public Cursor dataSaldoFp() {

        Cursor cur = db.rawQuery("SELECT sum(dana_terkumpul) AS totalfp FROM rencana_keuangan", null);

        return cur;
    }

    public Cursor pemasukanRencanaKeuangan(String a) {
        String query = "SELECT nominal,nama_transaksi FROM catatan_transaksi WHERE jenis_transaksi = 'pemasukan' AND nama_transaksi  = '" + a + "' ORDER BY created_at DESC limit 1";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("pemasukanRencanaKeuangann", query);
        return cur;
    }

    public Cursor pengeluaranRencanaKeuangan(String a) {
        String query = "SELECT nominal,nama_transaksi FROM catatan_transaksi WHERE jenis_transaksi = 'pengeluaran' AND nama_transaksi  = '" + a + "' ORDER BY created_at DESC limit 1";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("pengeluaranRencanaKeuangan", query);
        return cur;
    }

    public Cursor dataRencanaKeuanganBy(String a) {
        String query = "SELECT * FROM rencana_keuangan WHERE nama_rencana = '" + a + "'";
        Cursor cur = db.rawQuery(query, null);
        AndLog.ShowLog("dataRencanaKeuanganBy", query);
        return cur;
    }

    public Cursor getAllKontak() {

        Cursor cur = db.rawQuery("SELECT * FROM kontak ORDER BY created_at DESC limit 5", null);
        return cur;
    }

    public Cursor getDetailKontakByNama(String nama_kontak) {
        String query = "SELECT * FROM kontak WHERE nama_kontak = '" + nama_kontak + "'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public Cursor getDetailKontakById(String id) {
        String query = "SELECT * FROM kontak WHERE id_kontak = '" + id + "'";
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }

    public void deleteKontak(String id) {
        db.execSQL("DELETE FROM kontak WHERE id_kontak = '" + id + "'");
    }

    public void updateKontakById(String id, String nama_kontak, String no_hp, String alamat) {
        String query = "UPDATE kontak SET nama_kontak = '" + nama_kontak + "', no_hp = '" + no_hp + "', alamat = '" + alamat + "' WHERE id_kontak = '" + id + "'";
        db.execSQL(query);
    }

    public void updateKontakImage(byte[] image) {
        ContentValues values = new ContentValues();
        values.put("image_profile", image);
        db.update("kontak", values, null, null);
    }

    @SuppressLint("Range")
    public ArrayList<String> getKontak() {
        ArrayList<String> kontak = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM kontak", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            kontak.add(cursor.getString(cursor.getColumnIndex("nama_kontak")));
            cursor.moveToNext();
        }

        cursor.close();
        return kontak;
    }

    //delete Data
    public void deletePengeluaran(String id) {
        db.delete("pengeluaran", "id_pengeluaran" + "=" + "'" + id + "'", null);
    }

    public void deleteCatatanKeuangan(String id) {
        db.delete("catatan_transaksi", "id_catatan_transaksi =" + "'" + id + "'", null);
    }

    public void deleteHutang(String id) {
        db.delete("hutang", "id_hutang" + "=" + "'" + id + "'", null);
    }

    public void deleteCicilan(String id) {
        db.delete("hutang_piutang_detail", "id_hp_detail = " + "'" + id + "'", null);
    }

    public void deleteHutangDetail(String id) {
        db.delete("hutang_piutang_detail", "id_hutang_detail" + "=" + "'" + id + "'", null);
    }

    public void deletePiutang(String id) {
        db.delete("piutang", "id_piutang" + "=" + "'" + id + "'", null);
    }

    public void deletePiutangDetail(String id) {
        db.delete("piutang_detail", "id_piutang" + "=" + "'" + id + "'", null);
    }

    public void deleteKategori(String id) {
        db.delete("kategori", "id_kategori" + "=" + "'" + id + "'", null);
    }

    public void deleteRencanaKeuangan(String id) {
        db.delete("rencana_keuangan", "id_renkeu" + "=" + "'" + id + "'", null);
    }

    public void hapusAkun() {

        // Gunakan perintah SQL DELETE untuk menghapus semua data dari tabel
        String q1 = "DELETE FROM user";
        String q2 = "DELETE FROM saldo_umum";
        String q3 = "DELETE FROM rencana_keuangan";
        String q4 = "DELETE FROM kontak";
        String q5 = "DELETE FROM kategori";
        String q6 = "DELETE FROM hutang_piutang";
        String q7 = "DELETE FROM hutang_piutang_detail";
        String q8 = "DELETE FROM catatan_transaksi";

        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);
        db.execSQL(q4);
        db.execSQL(q5);
        db.execSQL(q6);
        db.execSQL(q7);
        db.execSQL(q8);

        // Tutup koneksi ke database
        db.close();

    }

    public void isEmptySaldoUmum() {
        db.execSQL("DELETE FROM saldo_umum");
    }

    //update data
    public void updateKategori(String kategori, String idKategori) {
        ContentValues values = new ContentValues();
        values.put("kategori", kategori);
        values.put("id_kategori", idKategori);
        db.update("kategori", values, "id_kategori" + " = " + "'" + idKategori + "'", null);
    }

    public void updateHutangPiutang(String idhp, String sisa_bayar) {
        ContentValues values = new ContentValues();
        values.put("id_hp", idhp);
        values.put("sisa_bayar", sisa_bayar);
        values.put("status_transaksi", "Belum Lunas");
        values.put("updated_at", fcreated_at);
        db.update("hutang_piutang", values, "id_hp = " + "'" + idhp + "'", null);
    }

    public void updateHutangPiutangLunas(String idhp) {
        ContentValues values = new ContentValues();
        values.put("id_hp", idhp);
        values.put("sisa_bayar", "0");
        values.put("status_transaksi", "Lunas");
        values.put("updated_at", fcreated_at);
        db.update("hutang_piutang", values, "id_hp = " + "'" + idhp + "'", null);
    }

    public void updateRencanaKeuanganRealisasi(String id) {
        ContentValues values = new ContentValues();
        values.put("realisasi_dana", "Sudah");
        values.put("updated_at", fcreated_at);
        db.update("rencana_keuangan", values, "nama_rencana" + " = " + "'" + id + "'", null);
    }

    public void updateRencanaKeuangan(String nominal, String id) {
        ContentValues values = new ContentValues();
        values.put("dana_terkumpul", nominal);
        values.put("updated_at", fcreated_at);
        db.update("rencana_keuangan", values, "nama_rencana" + " = " + "'" + id + "'", null);
    }

    public void updatePemasukanCatatanKeuangan(String nominal, String id) {
        ContentValues values = new ContentValues();
        values.put("nominal", nominal);
        values.put("tujuan_tabungan", nominal);
        values.put("kategori", nominal);
        values.put("updated_at", fcreated_at);
        db.update("rencana_keuangan", values, "nama_rencana" + " = " + "'" + id + "'", null);
    }

    public void updateRencanaKeuanganLunas(String nominal, String id) {
        ContentValues values = new ContentValues();
        values.put("dana_terkumpul", nominal);
        values.put("status", "Terpenuhi");
        values.put("realisasi_dana", "Belum");
        values.put("updated_at", fcreated_at);
        db.update("rencana_keuangan", values, "nama_rencana" + " = " + "'" + id + "'", null);
    }

    public void updateProfil(ContentValues values, String dEmail) {
        db.update("user", values, email + " = " + "'" + dEmail + "'", null);
    }

    public void updateImageProfile(byte[] image) {
        ContentValues values = new ContentValues();
        values.put("image_profile", image);
        db.update("user", values, null, null);
    }

    public void updateKasUmum(String nominal) {
        ContentValues values = new ContentValues();
        values.put("nominal", nominal);
        values.put("catatan", "-");
        values.put("updated_at", fcreated_at);
        db.update("saldo_umum", values, "id_saldo_umum = 1", null);
    }

    //Insert Data
    public void addKategori(String value) {
        ContentValues values = new ContentValues();
        values.put("kategori", value);
        values.put("created_at", fcreated_at);
        db.insert("kategori", null, values);
    }

    public void tambahCicilan(String idhp, String nominal, String catatan) {
        ContentValues values = new ContentValues();
        values.put("id_hp", idhp);
        values.put("tgl_transaksi", fday2nd);
        values.put("nominal", nominal);
        values.put("catatan", catatan);
        values.put("created_at", fcreated_at);
        db.insert("hutang_piutang_detail", null, values);
    }

    public void addHutang(String hutang, String nominal, String tgl1, String tgl2, String c) {
        ContentValues values = new ContentValues();
        values.put("dipinjamkan_oleh", hutang);
        values.put("jenis_transaksi", "Utang");
        values.put(nominal_hutang, nominal);
        values.put("tgl_transaksi", tgl1);
        values.put(tgl_jatuhtempo, tgl2);
        values.put("status_transaksi", "Belum Lunas");
        values.put("sisa_bayar", nominal);
        values.put("catatan", c);
        values.put(created_at_hutang, fcreated_at);
        db.insert("hutang_piutang", null, values);
    }

    public void addPiutang(String hutang, String nominal, String tgl1, String tgl2, String c) {
        ContentValues values = new ContentValues();
        values.put("dipinjamkan_ke", hutang);
        values.put("jenis_transaksi", "Piutang");
        values.put(nominal_hutang, nominal);
        values.put("tgl_transaksi", tgl1);
        values.put(tgl_jatuhtempo, tgl2);
        values.put("status_transaksi", "Belum Lunas");
        values.put("sisa_bayar", nominal);
        values.put("catatan", c);
        values.put(created_at_hutang, fcreated_at);
        db.insert("hutang_piutang", null, values);
    }


    public void addPemasukan(String JumlahPemasukan, String TujuanPemasukan, String KategoriPemasukan,
                             String TanggalPemasukan, String MetodeTransaksi, String Catatan) {
        ContentValues values = new ContentValues();
        values.put(nominal_pemasukan, JumlahPemasukan);
        values.put(tujuan_tabungan, TujuanPemasukan);
        values.put(kategori_pemasukan, KategoriPemasukan);
        values.put(tgl_transaksi_pemasukan, TanggalPemasukan);
        values.put(metode_transaksi_pemasukan, MetodeTransaksi);
        values.put("jenis_transaksi", "pemasukan");
        values.put(catatan_pemasukan, Catatan);
        values.put(hari, fday);
        values.put(bulan, fbulan);
        values.put(tahun, ftahun);
        values.put("nama_transaksi", TujuanPemasukan);
        values.put(created_at_pemasukan, fcreated_at);
        db.insert("catatan_transaksi", null, values);

        AndLog.ShowLog("addPemasukan", String.valueOf(values));
    }

    public void addKasUmum(String totalSaldo) {
        ContentValues values = new ContentValues();
        values.put("nominal", totalSaldo);
        values.put("catatan", "-");
        values.put("updated_at", fcreated_at);
        db.insert("saldo_umum", null, values);
    }

    public Cursor getDataSaldoUmum() {

        Cursor cur = db.rawQuery("SELECT count(*) as data FROM saldo_umum", null);

        return cur;
    }

    public void addPengeluaran(String nominal, String diambilDari, String kategoriPengeluaran,
                               String tglTransaksiPengeluaran, String metodeTransaksiPengeluaran,
                               String catatanPengeluaran, String statusTransaksi) {
        ContentValues values = new ContentValues();
        values.put(nominal_pengeluaran, nominal);
        values.put(diambil_dari, diambilDari);
        values.put(kategori_pengeluaran, kategoriPengeluaran);
        values.put(tgl_transaksi_pengeluaran, tglTransaksiPengeluaran);
        values.put(metode_transaksi_pengeluaran, metodeTransaksiPengeluaran);
        values.put("jenis_transaksi", "pengeluaran");
        values.put("status_transaksi", statusTransaksi);
        values.put(catatan_pengeluaran, catatanPengeluaran);
        values.put(hari, fday);
        values.put(bulan, fbulan);
        values.put(tahun, ftahun);
        values.put(created_at_pemasukan, fcreated_at);
        values.put("nama_transaksi", diambilDari);
        db.insert("catatan_transaksi", null, values);
    }

    public void addRencanaKeuangan(String nama_rencana, String totalSaldo, String targetWaktu, String created_at) {
        ContentValues values = new ContentValues();
        values.put("nama_rencana", nama_rencana);
        values.put("target_dana", totalSaldo);
        values.put("target_waktu", targetWaktu);
        values.put("created_at", created_at);
        db.insert("rencana_keuangan", null, values);
    }

    public Cursor addKontak_test(String namaK) {
        Cursor cursor = null;
//        String sql = " ;
        cursor = db.rawQuery("SELECT nama_kontak FROM kontak WHERE nama_kontak = " + "'" + namaK + "'", null);
        AndLog.ShowLog("Cursor Count : ", String.valueOf(cursor.getCount()));
        return cursor;
    }

    public void addKontak(String nama) {
        ContentValues values = new ContentValues();
        values.put("nama_kontak", nama);
        values.put("no_hp", "-");
        values.put("alamat", "-");
        values.put("created_at", fcreated_at);
        db.insert("kontak", null, values);
    }

    public void addUser(ContentValues values) {
        db.insert("user", null, values);
    }

    public void insertTujuanFinansial(String namaFinansial, int jumlahUangKumpul, String target) {
        ContentValues values = new ContentValues();
        values.put("nama_finansial_tujuan", namaFinansial);
        values.put("jumlah_uang_tujuan", jumlahUangKumpul);
        values.put("target_tujuan", target);

        db.insert("tujuan_finansial", null, values);
    }

    public void insertCatatanPengeluaran(int jumlahPengeluaran, String diambilDari, String kategori, String tanggalTransaksi, String metodePembayaran, boolean statusTransaksi, String catatan) {
        ContentValues values = new ContentValues();
        values.put("catatan_jumlah_pengeluaran", jumlahPengeluaran);
        values.put("catatan_diambil_pengeluaran", diambilDari);
        values.put("catatan_kategori_pengeluaran", kategori);
        values.put("catatan_tanggal_pengeluaran", tanggalTransaksi);
        values.put("catatan_metode_pengeluaran", metodePembayaran);
        values.put("catatan_status_pengeluaran", statusTransaksi);
        values.put("catatan_catatan_tambahan_pengeluaran", catatan);

        db.insert("pengeluaran", null, values);
    }

    public void insertCatatanPemasukan(int jumlahPemasukan, String tujuan, String kategori, String tanggalTransaksi, String metodePembayaran, boolean statusTransaksi, String catatan) {
        ContentValues values = new ContentValues();
        values.put("catatan_jumlah_pemasukan", jumlahPemasukan);
        values.put("catatan_tujuan_pemasukan", tujuan);
        values.put("catatan_kategori_pemasukan", kategori);
        values.put("catatan_tanggal_pemasukan", tanggalTransaksi);
        values.put("catatan_metode_pemasukan", metodePembayaran);
        values.put("catatan_status_pemasukan", statusTransaksi);
        values.put("catatan_catatan_tambahan_pemasukan", catatan);

        db.insert("pemasukan", null, values);
    }

    // Auth
    public void RegisterUser(ContentValues values) {
        db.insert("user", null, values);
    }

    public Cursor login(String username, String password) {

        Cursor cur = db.rawQuery("SELECT * FROM user WHERE username = " + "'" + username + "'" + "AND password =" + "'" + password + "'", null);
        return cur;
    }

}