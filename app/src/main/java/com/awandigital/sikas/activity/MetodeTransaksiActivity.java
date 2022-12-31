package com.awandigital.sikas.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityMetodeTransaksiBinding;

public class MetodeTransaksiActivity extends AppCompatActivity {
    ActivityMetodeTransaksiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMetodeTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.tvTunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("pilih-pembayaran");
                intent.putExtra("pembayaran", "Tunai");
                LocalBroadcastManager.getInstance(MetodeTransaksiActivity.this).sendBroadcast(intent);
                ((MetodeTransaksiActivity) MetodeTransaksiActivity.this).finish();
            }
        });

        binding.tvDompetDigital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("pilih-pembayaran");
                intent.putExtra("pembayaran", "Dompet Digital");
                LocalBroadcastManager.getInstance(MetodeTransaksiActivity.this).sendBroadcast(intent);
                ((MetodeTransaksiActivity) MetodeTransaksiActivity.this).finish();
            }
        });

        binding.tvTransferBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("pilih-pembayaran");
                intent.putExtra("pembayaran", "Transfer Bank");
                LocalBroadcastManager.getInstance(MetodeTransaksiActivity.this).sendBroadcast(intent);
                ((MetodeTransaksiActivity) MetodeTransaksiActivity.this).finish();
            }
        });


    }
}