package com.awandigital.sikas.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.awandigital.sikas.databinding.FragmentTransaksiKategoriPengeluaranBinding;


public class TransaksiKategoriPengeluaranFragment extends Fragment {
    FragmentTransaksiKategoriPengeluaranBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransaksiKategoriPengeluaranBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}