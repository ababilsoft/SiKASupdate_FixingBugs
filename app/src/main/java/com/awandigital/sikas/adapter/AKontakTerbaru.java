package com.awandigital.sikas.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.nama;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ItemKontakTerbaruBinding;
import com.awandigital.sikas.databinding.ItemTombolTambahBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKontakTerbaru;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

public class AKontakTerbaru extends RecyclerView.Adapter<AKontakTerbaru.ViewHolder> {
    private ArrayList<MKontakTerbaru> arrayList;
    Context context;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;

    public AKontakTerbaru(ArrayList<MKontakTerbaru> arrayList, Context context, SQLiteDatabase mDatabase) {
        this.arrayList = arrayList;
        this.context = context;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == R.layout.item_kontak_terbaru) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kontak_terbaru, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tombol_tambah, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == arrayList.size()) {
            holder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final BottomSheetDialog bsd = new BottomSheetDialog(context,
                            R.style.BottomShetDialogTheme);
                    bsd.setContentView(R.layout.lbs_tambah_kontak_baru);
                    EditText etNama = bsd.findViewById(R.id.etNamaKontak);
                    Button btSimpan = bsd.findViewById(R.id.bt_tambah);

                    Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String namaK = Objects.requireNonNull(etNama).getText().toString().trim();
                            if (namaK.isEmpty()) {
                                GlobalToast.ShowToast(context, "Data tidak boleh kosong");
                            } else {
                                dbHelper.addKontak(namaK);
                                Intent intent = new Intent("listKontak-state");
                                intent.putExtra("refresh", "list");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                bsd.dismiss();
                                GlobalToast.ShowToast(context, "Data kontak baru ditambahkan");
                            }
                        }
                    });
                    bsd.show();
                }
            });
        } else {
            MKontakTerbaru mKontakTerbaru = arrayList.get(position);
            holder.tvNamaKontak.setText(mKontakTerbaru.getNamaKontak());

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("getKontak-state");
                    intent.putExtra("kontak", "didapat");
                    intent.putExtra("namaKontak", holder.tvNamaKontak.getText());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    AndLog.ShowLog("LOG_NAMA_CTC", "getKontak");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == arrayList.size()) ? R.layout.item_tombol_tambah : R.layout.item_kontak_terbaru;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNamaKontak;
        public ImageView btAdd;
        public CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaKontak = itemView.findViewById(R.id.tvNamaKontak);
            btAdd = itemView.findViewById(R.id.ivAdd);
            cv = itemView.findViewById(R.id.cardview);
            context = itemView.getContext();
            dbHelper = new DatabaseHelper(context);
            mDatabase = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        }
    }
}
