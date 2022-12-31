package com.awandigital.sikas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityTargetKeuanganBinding;

public class TargetKeuangan extends AppCompatActivity {
    ActivityTargetKeuanganBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTargetKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}