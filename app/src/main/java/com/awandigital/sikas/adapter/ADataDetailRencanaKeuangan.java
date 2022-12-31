package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemDataDetailRencanaKeuanganBinding;
import com.awandigital.sikas.model.MDetailDataRencanaKeuangan;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class ADataDetailRencanaKeuangan extends RecyclerView.Adapter<ADataDetailRencanaKeuangan.ViewHolder> {
    private ArrayList<MDetailDataRencanaKeuangan> arrayList;
    Context context;
    SQLiteDatabase mDatabase;

    public ADataDetailRencanaKeuangan(ArrayList<MDetailDataRencanaKeuangan> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    public void filter(String text, ArrayList<MDetailDataRencanaKeuangan> arrayListCopy) {
        arrayList.clear();
        if (text.isEmpty()) {
            arrayList.addAll(arrayListCopy);
        } else {
            text = text.toLowerCase();
            for (MDetailDataRencanaKeuangan item : arrayListCopy) {

                if (item.getKategori().toLowerCase().contains(text)) {
                    arrayList.add(item);
                }

                if (item.getTglTransaksi().toLowerCase().contains(text)) {
                    arrayList.add(item);
                }
            }
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDataDetailRencanaKeuanganBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvKategori.setText(arrayList.get(position).getKategori());
        holder.binding.tvCatatan.setText(arrayList.get(position).getTglTransaksi());
        if (arrayList.get(position).getJenisTransaksi().equals("pemasukan")) {
            holder.binding.tvCostMasuk.setText("Rp" + DecimalsFormat.priceWithoutDecimal(arrayList.get(position).getNominal()));
            holder.binding.tvCostKeluar.setText("-");
        } else if (arrayList.get(position).getJenisTransaksi().equals("pengeluaran")) {
            holder.binding.tvCostMasuk.setText("-");
            holder.binding.tvCostKeluar.setText("Rp" + DecimalsFormat.priceWithoutDecimal(arrayList.get(position).getNominal()));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDataDetailRencanaKeuanganBinding binding;

        public ViewHolder(ItemDataDetailRencanaKeuanganBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
