package com.awandigital.sikas.fragment.transaksi.rencana;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.awandigital.sikas.databinding.FragmentRencanaKeuanganGrafikBinding;
import com.github.mikephil.charting.charts.LineChart;

public class RencanaKeuanganGrafikFragment extends Fragment {
    FragmentRencanaKeuanganGrafikBinding binding;
    LineChart mChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRencanaKeuanganGrafikBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void lineChart() {
        mChart = binding.lineChartGrafik;
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
    }

}