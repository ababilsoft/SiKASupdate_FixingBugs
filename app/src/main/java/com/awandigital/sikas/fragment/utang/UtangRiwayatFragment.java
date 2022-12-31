package com.awandigital.sikas.fragment.utang;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ARiwayatHutang;
import com.awandigital.sikas.adapter.AUtangPiutang;
import com.awandigital.sikas.databinding.FragmentUtangRiwayatBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.export_file.excel.exportRiwayatHutangExcel;
import com.awandigital.sikas.utils.export_file.pdf.exportPemasukanPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportRiwayatHutangPDF;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class UtangRiwayatFragment extends Fragment {
    FragmentUtangRiwayatBinding binding;

    UtangDetailFragment utangDetailFragment = new UtangDetailFragment();
    ARiwayatHutang aRiwayatHutang;
    AUtangPiutang aUtangPiutang;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    Activity mActivity;
    String utangSaya, utangTeman;
    ArrayList<MUtangPiutang> data = new ArrayList<>();
    ArrayList<MUtangPiutang> arrayListCopy = new ArrayList<>();
    int totalUtang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUtangRiwayatBinding.inflate(inflater, container, false);

//        Mengarah ke halaman detail hutang

//        binding.ivRiwayat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_utang, utangDetailFragment, "");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });


        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.rvUtang.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvUtang.setLayoutManager(layoutManager);
        listDataHutang();

        Cursor cursor = dbHelper.getUtangSaya();

        if (cursor.moveToFirst()) {
            if (cursor.getString(cursor.getColumnIndex("utangsaya")) == null) {
                utangSaya = "0";
            } else {
                utangSaya = cursor.getString(cursor.getColumnIndex("utangsaya"));
            }
        }

        binding.tvUtangSaya.setText("Rp" + DecimalsFormat.priceWithoutDecimal(utangSaya));

        Cursor cursor2 = dbHelper.getUtangTeman();

        if (cursor2.moveToFirst()) {
            if (cursor2.getString(cursor2.getColumnIndex("utangteman")) == null) {
                utangTeman = "0";
            } else {
                utangTeman = cursor2.getString(cursor2.getColumnIndex("utangteman"));
            }
        }
        binding.tvUtangTeman.setText("Rp" + DecimalsFormat.priceWithoutDecimal(utangTeman));

        totalUtang = Integer.parseInt(utangSaya) + Integer.parseInt(utangTeman);
        binding.tvSisaSeluruhUtang.setText(DecimalsFormat.priceWithoutDecimal(String.valueOf(totalUtang)));


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        binding.btLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        mActivity, R.style.BottomShetDialogTheme);
                View bsdView = LayoutInflater.from(getContext())
                        .inflate(R.layout.lbs_catatan_keuangan_download,
                                mActivity.findViewById(R.id.lbs_download));

                RadioGroup radioGroup = bsdView.findViewById(R.id.radioSex);
                RadioButton pdf = bsdView.findViewById(R.id.rbPdf);
                RadioButton excel = bsdView.findViewById(R.id.rbExcel);
                Button btnDownload = bsdView.findViewById(R.id.btSimpan);

                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        if (selectedId == pdf.getId()) {
                            if (arrayListCopy.isEmpty()) {
                                Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                exportRiwayatHutangPDF.createPdf(mActivity, arrayListCopy, "Riwayat_Hutang", true);
                                Toast.makeText(getContext(), "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "File gagal disimpan", Toast.LENGTH_SHORT).show();
                            }
                            bsd.dismiss();
                        } else if (selectedId == excel.getId()) {
                            if (arrayListCopy.isEmpty()) {
                                Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            exportRiwayatHutangExcel.exportDataIntoWorkbook(mActivity, "Riwayat_Hutang", arrayListCopy);
                            bsd.dismiss();
                        }
                    }
                });

                bsd.setContentView(bsdView);

                bsd.show();
            }
        });

        binding.etCariCatatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aRiwayatHutang.filter(String.valueOf(charSequence), arrayListCopy);
                aRiwayatHutang.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();
    }

    private void listDataHutang() {
        data.clear();
        Cursor cursorproduct = dbHelper.getAllUtangPiutang();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MUtangPiutang(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(3), // dipinjamkan ke
                        cursorproduct.getString(2), // dipinjamkan oleh
                        cursorproduct.getString(7), // nominal
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(8), // status transaksi
                        cursorproduct.getString(1) // jenis transaksi
                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("LOG_UTANG_PIUTANG", "No items Found in database");
        }
        //closing the cursor
        cursorproduct.close();
        arrayListCopy.clear();
        arrayListCopy.addAll(data);

        //creating the adapter object
        aRiwayatHutang = new ARiwayatHutang(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvUtang.setAdapter(aRiwayatHutang);
    }

}