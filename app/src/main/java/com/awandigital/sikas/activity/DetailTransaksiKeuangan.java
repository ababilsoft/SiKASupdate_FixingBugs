package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityDetailTransaksiKeuanganBinding;
import com.awandigital.sikas.adapter.ADetailTransaksiKeuangan;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MCatatanKeuanganSemua;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;

public class DetailTransaksiKeuangan extends AppCompatActivity {
    ActivityDetailTransaksiKeuanganBinding binding;
    ADetailTransaksiKeuangan adapter;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTransaksiKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(DetailTransaksiKeuangan.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        setData();
        String id = getIntent().getStringExtra("id");
        Cursor cursor = dbHelper.getAllCatatanById(id);
        if (cursor.moveToNext()) {
            String jenis_transaksi = cursor.getString(7);
            if (jenis_transaksi.equals("pemasukan")) {
                binding.tvCost.setTextColor(R.color.success_400);
                binding.tvJenisTransaksi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_circle_up_g, 0, 0, 0);
            }
            String status_transaksi = cursor.getString(12);

            String tgl = cursor.getString(5);
            String nama_transaksi = cursor.getString(14);
            String kategori = cursor.getString(4);
            String nominal = cursor.getString(1);
            String catatan = cursor.getString(11);

            binding.tvJenisTransaksi.setText(jenis_transaksi);
            if (status_transaksi != null) {
                binding.tvStatusPelunasan.setText(status_transaksi);
            } else {
                binding.tvStatusPelunasan.setVisibility(View.GONE);
            }

            binding.tvTgl.setText(tgl);
            binding.tvNamaTransaksi.setText(nama_transaksi);
            binding.tvKategori.setText(kategori);
            binding.tvCost.setText("Rp" + DecimalsFormat.priceWithoutDecimal(nominal));
            binding.tvCatatan.setText(catatan);

        }

        binding.btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UbahTransaksiActivity.class);
//                intent.putExtra("jenisTransakasi", binding.tvJenisTransaksi.getText());
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

//    private void setData() {
//        binding.tvJenisTransaksi.setText("Pengeluaran");
//        binding.tvStatusPelunasan.setText("Lunas");
//        binding.tvTgl.setText("14 Januari 2022");
//        binding.tvKategori.setText("Beli Mackbook");
//        binding.tvCatatan.setText("Elektronik - ShopeePay");
//        binding.tvCost.setText("-Rp200.000");
//        binding.tvCatatan2.setText("-");
//    }

}