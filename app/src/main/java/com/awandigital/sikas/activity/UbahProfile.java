package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.email;
import static com.awandigital.sikas.db.DatabaseHelper.image_profile;
import static com.awandigital.sikas.db.DatabaseHelper.nama;
import static com.awandigital.sikas.db.DatabaseHelper.password;
import static com.awandigital.sikas.db.DatabaseHelper.username;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityUbahProfileBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class UbahProfile extends AppCompatActivity implements IPickResult {

    ActivityUbahProfileBinding binding;
    SessionManager sessionManager;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    private static final int RESULT_LOAD_IMG = 1002, RESULT_LOAD_IMG_2 = 1003;

    Bitmap bitmap, decodedLogo, decodedLogoStruk;
    int bitmap_size = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUbahProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btUbahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(UbahProfile.this,
                        R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_ubah_profil_berhasil);
                Button btKembali = bsd.findViewById(R.id.btKembali);
                btKembali.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                        finish();
                    }
                });
                bsd.show();
            }
        });

        pDialog = new OwnProgressDialog(UbahProfile.this);
        sessionManager = new SessionManager(UbahProfile.this);
        dbHelper = new DatabaseHelper(UbahProfile.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        getDetailAkun();

        binding.tvBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btUbahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpanDataUpdate();
            }
        });

        binding.ivProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(UbahProfile.this);
            }
        });

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setToImageViewLogo(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 60, bytes);
        decodedLogo = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        binding.ivProfil.setImageBitmap(decodedLogo);
//        binding.ivProfil.setBackgroundResource(R.drawable.rounded_imageview);
//        binding.ivProfil.setVisibility(View.GONE);
    }

    private void setToImageViewLogoStruk(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 60, bytes);
        decodedLogoStruk = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        binding.ivProfil.setImageBitmap(decodedLogoStruk);
//        binding.ivProfil.setBackgroundResource(R.drawable.rounded_imageview);
//        icIconCamera2.setVisibility(View.GONE);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private static byte[] imageViewToByte(ImageView image) {

        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == RESULT_LOAD_IMG && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                // update user profile image in db
                dbHelper.updateImageProfile(getBitmapAsByteArray(bitmap));
                getDetailAkun();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                GlobalToast.ShowToast(UbahProfile.this, "Kesalahan Saat Reload Gambar");
            }

        }
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {
                final Uri imageUri = r.getUri();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                // update user profile image in db
                dbHelper.updateImageProfile(getBitmapAsByteArray(bitmap));
                getDetailAkun();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                GlobalToast.ShowToast(UbahProfile.this, "Kesalahan Saat Reload Gambar");
            }

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void SimpanDataUpdate() {
        String Namal, Username, Pwd, Email;
        Namal = binding.etNama.getText().toString().trim();
        Username = binding.etUsername.getText().toString().trim();
        Pwd = Objects.requireNonNull(binding.tilPassword.getText()).toString().trim();
        Email = binding.etEmail.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(nama, Namal);
        values.put(username, Username);
        values.put(password, Pwd);
        values.put(email, Email);

        ShowDialog(values, Email);
    }

    private void ShowDialog(ContentValues values, String getEmail) {
        final Dialog dialog = new Dialog(UbahProfile.this);
        dialog.setContentView(R.layout.layout_konfirmasi_dialog);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvKeterangan);
        tvMessage.setText("Anda Yakin Merubah Data Profile ?");
        TextView tvBatal = (TextView) dialog.findViewById(R.id.tvBatal);
        Button btKonfirmasi = (Button) dialog.findViewById(R.id.btKonfirmasi);
        btKonfirmasi.setText("Iya");
        btKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.updateProfil(values, getEmail);
                finish();
                GlobalToast.ShowToast(UbahProfile.this, "Data Berhasil Dirubah");
                dialog.dismiss();
            }
        });
        tvBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("Range")
    private void getDetailAkun() {
        Cursor cursor = dbHelper.getAllUser();

        if (cursor.moveToFirst()) {
            String getNamal = cursor.getString(cursor.getColumnIndex(nama));
            String getUsername = cursor.getString(cursor.getColumnIndex(username));
            String getEmail = cursor.getString(cursor.getColumnIndex(email));
            String getPwd = cursor.getString(cursor.getColumnIndex(password));
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(image_profile));

            binding.etNama.setText(getNamal);
            binding.etUsername.setText(getUsername);
            binding.etEmail.setText(getEmail);
            binding.tilPassword.setText(getPwd);
            if (imgByte != null) {
                if (imgByte.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    binding.ivProfil.setImageBitmap(getResizedBitmap(bmp, 512));
                }
            }

        }
    }
}