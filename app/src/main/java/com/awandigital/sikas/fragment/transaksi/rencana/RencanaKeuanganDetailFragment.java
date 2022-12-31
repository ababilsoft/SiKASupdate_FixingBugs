package com.awandigital.sikas.fragment.transaksi.rencana;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.FragmentRencanaKeuanganDetailBinding;


public class RencanaKeuanganDetailFragment extends Fragment {
    FragmentRencanaKeuanganDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRencanaKeuanganDetailBinding.inflate(inflater, container, false);

        binding.btLaporanKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_detail, new RencanaKeuanganGrafikFragment(), "");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        binding.tvValueTraget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_detail, new RencanaKeuanganTargetFragment(), "");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return binding.getRoot();
    }
}