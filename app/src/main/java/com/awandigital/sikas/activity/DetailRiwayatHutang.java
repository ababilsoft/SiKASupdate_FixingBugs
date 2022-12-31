package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.nominal_hutang;
import static com.awandigital.sikas.utils.customImage.getResizedBitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ADataDetailRencanaKeuangan;
import com.awandigital.sikas.adapter.ADetailRiwayatHutang;
import com.awandigital.sikas.adapter.AUtangPiutang;
import com.awandigital.sikas.databinding.ActivityDetailRiwayatHutangBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.model.MDetailDataRencanaKeuangan;
import com.awandigital.sikas.model.MHutangDetail;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.export_file.excel.exportDetailRiwayatHutangExcel;
import com.awandigital.sikas.utils.export_file.excel.exportRiwayatHutangExcel;
import com.awandigital.sikas.utils.export_file.pdf.exportDetailRiwayatHutangPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportRiwayatHutangPDF;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class DetailRiwayatHutang extends AppCompatActivity {
    ActivityDetailRiwayatHutangBinding binding;
    ADetailRiwayatHutang aDetailRiwayatHutang;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    String id, nama, sisa_bayar, tgl_transaksi, jenis_transaksi, nohpkontak, status_transaksi;
    ArrayList<MHutangDetail> arrayList = new ArrayList<>();
    ArrayList<MHutangDetail> arrayListCopy = new ArrayList<>();
    int dumpnominal;
    SessionManager sessionManager;

    public BroadcastReceiver receiveUpdate = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String value = intent.getStringExtra("refresh");
            if (value.equals("list")) {
                arrayList.clear();
                listData(id);
                getDetailHutang();

                Cursor cursor = dbHelper.getAllHutangDetailById(id);
                if (cursor.moveToFirst()) {
                    sisa_bayar = "Rp" + DecimalsFormat.priceWithoutDecimal(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
                }
                binding.tvNominal.setText(sisa_bayar);
            }

        }
    };

//    public BroadcastReceiver receiveExit = new BroadcastReceiver() {
//        @SuppressLint("Range")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            AndLog.ShowLog("ahsgd", getIntent().getStringExtra("q"));
////            if (getIntent().getStringExtra("q").equals("exit")) {
////                finish();
////            }
//
//            // Get extra data included in the Intent
////            Jenis = intent.getStringExtra("jenis");
////            binding.etTujuanPemasukan.setText("Kas Umum");
////            binding.etDiambilDari.setText("Kas Umum");
////            AndLog.ShowLog("jenis", Jenis);
////
//////          Ambil Data Nominal Kas Umum
////            Cursor cursor = dbHelper.dataKasUmum();
////            if (cursor.moveToFirst()) {
////                saldoKasUmum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nominal")));
////
////                AndLog.ShowLog("nominalKasUmum", String.valueOf(saldoKasUmum));
////
////            }
//        }
//
//    };

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRiwayatHutangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(DetailRiwayatHutang.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        sessionManager = new SessionManager(DetailRiwayatHutang.this);

        id = getIntent().getStringExtra("id");
        nama = getIntent().getStringExtra("nama");


        AndLog.ShowLog("id_d", id);

//        LocalBroadcastManager.getInstance(DetailRiwayatHutang.this).registerReceiver(receiveExit,
//                new IntentFilter("get-data-q"));

        LocalBroadcastManager.getInstance(DetailRiwayatHutang.this).registerReceiver(receiveUpdate,
                new IntentFilter("listHutangPiutang-state"));

        getDetailHutang();

//        AndLog.ShowLog("status_transaksi", cursor.getString(cursor.getColumnIndex("status_transaksi")));

        if (jenis_transaksi.equals("Piutang")) {
//            total hutang
            binding.tvOrang1.setText(nama);
//            ke
            binding.tvOrang2.setText("Saya");
        } else {
//            total hutang
            binding.tvOrang1.setText("Saya");
//            ke
            binding.tvOrang2.setText(nama);
        }

        binding.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRiwayatHutang.this, DetailKontakActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        binding.tvTandaiLunas.setText(status_transaksi);

        Cursor cursor1 = dbHelper.getDetailKontakByNama(nama);
        if (cursor1.moveToFirst()) {
            nohpkontak = cursor1.getString(cursor1.getColumnIndex("no_hp"));
            byte[] imgByte = cursor1.getBlob(cursor1.getColumnIndex("image_profile"));
            if (imgByte != null) {
                if (imgByte.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    binding.ivAkun.setImageBitmap(getResizedBitmap(bmp, 512));
                }
            }
        }

        binding.tvNamaPenghutang.setText(nama);
        binding.tvNominal.setText(sisa_bayar);
        binding.tvtglTransaksi.setText(tgl_transaksi);
        binding.tvNoHp.setText(nohpkontak);

        binding.rvDetailUtang.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(DetailRiwayatHutang.this, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvDetailUtang.setLayoutManager(layoutManager);

        listData(id);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status_transaksi.equals("Lunas")) {
                    GlobalToast.ShowToast(DetailRiwayatHutang.this, "Hutang sudah lunas");
                } else {
                    final BottomSheetDialog bsd = new BottomSheetDialog(DetailRiwayatHutang.this,
                            R.style.BottomShetDialogTheme);
                    bsd.setContentView(R.layout.lbs_tambah_pembayaran_utang_piutang);
                    EditText etJumlah = bsd.findViewById(R.id.et_jumlah);
                    etJumlah.addTextChangedListener(new NumberTextWatcherForThousand(etJumlah));
                    EditText etCatatan = bsd.findViewById(R.id.et_catatan);
                    Button btSimpan = bsd.findViewById(R.id.bt_tambah);

                    Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String vjml = NumberTextWatcherForThousand.trimCommaOfString(etJumlah.getText().toString().trim());
                            String vcatatan = Objects.requireNonNull(etCatatan).getText().toString().trim();

                            if (vjml.isEmpty() || vcatatan.isEmpty()) {
                                GlobalToast.ShowToast(DetailRiwayatHutang.this, "Data tidak boleh kosong");
                            } else {
                                int sisa;
                                int cicil = Integer.parseInt(vjml);
                                sisa = dumpnominal - cicil;

                                dbHelper.tambahCicilan(id, vjml, vcatatan);
                                dbHelper.updateHutangPiutang(id, String.valueOf(sisa));
//
//                            bsd.dismiss();
                                arrayList.clear();
                                listData(id);

                                Cursor cursor = dbHelper.getAllHutangDetailById(id);
                                if (cursor.moveToFirst()) {
                                    sisa_bayar = "Rp" + DecimalsFormat.priceWithoutDecimal(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
                                }
                                binding.tvNominal.setText(sisa_bayar);
                                bsd.dismiss();
                                GlobalToast.ShowToast(DetailRiwayatHutang.this, "Data cicilan baru ditambahkan");
                            }
                        }
                    });
                    bsd.show();
                }
            }
        });

        binding.tvTandaiLunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status_transaksi.equals("Lunas")) {
                    GlobalToast.ShowToast(DetailRiwayatHutang.this, "Hutang sudah lunas");
                } else {
                    arrayList.clear();
                    dbHelper.tambahCicilan(id, String.valueOf(dumpnominal), "Hutang Lunas");
                    dbHelper.updateHutangPiutangLunas(id);

                    Cursor cursor = dbHelper.getAllHutangDetailById(id);
                    if (cursor.moveToFirst()) {
                        sisa_bayar = "Rp" + DecimalsFormat.priceWithoutDecimal(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
                        status_transaksi = cursor.getString(cursor.getColumnIndex("status_transaksi"));
                    }
                    binding.tvNominal.setText(sisa_bayar);
                    binding.tvTandaiLunas.setText(status_transaksi);
                    listData(id);
                    getDetailHutang();
                    GlobalToast.ShowToast(DetailRiwayatHutang.this, "Hutang anda sudah lunas");
                }
            }
        });

        binding.btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        DetailRiwayatHutang.this, R.style.BottomShetDialogTheme);
                View bsdView = LayoutInflater.from(DetailRiwayatHutang.this)
                        .inflate(R.layout.lbs_catatan_keuangan_download,
                                DetailRiwayatHutang.this.findViewById(R.id.lbs_download));

                RadioGroup radioGroup = bsdView.findViewById(R.id.radioSex);
                RadioButton pdf = bsdView.findViewById(R.id.rbPdf);
                RadioButton excel = bsdView.findViewById(R.id.rbExcel);
                Button btnDownload = bsdView.findViewById(R.id.btSimpan);

                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        String title = "";
                        if (jenis_transaksi.equals("Piutang")) {
                            title = "Hutang " + nama + " ke Saya ";
                        } else {
                            title = "Hutang Saya ke " + nama;
                        }
                        if (selectedId == pdf.getId()) {
                            if (arrayListCopy.isEmpty()) {
                                Toast.makeText(DetailRiwayatHutang.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                exportDetailRiwayatHutangPDF.createPdf(DetailRiwayatHutang.this, arrayListCopy, "Detail_Riwayat_Hutang", true, title, String.valueOf(dumpnominal), status_transaksi);
                                Toast.makeText(DetailRiwayatHutang.this, "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(DetailRiwayatHutang.this, "File gagal disimpan", Toast.LENGTH_SHORT).show();
                            }
                            bsd.dismiss();
                        } else if (selectedId == excel.getId()) {
                            if (arrayListCopy.isEmpty()) {
                                Toast.makeText(DetailRiwayatHutang.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            exportDetailRiwayatHutangExcel.exportDataIntoWorkbook(DetailRiwayatHutang.this, "Detail_Riwayat_Hutang", arrayListCopy, title, String.valueOf(dumpnominal), status_transaksi);
                            bsd.dismiss();
                        }
                    }
                });

                bsd.setContentView(bsdView);

                bsd.show();
            }
        });

        binding.etCariCatatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aDetailRiwayatHutang.filter(String.valueOf(charSequence), arrayListCopy);
                aDetailRiwayatHutang.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("Range")
    void getDetailHutang() {
        Cursor cursor = dbHelper.getAllHutangDetailById(id);
        if (cursor.moveToFirst()) {
            sisa_bayar = "Rp" + DecimalsFormat.priceWithoutDecimal(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
            dumpnominal = Integer.parseInt(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
            tgl_transaksi = cursor.getString(cursor.getColumnIndex("tgl_transaksi"));
            jenis_transaksi = cursor.getString(cursor.getColumnIndex("jenis_transaksi"));
            status_transaksi = cursor.getString(cursor.getColumnIndex("status_transaksi"));
            binding.tvJumlahUtang.setText(DecimalsFormat.priceWithoutDecimal(cursor.getString(cursor.getColumnIndex("nominal"))));
        }

        if (cursor.getString(cursor.getColumnIndex("status_transaksi")).equals("Lunas")) {
            binding.tvTandaiLunas.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndLog.ShowLog("ASDAS", "AKJSDH");
    }

    private void listData(String idhp) {
        arrayList.clear();
        Cursor cursorproduct = dbHelper.getAllHutangDetailByIdHp(idhp);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                arrayList.add(new MHutangDetail(
                        cursorproduct.getString(0), // id detail
                        cursorproduct.getString(1), // id hp
                        cursorproduct.getString(2), // tgl transaksi
                        cursorproduct.getString(7), // nominal
                        cursorproduct.getString(8), // sisa bayar
                        cursorproduct.getString(3), // jml cicil
                        cursorproduct.getString(4), // catatan
                        cursorproduct.getString(6) // status transaksi
                ));
            } while (cursorproduct.moveToNext());
        }
        if (arrayList.isEmpty()) {
            AndLog.ShowLog("LOG_DETAIL_RIWAYAT_UP", "No items Found in database");
        }
        //closing the cursor
        cursorproduct.close();
        arrayListCopy.clear();
        arrayListCopy.addAll(arrayList);

        //creating the adapter object
        aDetailRiwayatHutang = new ADetailRiwayatHutang(arrayList, DetailRiwayatHutang.this, mDatabase);

        //adding the adapter to listview
        binding.rvDetailUtang.setAdapter(aDetailRiwayatHutang);
    }

}