package com.awandigital.sikas.fragment.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityDetailLaporanKeuanganBinding;

public class DetailLaporanKeuangan extends AppCompatActivity {
    ActivityDetailLaporanKeuanganBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailLaporanKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}