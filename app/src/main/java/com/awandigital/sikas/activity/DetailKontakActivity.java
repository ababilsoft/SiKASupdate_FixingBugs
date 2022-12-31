package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.image_profile;
import static com.awandigital.sikas.utils.customImage.getBitmapAsByteArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityDetailKontakBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.customImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DetailKontakActivity extends AppCompatActivity implements IPickResult {
    ActivityDetailKontakBinding binding;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    String id;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailKontakBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(DetailKontakActivity.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        id = getIntent().getStringExtra("id");

        getDetailAkun();

        binding.ivAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(DetailKontakActivity.this);
            }
        });

//        binding.btHapusKontak.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dbHelper.deleteKontak(id);
//                GlobalToast.ShowToast(DetailKontakActivity.this, "Data berhasil dihapus");
//                finish();
//            }
//        });

        binding.btUpdateKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = binding.etNamaKontak.getText().toString();
                String noHp = binding.etNoHp.getText().toString();
                String alamat = binding.etAlamat.getText().toString();
                if (nama.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
                    GlobalToast.ShowToast(DetailKontakActivity.this, "Data tidak boleh kosong");
                } else {
                    dbHelper.updateKontakById(id, nama, noHp, alamat);
                    GlobalToast.ShowToast(DetailKontakActivity.this, "Data berhasil diubah");
                    finish();
                }
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @SuppressLint("Range")
    void getDetailAkun() {
        Cursor cursor1 = dbHelper.getDetailKontakById(id);
        if (cursor1.moveToFirst()) {
            binding.etNamaKontak.setText(cursor1.getString(cursor1.getColumnIndex("nama_kontak")));
            binding.etNoHp.setText(cursor1.getString(cursor1.getColumnIndex("no_hp")));
            binding.etAlamat.setText(cursor1.getString(cursor1.getColumnIndex("alamat")));
            byte[] imgByte = cursor1.getBlob(cursor1.getColumnIndex("image_profile"));
            if (imgByte != null) {
                if (imgByte.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    binding.ivAkun.setImageBitmap(customImage.getResizedBitmap(bmp, 512));
                }
            }
//            nohpkontak = cursor1.getString(cursor1.getColumnIndex("no_hp"));
        }
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {
                final Uri imageUri = r.getUri();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                // update user profile image in db
                dbHelper.updateKontakImage(getBitmapAsByteArray(bitmap));
                getDetailAkun();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                GlobalToast.ShowToast(DetailKontakActivity.this, "Kesalahan Saat Reload Gambar");
            }

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}