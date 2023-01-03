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

import com.awandigital.sikas.activity.DetailTransaksiKeuangan;
import com.awandigital.sikas.databinding.ItemCatatanKeuanganSemuaBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MCatatanKeuanganSemua;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;

import java.util.ArrayList;

public class ACatatanKeuanganSemua extends RecyclerView.Adapter<ACatatanKeuanganSemua.ViewHolder> {
    private ArrayList<MCatatanKeuanganSemua> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public ACatatanKeuanganSemua(ArrayList<MCatatanKeuanganSemua> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCatatanKeuanganSemuaBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCatatanKeuanganSemua semua = arrayList.get(position);
        holder.binding.tvKategori.setText(semua.getKategori());
        if (semua.getJenisTransaksi().equals("pengeluaran")) {
            holder.binding.tvSumberDana.setText(semua.getDiambilDari());
            holder.binding.tvCostKeluar.setText("- Rp." + DecimalsFormat.priceWithoutDecimal(semua.getNominal()));
            holder.binding.tvCostMasuk.setText("Rp.0");
        } else if (semua.getJenisTransaksi().equals("pemasukan")) {
            holder.binding.tvSumberDana.setText(semua.getTujuanTabungan());
            holder.binding.tvCostMasuk.setText("+ Rp." + DecimalsFormat.priceWithoutDecimal(semua.getNominal()));
            holder.binding.tvCostKeluar.setText("Rp.0");
        }


        holder.binding.tvTgl.setText(semua.getTglTransaksi());
        holder.binding.layCatatanKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, DetailTransaksiKeuangan.class);
                intent.putExtra("id", arrayList.get(position).getIdCatatanKeuangan());
                holder.context.startActivity(intent);
//                GlobalToast.ShowToast(holder.context, semua.getKategori());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCatatanKeuanganSemuaBinding binding;
        Context context;

        public ViewHolder(ItemCatatanKeuanganSemuaBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = binding.getRoot().getContext();
        }
    }
}
