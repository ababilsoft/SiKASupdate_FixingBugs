package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemCatatanKeuanganKeluarBinding;
import com.awandigital.sikas.databinding.ItemTransaksiCatatanKeuanganBinding;
import com.awandigital.sikas.model.MPengeluaran;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class APengeluaranCatatanKeuangan extends RecyclerView.Adapter<APengeluaranCatatanKeuangan.ViewHolder> {
    private ArrayList<MPengeluaran> arrayList;
    Context context;
    SQLiteDatabase mDatabase;

    public APengeluaranCatatanKeuangan(ArrayList<MPengeluaran> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCatatanKeuanganKeluarBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MPengeluaran mPengeluaran = arrayList.get(position);
        holder.keuanganBinding.tvKategori.setText(mPengeluaran.getKategori());
        holder.keuanganBinding.tvCostKeluar.setText("-Rp" + DecimalsFormat.priceWithoutDecimal(mPengeluaran.getNominal()));
        holder.keuanganBinding.tvDiambilDari.setText(mPengeluaran.getDiambilDari());
        holder.keuanganBinding.tvtglTransaksi.setText(mPengeluaran.getTglTransaksi());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCatatanKeuanganKeluarBinding keuanganBinding;

        public ViewHolder(ItemCatatanKeuanganKeluarBinding itemView) {
            super(itemView.getRoot());
            this.keuanganBinding = itemView;
        }
    }
}
