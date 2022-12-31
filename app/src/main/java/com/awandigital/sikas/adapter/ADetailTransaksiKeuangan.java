package com.awandigital.sikas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.databinding.ItemTransaksiDetailRencanaKeuanganBinding;
import com.awandigital.sikas.model.MDetailTransaksi;

import java.util.ArrayList;

public class ADetailTransaksiKeuangan extends RecyclerView.Adapter<ADetailTransaksiKeuangan.ViewHolder> {

    private ArrayList<MDetailTransaksi> arrayList;

    public ADetailTransaksiKeuangan(ArrayList<MDetailTransaksi> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTransaksiDetailRencanaKeuanganBinding.inflate(LayoutInflater.from(
                parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MDetailTransaksi mDetailTransaksi = arrayList.get(position);
        holder.binding.tvJenisTransaksi.setText(mDetailTransaksi.getJenisTransaksi());
        holder.binding.tvStatusPelunasan.setText(mDetailTransaksi.getStatusPelunasan());
        holder.binding.tvTgl.setText(mDetailTransaksi.getTgl());
        holder.binding.tvKategori.setText(mDetailTransaksi.getKategori());
        holder.binding.tvCatatan.setText(mDetailTransaksi.getCatatan1());
        holder.binding.tvCost.setText(mDetailTransaksi.getJmlCost());
        holder.binding.tvCatatan2.setText(mDetailTransaksi.getCatatan2());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransaksiDetailRencanaKeuanganBinding binding;

        public ViewHolder(ItemTransaksiDetailRencanaKeuanganBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
