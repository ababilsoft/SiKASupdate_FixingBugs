package com.awandigital.sikas.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.CategoryActivity;
import com.awandigital.sikas.activity.TujuanFinancialActivity;
import com.awandigital.sikas.databinding.ItemKategoriTambahanBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKategori;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class AKategori extends RecyclerView.Adapter<AKategori.ViewHolder> {
    private ArrayList<MKategori> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;

    public AKategori(ArrayList<MKategori> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemKategoriTambahanBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MKategori mKategori = arrayList.get(position);
        holder.binding.tvKategori.setText(mKategori.getKategori());
        holder.binding.iVMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();

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
                                EditText etKategori = bsd.findViewById(R.id.etKategori);
                                Button btSimpan = bsd.findViewById(R.id.btUbah);

                                Objects.requireNonNull(etKategori).setText(mKategori.getKategori());

                                Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String getKategori = etKategori.getText().toString().trim();
                                        String idKat = String.valueOf(mKategori.getId_kategori());
                                        pDialog.show();
                                        dbHelper.updateKategori(getKategori, idKat);
                                        pDialog.dismiss();

                                        Intent intent = new Intent("update-kategori");
                                        intent.putExtra("kategori", "update");

                                        GlobalToast.ShowToast(context, "Data Kategori Berhasil Diupdate");
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                        bsd.dismiss();
                                    }
                                });

                                bsd.show();
                                break;
                            case 1:
                                pDialog.show();
                                dbHelper.deleteKategori(String.valueOf(mKategori.getId_kategori()));
                                Intent intent = new Intent("delete-kategori");
                                intent.putExtra("kategori", "delete");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                GlobalToast.ShowToast(context, "Data Berhasil Di Hapus");
                                pDialog.dismiss();
                                break;
                        }
                    }
                });
                alert.show();
            }
        });

        holder.binding.tvKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("get-data-kategori");
                intent.putExtra("getkategori", mKategori.getKategori());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                ((CategoryActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void ShowDialog() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemKategoriTambahanBinding binding;

        public ViewHolder(ItemKategoriTambahanBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            context = binding.getRoot().getContext();
            pDialog = new OwnProgressDialog(context);
            dbHelper = new DatabaseHelper(context);
            mDatabase = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        }
    }
}
