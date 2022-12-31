package com.awandigital.sikas.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemTransaksiCatatanKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.MSubItemCatatanKeuanganMasuk;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;

import java.util.ArrayList;

public class APemasukanCatatanKeuangan extends RecyclerView.Adapter<APemasukanCatatanKeuangan.ViewHolder> {
    private ArrayList<MPemasukan> arrayList;

    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public APemasukanCatatanKeuangan(ArrayList<MPemasukan> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTransaksiCatatanKeuanganBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MPemasukan mPemasukan = arrayList.get(position);
        holder.keuanganBinding.tvTgl.setText(mPemasukan.getTglTransaksi());
        holder.keuanganBinding.tvTujuanFinansial.setText(mPemasukan.getTujuanFinansial());
        holder.keuanganBinding.tvKategori.setText(mPemasukan.getKategori());
        holder.keuanganBinding.tvNominal.setText("+Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(mPemasukan.getNominal())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransaksiCatatanKeuanganBinding keuanganBinding;


        public ViewHolder(ItemTransaksiCatatanKeuanganBinding itemView) {
            super(itemView.getRoot());
            this.keuanganBinding = itemView;
            context = itemView.getRoot().getContext();
            dbHelper = new DatabaseHelper(context);

        }

    }
}
