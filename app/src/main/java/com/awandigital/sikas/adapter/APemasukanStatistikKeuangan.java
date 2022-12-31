package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemListPemasukanBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class APemasukanStatistikKeuangan extends RecyclerView.Adapter<APemasukanStatistikKeuangan.ViewHolder> {
    private ArrayList<MPemasukan> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public APemasukanStatistikKeuangan(ArrayList<MPemasukan> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemListPemasukanBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MPemasukan mPemasukan = arrayList.get(position);
        holder.binding.tvKategori.setText(mPemasukan.getKategori());
        holder.binding.tvNominal.setText("Rp" + DecimalsFormat.priceWithoutDecimal(mPemasukan.getNominal()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListPemasukanBinding binding;

        public ViewHolder(ItemListPemasukanBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
