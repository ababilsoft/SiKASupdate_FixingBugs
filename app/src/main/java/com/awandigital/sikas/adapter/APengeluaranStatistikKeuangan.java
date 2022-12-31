package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemListPengeluaranBinding;
import com.awandigital.sikas.model.MPengeluaran;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class APengeluaranStatistikKeuangan extends RecyclerView.Adapter<APengeluaranStatistikKeuangan.ViewHolder> {
    private ArrayList<MPengeluaran> arrayList;
    Context context;
    SQLiteDatabase mDatabase;

    public APengeluaranStatistikKeuangan(ArrayList<MPengeluaran> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemListPengeluaranBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MPengeluaran mPengeluaran = arrayList.get(position);
        holder.pengeluaranBinding.tvKategori.setText(mPengeluaran.getKategori());
        holder.pengeluaranBinding.tvNominal.setText("Rp" + DecimalsFormat.priceWithoutDecimal(mPengeluaran.getNominal()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListPengeluaranBinding pengeluaranBinding;

        public ViewHolder(ItemListPengeluaranBinding itemView) {
            super(itemView.getRoot());
            this.pengeluaranBinding = itemView;
        }
    }
}
