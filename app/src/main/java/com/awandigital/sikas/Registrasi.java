package com.awandigital.sikas;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.email;
import static com.awandigital.sikas.db.DatabaseHelper.nama;
import static com.awandigital.sikas.db.DatabaseHelper.password;
import static com.awandigital.sikas.db.DatabaseHelper.status_login;
import static com.awandigital.sikas.db.DatabaseHelper.username;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.databinding.ActivityRegistrasiBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Registrasi extends AppCompatActivity {
    SimpleDateFormat formatlengkap_beta = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", new Locale("id", "ID"));
    Calendar c = Calendar.getInstance();
    ActivityRegistrasiBinding binding;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    String dates = formatlengkap_beta.format(c.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrasiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(Registrasi.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(Registrasi.this);
        sessionManager = new SessionManager(Registrasi.this);
        binding.btDaftarBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();

                String getNamaL = binding.etNamaLengkap.getText().toString().trim();
                String getEmail = binding.etEmail.getText().toString().trim();
                String getUsername = binding.etUsername.getText().toString().trim();
                String getPwd = Objects.requireNonNull(binding.tilPassword.getText()).toString().trim();

                ContentValues values = new ContentValues();
                values.put(nama, getNamaL);
                values.put(username, getUsername);
                values.put(password, getPwd);
                values.put(email, getEmail);
                values.put(status_login, "1");
                values.put("updated_at", dates);

                sessionManager.createLoginSession(getEmail, getNamaL);
                dbHelper.addUser(values);
                AndLog.ShowLog("tagsReg: ", getNamaL + getEmail + getUsername + getPwd);
                GlobalToast.ShowToast(Registrasi.this, "Registrasi atas nama " + getNamaL + " Berhasil!");
                Intent intent = new Intent(Registrasi.this, Home.class);
                startActivity(intent);
                pDialog.dismiss();
            }
        });
    }
}