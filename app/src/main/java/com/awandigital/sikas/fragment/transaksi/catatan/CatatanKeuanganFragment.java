package com.awandigital.sikas.fragment.transaksi.catatan;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.FragmentCatatanKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MCatatanKeuanganKeluar;
import com.awandigital.sikas.model.MCatatanKeuanganMasuk;
import com.awandigital.sikas.model.MCatatanKeuanganSemua;
import com.awandigital.sikas.adapter.ACatatanKeuanganPemasukan;
import com.awandigital.sikas.adapter.ACatatanKeuanganPengeluaran;
import com.awandigital.sikas.adapter.ACatatanKeuanganSemua;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.awandigital.sikas.utils.export_file.excel.exportPemasukanExcel;
import com.awandigital.sikas.utils.export_file.excel.exportPengeluaranExcel;
import com.awandigital.sikas.utils.export_file.excel.exportSemuaExcel;
import com.awandigital.sikas.utils.export_file.pdf.exportPemasukanPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportPengeluaranPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportSemuaPDF;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class CatatanKeuanganFragment extends Fragment {
    FragmentCatatanKeuanganBinding binding;
    ACatatanKeuanganSemua aCatatanKeuanganSemua;
    ACatatanKeuanganPemasukan aCatatanKeuanganPemasukan;
    ACatatanKeuanganPengeluaran aCatatanKeuanganPengeluaran;

    ArrayList<MCatatanKeuanganKeluar> dataKeluar = new ArrayList<>();
    ArrayList<MCatatanKeuanganMasuk> dataMasuk = new ArrayList<>();
    ArrayList<MCatatanKeuanganSemua> dataSemua = new ArrayList<>();

    String vTab, vSubTab;

    int total = 0;
    Activity mActivity;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatatanKeuanganBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        //        Data Statis Semua
        binding.rvCatatanKeuanganSemua.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvCatatanKeuanganSemua.setLayoutManager(layoutManager);


//        Data Statis Pemasukan
        binding.rvCatatanKeuanganMasuk.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvCatatanKeuanganMasuk.setLayoutManager(layoutManager2);


//        Data Statis Pengeluaran
        binding.rvCatatanKeuanganKeluar.setHasFixedSize(true);
        GridLayoutManager layoutManager3 = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvCatatanKeuanganKeluar.setLayoutManager(layoutManager3);


        vTab = "Semua";
        vSubTab = "Semua";

        AndLog.ShowLog("vTab", vTab + " " + vSubTab);
        AndLog.ShowLog("vSubTab", vSubTab + " " + vTab);

        catatanKeuanganSemua();
        catatanKeuanganMasuk();
        catatanKeuanganKeluar();


        binding.btAddCatatanKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_detail, new CatatanKeuanganTambahFragment(), "");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        binding.btSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTab = "Semua";

                if (vSubTab.equals("Semua")) {
                    catatanKeuanganSemua();
                } else if (vSubTab.equals("monthly")) {
                    MonthlycatatanKeuanganSemua();
                } else if (vSubTab.equals("weekly")) {
                    WeeklycatatanKeuanganSemua();
                } else if (vSubTab.equals("day")) {
                    DaycatatanKeuanganSemua();
                }


                binding.llCatatanKeuanganSemua.setVisibility(View.VISIBLE);
                binding.llCatatanKeuanganPengeluaran.setVisibility(View.GONE);
                binding.llCatatanKeuanganPemasukan.setVisibility(View.GONE);
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_500));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btPengeluaran.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // chek data catatan keluar
                vTab = "pengeluaran";

                if (vSubTab.equals("Semua")) {
                    catatanKeuanganSemua();
                } else if (vSubTab.equals("monthly")) {
                    MonthlycatatanKeuanganKeluar();
                } else if (vSubTab.equals("weekly")) {
                    WeeklycatatanKeuanganKeluar();
                } else if (vSubTab.equals("day")) {
                    DaycatatanKeuanganKeluar();
                }

                binding.llCatatanKeuanganSemua.setVisibility(View.GONE);
                binding.llCatatanKeuanganPengeluaran.setVisibility(View.VISIBLE);
                binding.llCatatanKeuanganPemasukan.setVisibility(View.GONE);
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.danger_500));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));


//                for (int a = 0; a < arrayPengeluaran.size(); a++) {
//
//                    int tempTotal = Integer.parseInt(arrayPengeluaran.get(a).getNominal());
//                    total = total + tempTotal;
//
//                }

                AndLog.ShowLog("Totals", String.valueOf(total));

            }
        });

        binding.btPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTab = "pemasukan";

                if (vSubTab.equals("Semua")) {
                    catatanKeuanganSemua();
                } else if (vSubTab.equals("monthly")) {
                    MonthlycatatanKeuanganMasuk();
                } else if (vSubTab.equals("weekly")) {
                    WeeklycatatanKeuanganMasuk();
                } else if (vSubTab.equals("day")) {
                    DaycatatanKeuanganMasuk();
                }


                binding.llCatatanKeuanganSemua.setVisibility(View.GONE);
                binding.llCatatanKeuanganPengeluaran.setVisibility(View.GONE);
                binding.llCatatanKeuanganPemasukan.setVisibility(View.VISIBLE);
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.success_500));
            }
        });

        binding.btSubSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSubTab = "Semua";

                if (vTab.equals("Semua")) {
                    catatanKeuanganSemua();
                } else if (vTab.equals("pengeluaran")) {
                    catatanKeuanganKeluar();
                } else if (vTab.equals("pemasukan")) {
                    catatanKeuanganMasuk();
                }
                binding.btSubSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btSubSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubBulanIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubMingguIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubHariIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubHariIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

            }
        });

        binding.btSubBulanIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSubTab = "monthly";

                MonthlycatatanKeuanganSemua();
                MonthlycatatanKeuanganMasuk();
                MonthlycatatanKeuanganKeluar();
                binding.btSubSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btSubBulanIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubMingguIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubHariIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubHariIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btSubMingguIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSubTab = "weekly";
                WeeklycatatanKeuanganSemua();
                WeeklycatatanKeuanganKeluar();
                WeeklycatatanKeuanganMasuk();
                binding.btSubSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubBulanIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btSubMingguIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubHariIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubHariIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btSubHariIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSubTab = "day";
                DaycatatanKeuanganSemua();
                DaycatatanKeuanganKeluar();
                DaycatatanKeuanganMasuk();

                binding.btSubSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubBulanIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSubMingguIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btSubHariIni.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btSubHariIni.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentCatatanKeuanganFilterBinding filterBinding;
//                filterBinding = FragmentCatatanKeuanganFilterBinding.inflate(inflater, container, false);
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        mActivity, R.style.BottomShetDialogTheme);
                View bsdView = LayoutInflater.from(getContext())
                        .inflate(R.layout.lbs_filter,
                                mActivity.findViewById(R.id.lsb_filte_catatan_keuangan));
                bsd.setContentView(bsdView);

                bsd.show();
            }
        });

        binding.btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    public void onClick(View v) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        if (selectedId == pdf.getId()) {
                            switch (vTab) {
                                case "Semua":
                                    if (dataSemua.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try {
                                        exportSemuaPDF.createPdf(v.getContext(), dataSemua, "Semua_Catatan_Keuangan", true);
                                        Toast.makeText(getContext(), "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "File gagal disimpan", Toast.LENGTH_SHORT).show();
                                    }
                                    bsd.dismiss();
                                    return;
                                case "pemasukan":
                                    if (dataMasuk.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try {
                                        exportPemasukanPDF.createPdf(v.getContext(), dataMasuk, "Pemasukan_Catatan_Keuangan", true);
                                        Toast.makeText(getContext(), "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "File gagal disimpan", Toast.LENGTH_SHORT).show();
                                    }
                                    bsd.dismiss();
                                    break;
                                case "pengeluaran":
                                    if (dataKeluar.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    try {
                                        exportPengeluaranPDF.createPdf(v.getContext(), dataKeluar, "Pengeluaran_Catatan_Keuangan", true);
                                        Toast.makeText(getContext(), "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "File gagal disimpan", Toast.LENGTH_SHORT).show();
                                    }
                                    bsd.dismiss();
                                    break;
                            }
                        } else if (selectedId == excel.getId()) {
                            switch (vTab) {
                                case "Semua":
                                    if (dataSemua.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    exportSemuaExcel.exportDataIntoWorkbook(getContext(), "Semua_Catatan_Keuangan", dataSemua);
                                    bsd.dismiss();
                                    return;
                                case "pemasukan":
                                    if (dataMasuk.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    exportPemasukanExcel.exportDataIntoWorkbook(getContext(), "Pemasukan_Catatan_Keuangan", dataMasuk);
                                    bsd.dismiss();
                                    break;
                                case "pengeluaran":
                                    if (dataKeluar.isEmpty()) {
                                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    exportPengeluaranExcel.exportDataIntoWorkbook(getContext(), "Pengeluaran_Catatan_Keuangan", dataKeluar);
                                    bsd.dismiss();
                                    break;
                            }
                        }
                    }
                });


                bsd.setContentView(bsdView);

                bsd.show();
            }
        });


        return binding.getRoot();


    }


    private void catatanKeuanganKeluar() {
        dataKeluar.clear();
        Cursor cursorproduct = dbHelper.getAllPengeluaran();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataKeluar.add(new MCatatanKeuanganKeluar(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(3), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataKeluar.size(); a++) {
            nominal = Integer.parseInt(dataKeluar.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostKeluar2.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("total", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataKeluar.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.listPengeluaran", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPengeluaran = new ACatatanKeuanganPengeluaran(dataKeluar, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganKeluar.setAdapter(aCatatanKeuanganPengeluaran);
    }

    @SuppressLint("SetTextI18n")
    private void catatanKeuanganMasuk() {
        dataMasuk.clear();
        Cursor cursorproduct = dbHelper.getAllPemasukan();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataMasuk.add(new MCatatanKeuanganMasuk(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataMasuk.size(); a++) {
            nominal = Integer.parseInt(dataMasuk.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataMasuk.isEmpty()) {
            AndLog.ShowLog("catatanKeuanganFragmen.listPemasukan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPemasukan = new ACatatanKeuanganPemasukan(dataMasuk, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganMasuk.setAdapter(aCatatanKeuanganPemasukan);
    }

    @SuppressLint("SetTextI18n")
    private void catatanKeuanganSemua() {
        dataSemua.clear();
        Cursor cursorproduct = dbHelper.getAllCatatan();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataSemua.add(new MCatatanKeuanganSemua(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7), // jenis transaksi
                        cursorproduct.getString(2), // tujuan tabungan
                        cursorproduct.getString(3) // diambil dari

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total1 = 0;
        int total2 = 0;
        for (int a = 0; a < dataSemua.size(); a++) {
            nominal = Integer.parseInt(dataSemua.get(a).getNominal());

            if (dataSemua.get(a).getJenisTransaksi().equals("pemasukan")) {
                total1 = total1 + nominal;
            } else {
                total2 = total2 + nominal;
            }
        }

        binding.tvTotalCostMasuk.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total1)));
        binding.tvTotalCostKeluar.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total2)));


        if (dataSemua.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.Semua", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganSemua = new ACatatanKeuanganSemua(dataSemua, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganSemua.setAdapter(aCatatanKeuanganSemua);
    }

    //    all
    @SuppressLint("SetTextI18n")
    private void MonthlycatatanKeuanganSemua() {
        dataSemua.clear();
        Cursor cursorproduct = dbHelper.MonthlygetAllCatatan();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataSemua.add(new MCatatanKeuanganSemua(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7), // jenis transaksi
                        cursorproduct.getString(2), // tujuan tabungan
                        cursorproduct.getString(3) // diambil dari

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total1 = 0;
        int total2 = 0;
        for (int a = 0; a < dataSemua.size(); a++) {
            nominal = Integer.parseInt(dataSemua.get(a).getNominal());

            if (dataSemua.get(a).getJenisTransaksi().equals("pemasukan")) {
                total1 = total1 + nominal;
            } else {
                total2 = total2 + nominal;
            }
        }

        binding.tvTotalCostMasuk.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total1)));
        binding.tvTotalCostKeluar.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total2)));


        if (dataSemua.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.Semua", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganSemua = new ACatatanKeuanganSemua(dataSemua, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganSemua.setAdapter(aCatatanKeuanganSemua);
    }

    private void WeeklycatatanKeuanganSemua() {
        dataSemua.clear();
        Cursor cursorproduct = dbHelper.getAllCatatanWeekly();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataSemua.add(new MCatatanKeuanganSemua(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7), // jenis transaksi
                        cursorproduct.getString(2), // tujuan tabungan
                        cursorproduct.getString(3) // diambil dari

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total1 = 0;
        int total2 = 0;
        for (int a = 0; a < dataSemua.size(); a++) {
            nominal = Integer.parseInt(dataSemua.get(a).getNominal());

            if (dataSemua.get(a).getJenisTransaksi().equals("pemasukan")) {
                total1 = total1 + nominal;
            } else {
                total2 = total2 + nominal;
            }
        }

        binding.tvTotalCostMasuk.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total1)));
        binding.tvTotalCostKeluar.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total2)));


        if (dataSemua.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.Semua", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganSemua = new ACatatanKeuanganSemua(dataSemua, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganSemua.setAdapter(aCatatanKeuanganSemua);
    }

    private void DaycatatanKeuanganSemua() {
        dataSemua.clear();
        Cursor cursorproduct = dbHelper.getAllCatatanDay();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataSemua.add(new MCatatanKeuanganSemua(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7), // jenis transaksi
                        cursorproduct.getString(2), // tujuan tabungan
                        cursorproduct.getString(3) // diambil dari

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total1 = 0;
        int total2 = 0;
        for (int a = 0; a < dataSemua.size(); a++) {
            nominal = Integer.parseInt(dataSemua.get(a).getNominal());

            if (dataSemua.get(a).getJenisTransaksi().equals("pemasukan")) {
                total1 = total1 + nominal;
            } else {
                total2 = total2 + nominal;
            }
        }

        binding.tvTotalCostMasuk.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total1)));
        binding.tvTotalCostKeluar.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total2)));


        if (dataSemua.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.Semua", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganSemua = new ACatatanKeuanganSemua(dataSemua, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganSemua.setAdapter(aCatatanKeuanganSemua);
    }


    //    in
    @SuppressLint("SetTextI18n")
    private void MonthlycatatanKeuanganMasuk() {
        dataMasuk.clear();
        Cursor cursorproduct = dbHelper.getFilterPemasukanMonthly2nd(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataMasuk.add(new MCatatanKeuanganMasuk(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataMasuk.size(); a++) {
            nominal = Integer.parseInt(dataMasuk.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataMasuk.isEmpty()) {
            AndLog.ShowLog("catatanKeuanganFragmen.listPemasukan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPemasukan = new ACatatanKeuanganPemasukan(dataMasuk, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganMasuk.setAdapter(aCatatanKeuanganPemasukan);
    }

    @SuppressLint("SetTextI18n")
    private void WeeklycatatanKeuanganMasuk() {
        dataMasuk.clear();
        Cursor cursorproduct = dbHelper.getFilterPemasukanWeekly2nd(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataMasuk.add(new MCatatanKeuanganMasuk(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataMasuk.size(); a++) {
            nominal = Integer.parseInt(dataMasuk.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataMasuk.isEmpty()) {
            AndLog.ShowLog("catatanKeuanganFragmen.listPemasukan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPemasukan = new ACatatanKeuanganPemasukan(dataMasuk, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganMasuk.setAdapter(aCatatanKeuanganPemasukan);
    }

    @SuppressLint("SetTextI18n")
    private void DaycatatanKeuanganMasuk() {
        dataMasuk.clear();
        Cursor cursorproduct = dbHelper.getFilterPemasukanDay2nd(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataMasuk.add(new MCatatanKeuanganMasuk(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataMasuk.size(); a++) {
            nominal = Integer.parseInt(dataMasuk.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataMasuk.isEmpty()) {
            AndLog.ShowLog("catatanKeuanganFragmen.listPemasukan", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPemasukan = new ACatatanKeuanganPemasukan(dataMasuk, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganMasuk.setAdapter(aCatatanKeuanganPemasukan);
    }

    //    out
    private void MonthlycatatanKeuanganKeluar() {
        dataKeluar.clear();
        Cursor cursorproduct = dbHelper.getFilterPengeluaranMonthly(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataKeluar.add(new MCatatanKeuanganKeluar(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(3), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataKeluar.size(); a++) {
            nominal = Integer.parseInt(dataKeluar.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostKeluar2.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("total", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataKeluar.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.listPengeluaran", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPengeluaran = new ACatatanKeuanganPengeluaran(dataKeluar, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganKeluar.setAdapter(aCatatanKeuanganPengeluaran);
    }

    private void WeeklycatatanKeuanganKeluar() {
        dataKeluar.clear();
        Cursor cursorproduct = dbHelper.getFilterPengeluaranWeekly2nd(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataKeluar.add(new MCatatanKeuanganKeluar(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(3), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataKeluar.size(); a++) {
            nominal = Integer.parseInt(dataKeluar.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostKeluar2.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("total", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataKeluar.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.listPengeluaran", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPengeluaran = new ACatatanKeuanganPengeluaran(dataKeluar, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganKeluar.setAdapter(aCatatanKeuanganPengeluaran);
    }

    private void DaycatatanKeuanganKeluar() {
        dataKeluar.clear();
        Cursor cursorproduct = dbHelper.getFilterPengeluaranDay2nd(vTab);

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                dataKeluar.add(new MCatatanKeuanganKeluar(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(3), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(7) // jenis transaksi

                ));

            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < dataKeluar.size(); a++) {
            nominal = Integer.parseInt(dataKeluar.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalCostKeluar2.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("total", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (dataKeluar.isEmpty()) {
            AndLog.ShowLog("CatatanKeuanganFragment.listPengeluaran", "No items Pemasukan Found in database");
        }


        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aCatatanKeuanganPengeluaran = new ACatatanKeuanganPengeluaran(dataKeluar, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvCatatanKeuanganKeluar.setAdapter(aCatatanKeuanganPengeluaran);
    }

}