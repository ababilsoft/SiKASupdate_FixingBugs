package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.tujuan_tabungan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityUbahTransaksiBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;

public class UbahTransaksiActivity extends AppCompatActivity {
    ActivityUbahTransaksiBinding binding;
    Intent intent;
    String jenis_transaksi;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;
    String nama_transaksi;
    int dana_terkumpul;
    int target_dana;
    String st;

    @SuppressLint({"ResourceAsColor", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUbahTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(UbahTransaksiActivity.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.etJumlah.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlah));
        binding.etJumlahPemasukan.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlahPemasukan));

        String id = getIntent().getStringExtra("id");

        Cursor cursor = dbHelper.getAllCatatanById(id);
        if (cursor.moveToNext()) {
            jenis_transaksi = cursor.getString(7);
            String status_transaksi = cursor.getString(12);
            String tgl = cursor.getString(5);
            nama_transaksi = cursor.getString(14);
            String kategori = cursor.getString(4);
            String nominal = cursor.getString(1);
            String catatan = cursor.getString(11);
            String metode_transaksi = cursor.getString(6);

            if (jenis_transaksi.equals("pemasukan")) {
                binding.tvPemasukan.setTextColor(R.color.success_400);
                binding.tvPemasukan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_circle_up_g, 0, 0, 0);

                binding.tvPemasukan.setVisibility(View.VISIBLE);
                binding.layPemasukan.setVisibility(View.VISIBLE);
                binding.tvJenisTransaksi.setVisibility(View.GONE);
                binding.layPengeluaran.setVisibility(View.GONE);

                //isi value
                binding.etJumlahPemasukan.setText(DecimalsFormat.priceWithoutDecimal(nominal));
                binding.etTujuanPemasukan.setText(nama_transaksi);
                binding.etKategoriPemasukan.setText(kategori);
                binding.etTanggalPemasukan.setText(tgl);
                binding.etMetodeTransaksiPemasukan.setText(metode_transaksi);
                binding.etCatatanPemasukan.setText(catatan);
            } else {
                binding.tvJenisTransaksi.setTextColor(R.color.danger_500);
                binding.tvJenisTransaksi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_circle_down_r, 0, 0, 0);

                binding.etDiambilDari.setText(nama_transaksi);
                binding.tvPemasukan.setVisibility(View.GONE);
                binding.layPemasukan.setVisibility(View.GONE);
                binding.tvJenisTransaksi.setVisibility(View.VISIBLE);
                binding.layPengeluaran.setVisibility(View.VISIBLE);

                //isi value

                binding.tvJenisTransaksi.setText(jenis_transaksi);
                if (status_transaksi != null) {
                    if (status_transaksi.equals("Lunas")) {
                        binding.rbLunas.setChecked(true);
                    } else {
                        binding.rbBelum.setChecked(true);
                    }
                }

                binding.etTanggalTransaksi.setText(tgl);
                binding.etKategori.setText(kategori);
                binding.etJumlah.setText(DecimalsFormat.priceWithoutDecimal(nominal));
                binding.etCatatanPengeluaran.setText(catatan);
                binding.etMetodeTransaksi.setText(metode_transaksi);
            }

            Cursor cursor1 = dbHelper.dataRencanaKeuanganBy(nama_transaksi);

            if (cursor1.moveToFirst()) {

                dana_terkumpul = cursor1.getInt(cursor1.getColumnIndex("dana_terkumpul"));
                target_dana = cursor1.getInt(cursor1.getColumnIndex("target_dana"));

            }


            binding.etKategori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(intent);
                }
            });

            binding.etMetodeTransaksi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MetodeTransaksiActivity.class);
                    startActivity(intent);
                }
            });

            binding.etDiambilDari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), TujuanFinancialActivity.class);
                    startActivity(intent);
                }
            });

            binding.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            binding.btUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId = binding.rgStatusTransaksi.getCheckedRadioButtonId();
                    int total, nominal;


                    String JumlahPemasukan = NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlahPemasukan.getText().toString());
                    String TujuanPemasukan = binding.etTujuanPemasukan.getText().toString().trim();
                    String KategoriPemasukan = binding.etKategoriPemasukan.getText().toString().trim();
                    String TanggalPemasukan = binding.etTanggalPemasukan.getText().toString().trim();
                    String MetodeTransaksi = binding.etMetodeTransaksiPemasukan.getText().toString().trim();
                    String Catatan = binding.etCatatanPemasukan.getText().toString().trim();


                    String etJumlahP = NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlah.getText().toString());
                    String etDiambilDariP = binding.etDiambilDari.getText().toString().trim();
                    String etkategoriP = binding.etKategori.getText().toString().trim();
                    String etTglTransaksiP = binding.etTanggalTransaksi.getText().toString().trim();
                    String etMetodeTransaksiP = binding.etMetodeTransaksi.getText().toString().trim();
                    String etCatatanP = binding.etCatatanPengeluaran.getText().toString().trim();

                    if (selectedId == binding.rbLunas.getId()) {
                        st = binding.rbLunas.getText().toString().trim();
                    } else if (selectedId == binding.rbBelum.getId()) {
                        st = binding.rbBelum.getText().toString().trim();
                    }

                    //chek jenis transaksi
                    if (jenis_transaksi.equals("pemasukan")) {
                        //get nominal
                        nominal = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlahPemasukan.getText().toString()));

                        dbHelper.deleteCatatanKeuangan(id);
                        dbHelper.addPemasukan(JumlahPemasukan, TujuanPemasukan, KategoriPemasukan, TanggalPemasukan, MetodeTransaksi, Catatan);

                    } else {

                        //get nominal
                        nominal = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlah.getText().toString()));

                        dbHelper.deleteCatatanKeuangan(id);
                        dbHelper.addPengeluaran(etJumlahP, etDiambilDariP, etkategoriP,
                                etTglTransaksiP, etMetodeTransaksiP, etCatatanP, st);

                    }

                    //total dana terkumpul baru untuk update di rencana keuangan
                    total = dana_terkumpul - nominal;
                    AndLog.ShowLog("nominaltotal", String.valueOf(nominal));

                    //Update Status terpenuhi
                    if (target_dana == total) {
                        GlobalToast.ShowToast(UbahTransaksiActivity.this, "Selamat! Target Dana " + nama_transaksi + " berhasil terpenuhi.");
                        dbHelper.updateRencanaKeuanganLunas(String.valueOf(total), nama_transaksi);
                    }
                    //Update Progress Uang Rencana Keuangan Belum terpenuhi
                    dbHelper.updateRencanaKeuangan(String.valueOf(total), nama_transaksi);

                    finish();
                    GlobalToast.ShowToast(UbahTransaksiActivity.this, "Data Berhasil Diubah");


                }
            });
        }
    }
}