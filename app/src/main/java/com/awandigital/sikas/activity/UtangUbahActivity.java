package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.AKontakTerbaru;
import com.awandigital.sikas.adapter.ATujuanFinancial;
import com.awandigital.sikas.databinding.ActivityUtangUbahBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKategori;
import com.awandigital.sikas.model.MKontakTerbaru;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.OwnProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class UtangUbahActivity extends AppCompatActivity {
    ActivityUtangUbahBinding binding;
    AKontakTerbaru aKontakTerbaru;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String[] nama_bulan;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    String idHp, nama, nominal, tgl_transaksi, jenis_transaksi, catatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUtangUbahBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(UtangUbahActivity.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(UtangUbahActivity.this);

        nama_bulan = getResources().getStringArray(R.array.month);

        idHp = getIntent().getStringExtra("id_hp");

        getDetailData(idHp);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.rvKontakTerbaru.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(UtangUbahActivity.this, 1,
                GridLayoutManager.HORIZONTAL, false);
        binding.rvKontakTerbaru.setLayoutManager(layoutManager);

        binding.etJumlah.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlah));
        listKontakTerbaru();

        binding.etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UtangUbahActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                binding.etTanggal.setText(String.valueOf(dayOfMonth + " " + nama_bulan[monthOfYear] + " " + year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        binding.btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @SuppressLint({"Range", "SetTextI18n"})
    private void getDetailData(String idHp) {
        Cursor cursor = dbHelper.getAllHutangDetailById(idHp);
        if (cursor.moveToFirst()) {
            jenis_transaksi = cursor.getString(cursor.getColumnIndex("jenis_transaksi"));
            if (jenis_transaksi.equals("Utang")) {
                nama = cursor.getString(cursor.getColumnIndex("dipinjamkan_oleh"));
                binding.lineUtang.setVisibility(View.VISIBLE);
                binding.linePiutang.setVisibility(View.GONE);
                binding.etDipinjamkanOleh.setText(nama);
            } else if (jenis_transaksi.equals("Piutang")) {
                nama = cursor.getString(cursor.getColumnIndex("dipinjamkan_ke"));
                binding.linePiutang.setVisibility(View.VISIBLE);
                binding.lineUtang.setVisibility(View.GONE);
                binding.etDipinjamkanKe.setText(nama);
            }
            tgl_transaksi = cursor.getString(cursor.getColumnIndex("tgl_transaksi"));

            nominal = cursor.getString(cursor.getColumnIndex("nominal"));
            catatan = cursor.getString(cursor.getColumnIndex("catatan"));

            binding.etTanggal.setText(tgl_transaksi);
            binding.etJumlah.setText(DecimalsFormat.priceWithoutDecimal(nominal));
            binding.etCatatan.setText(catatan);

        }
    }

    private void listKontakTerbaru() {
        Cursor cursorproduct = dbHelper.getAllKontak();

        ArrayList<MKontakTerbaru> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MKontakTerbaru(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(1) // nama

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("LOG_KONTAK", "No items Found in database");
        }
        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aKontakTerbaru = new AKontakTerbaru(data, UtangUbahActivity.this, mDatabase);

        //adding the adapter to listview
        binding.rvKontakTerbaru.setAdapter(aKontakTerbaru);

    }

}