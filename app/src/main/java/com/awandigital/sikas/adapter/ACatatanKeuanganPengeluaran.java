package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemCatatanKeuanganKeluarBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MCatatanKeuanganKeluar;
import com.awandigital.sikas.model.MPengeluaran;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class ACatatanKeuanganPengeluaran extends RecyclerView.Adapter<ACatatanKeuanganPengeluaran.ViewHolder> {
    private ArrayList<MCatatanKeuanganKeluar> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public ACatatanKeuanganPengeluaran(ArrayList<MCatatanKeuanganKeluar> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCatatanKeuanganKeluarBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCatatanKeuanganKeluar mPengeluaran = arrayList.get(position);
        if (mPengeluaran.getJenisTransaksi().equals("pengeluaran")) {
            holder.binding.tvKategori.setText(mPengeluaran.getKategori());
            holder.binding.tvCostKeluar.setText("- Rp" + DecimalsFormat.priceWithoutDecimal(mPengeluaran.getNominal()));
            holder.binding.tvDiambilDari.setText(mPengeluaran.getDiambilDari());
            holder.binding.tvtglTransaksi.setText(mPengeluaran.getTglTransaksi());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCatatanKeuanganKeluarBinding binding;

        public ViewHolder(ItemCatatanKeuanganKeluarBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
