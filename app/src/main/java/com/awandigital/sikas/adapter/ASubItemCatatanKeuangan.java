package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.SubItemDataCatatanKeuanganBinding;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.MSubItemCatatanKeuanganMasuk;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class ASubItemCatatanKeuangan extends RecyclerView.Adapter<ASubItemCatatanKeuangan.ViewHolder> {
    private ArrayList<MSubItemCatatanKeuanganMasuk> arrayList;
    Context context;
    SQLiteDatabase mDatabase;

    public ASubItemCatatanKeuangan(ArrayList<MSubItemCatatanKeuanganMasuk> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SubItemDataCatatanKeuanganBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MSubItemCatatanKeuanganMasuk mPemasukan = arrayList.get(position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SubItemDataCatatanKeuanganBinding binding;

        public ViewHolder(SubItemDataCatatanKeuanganBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
