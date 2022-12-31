package com.awandigital.sikas.adapter;

import static com.awandigital.sikas.db.DatabaseHelper.nama_rencana;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.TujuanFinancialActivity;
import com.awandigital.sikas.databinding.ItemKategoriTambahanBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKategori;
import com.awandigital.sikas.model.MTujuanFinansial;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ATujuanFinancial extends RecyclerView.Adapter<ATujuanFinancial.ViewHolder> {
    private ArrayList<MTujuanFinansial> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public ATujuanFinancial(ArrayList<MTujuanFinansial> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemKategoriTambahanBinding.inflate(LayoutInflater.from(
                parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MTujuanFinansial mTujuanFinansial = arrayList.get(position);
        holder.binding.tvKategori.setText(mTujuanFinansial.getNama_rencana_keuangan());
        holder.binding.iVMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

        holder.binding.tvKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("get-data-tujuan");
                intent.putExtra("gettujuan", mTujuanFinansial.getNama_rencana_keuangan());
                intent.putExtra("jenis", "FP");
                if (mTujuanFinansial.getDana_terkumpul() == null) {
                    intent.putExtra("dana_terkumpul", "0");
                } else {
                    intent.putExtra("dana_terkumpul", mTujuanFinansial.getDana_terkumpul());
                }
                intent.putExtra("target_dana", mTujuanFinansial.getTarget_dana());


                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                ((TujuanFinancialActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ShowDialog() {
        final String[] option = {"Ubah", "Hapus"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        final BottomSheetDialog bsd = new BottomSheetDialog(context,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_ubah_kategori);

                        bsd.show();
                        break;
                    case 1:
                        GlobalToast.ShowToast(context, "Anda Memilih" + option[1]);
                        break;
                }
            }
        });
        alert.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemKategoriTambahanBinding binding;

        public ViewHolder(ItemKategoriTambahanBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = binding.getRoot().getContext();
            dbHelper = new DatabaseHelper(context);
        }
    }
}
