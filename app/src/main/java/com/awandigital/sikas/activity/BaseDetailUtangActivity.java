package com.awandigital.sikas.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.awandigital.sikas.R;
import com.awandigital.sikas.fragment.utang.UtangPiutangTambahFragment;
import com.awandigital.sikas.fragment.utang.UtangRiwayatFragment;
import com.awandigital.sikas.fragment.utang.UtangUbahFragment;


public class BaseDetailUtangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_detail_utang);

        Intent intent = getIntent();
        String pindah = intent.getStringExtra("pindah");

        if (pindah.equals("1")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_utang, new UtangRiwayatFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();
        } else if (pindah.equals("2")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_utang, new UtangPiutangTambahFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_utang, new UtangUbahFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}