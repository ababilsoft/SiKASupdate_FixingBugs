package com.awandigital.sikas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.CategoryActivity;
import com.awandigital.sikas.activity.DetailKontakActivity;
import com.awandigital.sikas.activity.UtangUbahActivity;
import com.awandigital.sikas.databinding.ItemRiwayatDetailUtangBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MHutangDetail;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ADetailRiwayatHutang extends RecyclerView.Adapter<ADetailRiwayatHutang.ViewHolder> {
    private ArrayList<MHutangDetail> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public ADetailRiwayatHutang(ArrayList<MHutangDetail> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRiwayatDetailUtangBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MHutangDetail mHutangDetail = arrayList.get(position);

        holder.binding.tvNominalIn.setText("Rp" + DecimalsFormat.priceWithoutDecimal(mHutangDetail.getJml_cicil()));
        holder.binding.tvCatatan.setText(mHutangDetail.getCatatan());
        holder.binding.tvTgl.setText(mHutangDetail.getTgl_transaksi());

        holder.binding.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        context, R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_detail_hutang_piutang);
                TextView tvTgl, tvCatatan, tvNominal;
                tvTgl = bsd.findViewById(R.id.tvtglTransaksi);
                tvCatatan = bsd.findViewById(R.id.tvCatatan);
                tvNominal = bsd.findViewById(R.id.tvNominal);
                tvTgl.setText(mHutangDetail.getTgl_transaksi());
                tvCatatan.setText(mHutangDetail.getCatatan());
                tvNominal.setText("Rp" + DecimalsFormat.priceWithoutDecimal(mHutangDetail.getJml_cicil()));
                String sisaBayar = mHutangDetail.getSisa_bayar();
                Button btUbah = bsd.findViewById(R.id.btUbah);
                TextView tvHapus = bsd.findViewById(R.id.tvHapus);

//                if (mHutangDetail.getStatus_transaksi().equals("Lunas")) {
//                    btUbah.setEnabled(false);
//                    btUbah.setBackgroundResource(R.drawable.button_tersier);
//                }
//                btUbah.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, UtangUbahActivity.class);
//                        intent.putExtra("id_hp", String.valueOf(mHutangDetail.getId_hp()));
//                        context.startActivity(intent);
//
//                    }
//                });

                tvHapus.setOnClickListener(new View.OnClickListener() {

                    @SuppressLint("Range")
                    @Override
                    public void onClick(View view) {
                        int sisa_bayar = 0;
                        int total = 0;
                        int cicilan = 0;
                        cicilan = Integer.parseInt(mHutangDetail.getJml_cicil());
//                        sisa_bayar = Integer.parseInt(sisaBayar);

                        Cursor cursor = dbHelper.getAllHutangDetailById(String.valueOf(mHutangDetail.getId_hp()));
                        if (cursor.moveToFirst()) {
                            sisa_bayar = Integer.parseInt(cursor.getString(cursor.getColumnIndex("sisa_bayar")));
                        }
                        total = sisa_bayar + cicilan;
                        GlobalToast.ShowToast(context, String.valueOf(total));

                        dbHelper.deleteCicilan(String.valueOf(mHutangDetail.getId_hutang_detail())); // hapus cicilan di hp detail
                        dbHelper.updateHutangPiutang(String.valueOf(mHutangDetail.getId_hp()), String.valueOf(total)); // update sisa bayar

                        arrayList.clear();

                        Intent intent = new Intent("listHutangPiutang-state");
                        intent.putExtra("refresh", "list");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        GlobalToast.ShowToast(context, "Data Berhasil Dihapus");

                        bsd.dismiss();

                    }
                });
                bsd.show();
            }
        });
    }

    public void filter(String text, ArrayList<MHutangDetail> arrayListCopy) {
        arrayList.clear();
        if (text.isEmpty()) {
            arrayList.addAll(arrayListCopy);
        } else {
            text = text.toLowerCase();
            for (MHutangDetail item : arrayListCopy) {
                if (item.getCatatan().toLowerCase().contains(text)) {
                    arrayList.add(item);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemRiwayatDetailUtangBinding binding;

        public ViewHolder(ItemRiwayatDetailUtangBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = itemView.getRoot().getContext();
            dbHelper = new DatabaseHelper(context);
        }
    }
}
