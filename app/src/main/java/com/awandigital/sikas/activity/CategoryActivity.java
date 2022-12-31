package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ACatatanKeuanganSemua;
import com.awandigital.sikas.adapter.AKategori;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.databinding.ActivityCategoryBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKategori;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;


public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    AKategori aKategori;
    Context mActivity;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    ArrayList<MKategori> data = new ArrayList<>();

    public BroadcastReceiver receiveUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String value = intent.getStringExtra("kategori");
            if (value.equals("update")) {
                data.clear();
                listKategori();
            }

        }
    };

    public BroadcastReceiver Delete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String value = intent.getStringExtra("kategori");
            if (value.equals("delete")) {
                data.clear();
                listKategori();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mActivity = getApplicationContext();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(CategoryActivity.this);

        binding.rvKategoriTambahan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvKategoriTambahan.setLayoutManager(layoutManager);
        listKategori();

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveUpdate,
                new IntentFilter("update-kategori"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(Delete,
                new IntentFilter("delete-kategori"));

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        CategoryActivity.this, R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_tambah_kategori);
                EditText etKategori = bsd.findViewById(R.id.etKategori);
                Button btSimpan = bsd.findViewById(R.id.btSimpan);

                Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String getKategori = etKategori.getText().toString().trim();
                        pDialog.show();
                        dbHelper.addKategori(getKategori);
                        pDialog.dismiss();
                        bsd.dismiss();
                        data.clear();
                        listKategori();
                        GlobalToast.ShowToast(mActivity, "Kategori " + getKategori + " Berhasil Ditambahkan");
                    }
                });
                bsd.show();
            }
        });


        binding.tvMakandanMinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV1");
                intent.putExtra("v1", binding.tvMakandanMinum.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

        binding.tvTransportasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV2");
                intent.putExtra("v2", binding.tvTransportasi.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

        binding.tvKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV3");
                intent.putExtra("v3", binding.tvKesehatan.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

        binding.tvBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV4");
                intent.putExtra("v4", binding.tvBelanja.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

        binding.tvPendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV5");
                intent.putExtra("v5", binding.tvPendidikan.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

        binding.tvAsuransi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("getV6");
                intent.putExtra("v6", binding.tvAsuransi.getText());
                LocalBroadcastManager.getInstance(CategoryActivity.this).sendBroadcast(intent);
                AndLog.ShowLog("LOG_NAMA_KATEGORI", "get");
                CategoryActivity.this.finish();
            }
        });

    }

    private void listKategori() {
        Cursor cursorproduct = dbHelper.getAllKategori();
        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MKategori(
                        cursorproduct.getInt(0), // id
                        cursorproduct.getString(1), // nama kategori
                        cursorproduct.getString(2) // created at

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("KategoriActivity", "No items Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aKategori = new AKategori(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvKategoriTambahan.setAdapter(aKategori);

    }

    private ArrayList<MKategori> kategoriTambahan() {
        ArrayList<MKategori> arrayList = new ArrayList<>();
        arrayList.add(new MKategori(1, "Gaji", ""));
        arrayList.add(new MKategori(2, "Bonus Freelance", ""));
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}