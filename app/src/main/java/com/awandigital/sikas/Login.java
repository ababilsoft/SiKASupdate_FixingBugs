package com.awandigital.sikas;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.email;
import static com.awandigital.sikas.db.DatabaseHelper.nama;
import static com.awandigital.sikas.db.DatabaseHelper.username;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awandigital.sikas.databinding.ActivityLoginBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;

import java.util.Objects;

public class Login extends AppCompatActivity {
    SessionManager sessionManager;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    ActivityLoginBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(Login.this);
        pDialog = new OwnProgressDialog(Login.this);
        dbHelper = new DatabaseHelper(this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.btMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogin();
            }
        });

        binding.btDaftarBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Login.this, Registrasi.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("Range")
    private void setLogin() {

        String username = binding.etUsername.getText().toString().trim();
        String pwd = Objects.requireNonNull(binding.tilPassword.getText()).toString().trim();

        Cursor cursor = dbHelper.login(username, pwd);

        if (cursor.moveToFirst()) {
            String getNamal = cursor.getString(cursor.getColumnIndex(nama));
            String getEmail = cursor.getString(cursor.getColumnIndex(email));

            sessionManager.createLoginSession(getEmail, getNamal);

            GlobalToast.ShowToast(Login.this, "Selamat Datang Kembali, " + getNamal);
            intent = new Intent(this, Home.class);
            startActivity(intent);
        } else {
            GlobalToast.ShowToast(Login.this, "Username/Password salah, ulangi lagi. (Huruf Kapital Berpengaruh)");
        }


//        cursor.close();

    }

}