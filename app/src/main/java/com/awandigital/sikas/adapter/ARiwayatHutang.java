package com.awandigital.sikas.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.DetailRiwayatHutang;
import com.awandigital.sikas.databinding.ItemUtangBinding;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;

import java.util.ArrayList;

public class ARiwayatHutang extends RecyclerView.Adapter<ARiwayatHutang.ViewHolder> {
    private ArrayList<MUtangPiutang> arrayList;
    Context context;
    SQLiteDatabase mDatabase;

    public ARiwayatHutang(ArrayList<MUtangPiutang> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemUtangBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MUtangPiutang hutang = arrayList.get(position);
        if (hutang.getJenis_transaksi().equals("Utang")) {
            holder.binding.tvNama.setText(hutang.getDipinjamkan_oleh());
            holder.binding.tvNominal.setText("Rp" + DecimalsFormat.priceWithoutDecimal(hutang.getNominal()));
            holder.binding.tvJenisTransaksi.setText("Utang Saya");
            holder.binding.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.danger_400));
        } else if (hutang.getJenis_transaksi().equals("Piutang")) {
            holder.binding.tvNama.setText(hutang.getDipinjamkan_ke());
            holder.binding.tvNominal.setText("Rp" + DecimalsFormat.priceWithoutDecimal(hutang.getNominal()));
            holder.binding.tvJenisTransaksi.setText("Utang teman");
        }
        holder.binding.tvTgl.setText(hutang.getTgl_transaksi());
        holder.binding.btItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRiwayatHutang.class);
                intent.putExtra("id", hutang.getId_hp());
                intent.putExtra("nama", holder.binding.tvNama.getText());
                context.startActivity(intent);
            }
        });

    }

    public void filter(String text, ArrayList<MUtangPiutang> arrayListCopy) {
        arrayList.clear();
        if (text.isEmpty()) {
            arrayList.addAll(arrayListCopy);
        } else {
            text = text.toLowerCase();
            for (MUtangPiutang item : arrayListCopy) {
                if (item.getJenis_transaksi().equals("Utang")) {
                    if (item.getDipinjamkan_oleh().toLowerCase().contains(text)) {
                        arrayList.add(item);
                    }
                } else if (item.getJenis_transaksi().equals("Piutang")) {
                    if (item.getDipinjamkan_ke().toLowerCase().contains(text)) {
                        arrayList.add(item);
                    }
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUtangBinding binding;


        public ViewHolder(ItemUtangBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = binding.getRoot().getContext();
        }
    }
}
