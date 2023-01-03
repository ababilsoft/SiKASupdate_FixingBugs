package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemCatatanKeuanganMasukBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MCatatanKeuanganMasuk;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class ACatatanKeuanganPemasukan extends RecyclerView.Adapter<ACatatanKeuanganPemasukan.ViewHolder> {
    private ArrayList<MCatatanKeuanganMasuk> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public ACatatanKeuanganPemasukan(ArrayList<MCatatanKeuanganMasuk> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCatatanKeuanganMasukBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCatatanKeuanganMasuk mPemasukan = arrayList.get(position);
        if (mPemasukan.getJenisTransaksi().equals("pemasukan")) {
            holder.binding.tvCostMasuk.setText("+ Rp." + DecimalsFormat.priceWithoutDecimal(mPemasukan.getNominal()));
            holder.binding.tvKategori.setText(mPemasukan.getKategori());
            holder.binding.tvTgl.setText(mPemasukan.getTglTransaksi());
            holder.binding.tvTujuanFinansial.setText(mPemasukan.getTujuanFinansial());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCatatanKeuanganMasukBinding binding;

        public ViewHolder(ItemCatatanKeuanganMasukBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
