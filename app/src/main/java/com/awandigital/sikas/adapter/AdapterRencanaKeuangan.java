package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.activity.DetailRencanaKeuanganActivity;
import com.awandigital.sikas.databinding.ItemTransaksiRencanaKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.DecimalsFormat;

import java.util.ArrayList;

public class AdapterRencanaKeuangan extends RecyclerView.Adapter<AdapterRencanaKeuangan.ViewHolder> {

    private ArrayList<rencana_keuangan> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public AdapterRencanaKeuangan(ArrayList<rencana_keuangan> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTransaksiRencanaKeuanganBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        rencana_keuangan rencana_keuangan = arrayList.get(position);
        holder.binding.tvTujuan.setText(rencana_keuangan.getTujuan());
        if (rencana_keuangan.getDanaTerkumpul() == null) {
            holder.binding.tvDanaTerkumpul.setText("Rp." + DecimalsFormat.priceWithoutDecimal("0"));
        } else {
            holder.binding.tvDanaTerkumpul.setText("Rp." + DecimalsFormat.priceWithoutDecimal(rencana_keuangan.getDanaTerkumpul()));
        }

        holder.binding.tvTargetDana.setText("Rp." + DecimalsFormat.priceWithoutDecimal(rencana_keuangan.getTargetDana()));
        holder.binding.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRencanaKeuanganActivity.class);
                intent.putExtra("namatujuan", rencana_keuangan.getTujuan());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransaksiRencanaKeuanganBinding binding;

        public ViewHolder(ItemTransaksiRencanaKeuanganBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = binding.getRoot().getContext();
        }
    }
}
