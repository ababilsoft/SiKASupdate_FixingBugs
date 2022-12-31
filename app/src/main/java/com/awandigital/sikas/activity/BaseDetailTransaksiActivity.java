package com.awandigital.sikas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.databinding.ActivityBaseDetailTransaksiBinding;
import com.awandigital.sikas.fragment.transaksi.catatan.CatatanKeuanganFragment;
import com.awandigital.sikas.fragment.transaksi.catatan.CatatanKeuanganTambahFragment;
import com.awandigital.sikas.fragment.transaksi.rencana.RencanaKeuanganFragment;


public class BaseDetailTransaksiActivity extends AppCompatActivity {
    ActivityBaseDetailTransaksiBinding binding;

    RencanaKeuanganFragment rencana = new RencanaKeuanganFragment();
    CatatanKeuanganFragment catatan = new CatatanKeuanganFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseDetailTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String from = intent.getStringExtra("prom");

        if (from.equals("1")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_detail, rencana);
            transaction.disallowAddToBackStack();
            transaction.commit();
            binding.toolbarTitle.setText("Rencana Keuangan");
            binding.iVChart.setVisibility(View.GONE);
        } else if (from.equals("2")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_detail, catatan);
            transaction.disallowAddToBackStack();
            transaction.commit();
            binding.toolbarTitle.setText("Catatan Keuangan");
        } else if (from.equals("3")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_detail, new CatatanKeuanganTambahFragment());
            transaction.disallowAddToBackStack();
            transaction.commit();
            binding.iVChart.setVisibility(View.GONE);
            binding.toolbarTitle.setText("Tambah Catatan");
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_detail, rencana);
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

        binding.iVChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}