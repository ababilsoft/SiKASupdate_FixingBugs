package com.awandigital.sikas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityBaseDetailTambahCatatanBinding;
import com.awandigital.sikas.fragment.TransaksiKategoriPengeluaranFragment;


public class BaseDetailTambahCatatanActivity extends AppCompatActivity {
    ActivityBaseDetailTambahCatatanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBaseDetailTambahCatatanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String pindah = intent.getStringExtra("input");

        if (pindah.equals("1")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_input, new TransaksiKategoriPengeluaranFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();

        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_input, new TransaksiKategoriPengeluaranFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}