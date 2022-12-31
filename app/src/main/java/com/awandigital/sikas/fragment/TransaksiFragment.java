package com.awandigital.sikas.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.BaseDetailTransaksiActivity;
import com.awandigital.sikas.activity.TujuanFinancialActivity;
import com.awandigital.sikas.adapter.ARencanaKeuanganAll;
import com.awandigital.sikas.adapter.ATujuanFinancial;
import com.awandigital.sikas.databinding.FragmentTransaksiBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.MPengeluaran;
import com.awandigital.sikas.model.MTujuanFinansial;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.adapter.APemasukanCatatanKeuangan;
import com.awandigital.sikas.adapter.APengeluaranCatatanKeuangan;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.fragment.transaksi.LaporanKeuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaksiFragment extends Fragment {
    private FragmentTransaksiBinding binding;
    AdapterRencanaKeuangan adapterRencanaKeuangan;
    APemasukanCatatanKeuangan aPemasukanCatatanKeuangan;
    APengeluaranCatatanKeuangan aPengeluaranCatatanKeuangan;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    Activity mActivity;
    SessionManager sessionManager;
    int saldoKU = 0, saldoFP = 0, grandSaldo = 0;
    ArrayList<MPemasukan> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransaksiBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        sessionManager = new SessionManager(mActivity);

        binding.tvNama.setText("Hi, " + sessionManager.getNama());

        binding.rvRencanaKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.HORIZONTAL, false);
        binding.rvRencanaKeuangan.setLayoutManager(layoutManager);
        listDataRencanaKeuangan();

        binding.rvPemasukanCatatanKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvPemasukanCatatanKeuangan.setLayoutManager(layoutManager2);
        listDataPemasukanCatatanKeuangan();

        binding.rvPengeluaranCatatanKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager3 = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvPengeluaranCatatanKeuangan.setLayoutManager(layoutManager3);
        listDataPengeluaranCatatanKeuangan();


        binding.btLihatSemuaRencana.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailTransaksiActivity.class);
                i.putExtra("prom", "1");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.btLihatSemuaCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailTransaksiActivity.class);
                i.putExtra("prom", "2");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailTransaksiActivity.class);
                i.putExtra("prom", "3");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.btPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llPemasukan.setVisibility(View.VISIBLE);
                binding.llPengeluaran.setVisibility(View.GONE);
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_blue));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_white));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_100));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_300));
            }
        });

        binding.btPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llPemasukan.setVisibility(View.GONE);
                binding.llPengeluaran.setVisibility(View.VISIBLE);
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_white));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_blue));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_300));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_100));
            }
        });

        binding.btLaporanKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LaporanKeuangan.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void listDataPengeluaranCatatanKeuangan() {
        Cursor cursorproduct = dbHelper.getAllPengeluaran();

        ArrayList<MPengeluaran> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPengeluaran(
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(2), // diambil dari
                        cursorproduct.getString(3), // kategori
                        cursorproduct.getString(4) // tgl transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalHarianPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("listDataPengeluaranCatatanKeuangan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPengeluaranCatatanKeuangan = new APengeluaranCatatanKeuangan(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvPengeluaranCatatanKeuangan.setAdapter(aPengeluaranCatatanKeuangan);
    }

    private void listDataPemasukanCatatanKeuangan() {
        Cursor cursorproduct = dbHelper.getAllPemasukan();

        ArrayList<MPemasukan> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPemasukan(
                        cursorproduct.getString(4), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(3), // kategori
                        cursorproduct.getString(1) // nominal

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalHarian.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("TransaksiFragment.listpemasukan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanCatatanKeuangan = new APemasukanCatatanKeuangan(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvPemasukanCatatanKeuangan.setAdapter(aPemasukanCatatanKeuangan);
    }

    private void listDataRencanaKeuangan() {

        Cursor cursorproduct = dbHelper.getAllRencanaKeuangan();

        ArrayList<rencana_keuangan> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new rencana_keuangan(
                        cursorproduct.getString(0), //
                        cursorproduct.getString(1), // nama rencana keuangan
                        cursorproduct.getString(3), // target dana
                        cursorproduct.getString(2)// dana terkumpul

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("listDataRencanaKeuangan", "No items Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        adapterRencanaKeuangan = new AdapterRencanaKeuangan(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvRencanaKeuangan.setAdapter(adapterRencanaKeuangan);

    }

    private void pieChart() {
        PieChart pieChart = binding.chartTransaksi;

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("saldoumum", saldoKU);
        typeAmountMap.put("saldofp", saldoFP);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#0096f4"));
        colors.add(Color.parseColor("#27ae61"));

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet()) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieChart.getLegend().setEnabled(false);
        pieData.setDrawValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onResume() {
        super.onResume();

        listDataPemasukanCatatanKeuangan();
        listDataPengeluaranCatatanKeuangan();

        adapterRencanaKeuangan.notifyDataSetChanged();
        aPemasukanCatatanKeuangan.notifyDataSetChanged();
        aPengeluaranCatatanKeuangan.notifyDataSetChanged();

        Cursor cursor = dbHelper.dataKasUmum();

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            if (cursor.getString(cursor.getColumnIndex("nominal")) == null) {
                saldoKU = 0;
            } else {
                saldoKU = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nominal")));
            }
            binding.tvSaldoKasUmum.setText("Rp." + DecimalsFormat.priceWithoutDecimal(String.valueOf(saldoKU)));

        }


        Cursor cursor1 = dbHelper.dataSaldoFp();

        if (cursor1.moveToFirst() && cursor1.getCount() > 0) {
            if (cursor1.getString(cursor1.getColumnIndex("totalfp")) == null) {
                saldoFP = 0;
            } else {

                saldoFP = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("totalfp")));
            }
            binding.tvSeluruhSaldoFP.setText("Rp." + DecimalsFormat.priceWithoutDecimal(String.valueOf(saldoFP)));
        }

        grandSaldo = saldoKU + saldoFP;
        binding.tvSaldo.setText(DecimalsFormat.priceWithoutDecimal(String.valueOf(grandSaldo)));

        pieChart();
        listDataRencanaKeuangan();

    }
}