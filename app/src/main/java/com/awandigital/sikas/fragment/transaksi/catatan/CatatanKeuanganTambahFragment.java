package com.awandigital.sikas.fragment.transaksi.catatan;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.bulan;
import static com.awandigital.sikas.db.DatabaseHelper.catatan_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.created_at_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.hari;
import static com.awandigital.sikas.db.DatabaseHelper.kategori_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.metode_transaksi_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.nama;
import static com.awandigital.sikas.db.DatabaseHelper.nama_rencana;
import static com.awandigital.sikas.db.DatabaseHelper.nominal_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.tahun;
import static com.awandigital.sikas.db.DatabaseHelper.tgl_transaksi_pemasukan;
import static com.awandigital.sikas.db.DatabaseHelper.tujuan_tabungan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.awandigital.sikas.R;
import com.awandigital.sikas.Registrasi;
import com.awandigital.sikas.activity.BaseDetailTambahCatatanActivity;
import com.awandigital.sikas.activity.CategoryActivity;
import com.awandigital.sikas.activity.MetodeTransaksiActivity;
import com.awandigital.sikas.activity.TujuanFinancialActivity;
import com.awandigital.sikas.databinding.FragmentTambahCatatanKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.OwnProgressDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CatatanKeuanganTambahFragment extends Fragment {
    FragmentTambahCatatanKeuanganBinding binding;
    Activity mActivity;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    String f;
    String myFormat = "dd-MMM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    String Jenis, getCountSaldo, DanaTerkumpul, TargetDana;
    int saldoKasUmum = 0, totalSaldo, SaldoFP = 0;
    String namaRencana, dumpNominalPemasukan, dumpNominalPengeluaran;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String value;

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            namaRencana = intent.getStringExtra("gettujuan");
            Jenis = intent.getStringExtra("jenis");
            DanaTerkumpul = intent.getStringExtra("dana_terkumpul");
            TargetDana = intent.getStringExtra("target_dana");
            binding.etTujuanPemasukan.setText(namaRencana);
            binding.etDiambilDari.setText(namaRencana);

            AndLog.ShowLog("gettujuan", namaRencana);
            AndLog.ShowLog("jenis", Jenis);
            AndLog.ShowLog("dana_terkumpul", DanaTerkumpul);
            AndLog.ShowLog("target_dana", TargetDana);

        }
    };

    public BroadcastReceiver receiverSaldoUmum = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Jenis = intent.getStringExtra("jenis");
            binding.etTujuanPemasukan.setText("Kas Umum");
            binding.etDiambilDari.setText("Kas Umum");
            AndLog.ShowLog("jenis", Jenis);

//          Ambil Data Nominal Kas Umum
            Cursor cursor = dbHelper.dataKasUmum();
            if (cursor.moveToFirst()) {
                saldoKasUmum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nominal")));

                AndLog.ShowLog("nominalKasUmum", String.valueOf(saldoKasUmum));

            }
        }

    };

    public BroadcastReceiver receiverKategori = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String getKategori = intent.getStringExtra("getkategori");

            binding.etKategoriPemasukan.setText(getKategori); // pemasukan
            binding.etKategori.setText(getKategori); // pengeluaran
            AndLog.ShowLog("getkategori", getKategori);
        }
    };

    public BroadcastReceiver receiverPembayaran = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String getPembayaran = intent.getStringExtra("pembayaran");
            binding.etMetodeTransaksiPemasukan.setText(getPembayaran);
            binding.etMetodeTransaksi.setText(getPembayaran);
            AndLog.ShowLog("getPembayaran", getPembayaran);
        }
    };

    public BroadcastReceiver receiveMakanMinum = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v1");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };


    public BroadcastReceiver receiveTransportasi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v2");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };


    public BroadcastReceiver receiveKesehatan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v3");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };


    public BroadcastReceiver receiveBelanja = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v4");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };

    public BroadcastReceiver receivePendidikan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v5");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };


    public BroadcastReceiver receiveAsuransi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            value = intent.getStringExtra("v6");
            binding.etKategori.setText(value);
            binding.etKategoriPemasukan.setText(value);
            AndLog.ShowLog("getKategori", value);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTambahCatatanKeuanganBinding.inflate(inflater, container, false);
        mActivity = getActivity();

        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(mActivity);
        sessionManager = new SessionManager(mActivity);

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("get-data-tujuan"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiverKategori,
                new IntentFilter("get-data-kategori"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiverPembayaran,
                new IntentFilter("pilih-pembayaran"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiverSaldoUmum,
                new IntentFilter("get-data-saldoumum"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveMakanMinum,
                new IntentFilter("getV1"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveTransportasi,
                new IntentFilter("getV2"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveKesehatan,
                new IntentFilter("getV3"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveBelanja,
                new IntentFilter("getV4"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receivePendidikan,
                new IntentFilter("getV5"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiveAsuransi,
                new IntentFilter("getV6"));

        binding.etJumlahPemasukan.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlahPemasukan));

        binding.etJumlah.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlah));

//        Set Waktu Default
        myCalendar = Calendar.getInstance();
        binding.etTanggalPemasukan.setText(sdf.format(myCalendar.getTime()));
        binding.etTanggalTransaksi.setText(sdf.format(myCalendar.getTime()));


        binding.llPemasukan.setVisibility(View.VISIBLE);
        binding.llPengeluaran.setVisibility(View.INVISIBLE);
        binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(

                requireContext(), R.drawable.card_catatan));
        binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(

                requireContext(), R.drawable.button_primary_catatan));
        binding.btPemasukan.setTextColor(ContextCompat.getColor(

                requireContext(), R.color.primary_500));
        binding.btPengeluaran.setTextColor(ContextCompat.getColor(

                requireContext(), R.color.primary_300));

        binding.etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailTambahCatatanActivity.class);
                i.putExtra("input", "1");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.etMetodeTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailTambahCatatanActivity.class);
                i.putExtra("input", "2");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.btPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("klik", "pemasukan");
                binding.llPemasukan.setVisibility(View.VISIBLE);
                binding.llPengeluaran.setVisibility(View.INVISIBLE);
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_500));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("klik", "pengeluaran");
                binding.llPemasukan.setVisibility(View.INVISIBLE);
                binding.llPengeluaran.setVisibility(View.VISIBLE);
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_500));
            }
        });

        ColorStateList colorStateListPrimary = new ColorStateList(
                new int[][]{
                        new int[]{ContextCompat.getColor(requireContext(), R.color.primary_500)}
                },
                new int[]{ContextCompat.getColor(requireContext(), R.color.primary_300)}
        );

        ColorStateList colorStateListSecondary = new ColorStateList(
                new int[][]{
                        new int[]{ContextCompat.getColor(requireContext(), R.color.radio_button_secondary)}
                },
                new int[]{ContextCompat.getColor(requireContext(), R.color.primary_300)}
        );

        binding.rbLunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rbLunas.setButtonTintList(colorStateListPrimary);
                binding.rbBelum.setButtonTintList(colorStateListSecondary);
            }
        });

        binding.rbBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rbBelum.setButtonTintList(colorStateListPrimary);
                binding.rbLunas.setButtonTintList(colorStateListSecondary);
            }
        });

        binding.etTujuanPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TujuanFinancialActivity.class);
                startActivity(intent);
            }
        });

        binding.etKategoriPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CategoryActivity.class);
                startActivity(intent);
            }
        });

        binding.etMetodeTransaksiPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MetodeTransaksiActivity.class);
                startActivity(intent);
            }
        });

        binding.etDiambilDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TujuanFinancialActivity.class);
                startActivity(intent);
            }
        });

        binding.etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CategoryActivity.class);
                startActivity(intent);
            }
        });

        binding.etMetodeTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MetodeTransaksiActivity.class);
                startActivity(intent);
            }
        });


        binding.btSimpanPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pDialog.show();

                String JumlahPemasukan = NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlahPemasukan.getText().toString());
                String TujuanPemasukan = binding.etTujuanPemasukan.getText().toString().trim();
                String KategoriPemasukan = binding.etKategoriPemasukan.getText().toString().trim();
                String TanggalPemasukan = binding.etTanggalPemasukan.getText().toString().trim();
                String MetodeTransaksi = binding.etMetodeTransaksiPemasukan.getText().toString().trim();
                String Catatan = binding.etOpsionalPemasukan.getText().toString().trim();

                if (JumlahPemasukan.isEmpty()
                        || TujuanPemasukan.isEmpty()
                        || KategoriPemasukan.isEmpty()
                        || MetodeTransaksi.isEmpty()) {
                    GlobalToast.ShowToast(mActivity, "Data Tidak Boleh Kosong");
                    pDialog.dismiss();
                } else {


                    if (Jenis.equals("FP")) {
                        totalSaldo = Integer.parseInt(DanaTerkumpul) + Integer.parseInt(JumlahPemasukan);
//                      Update nominal rencana keuangan di dana terkumpul
                        if (TargetDana.equals(String.valueOf(totalSaldo))) {
//                            Update Status terpenuhi
                            GlobalToast.ShowToast(mActivity, "Selamat! Target Dana " + namaRencana + " berhasil terpenuhi.");
                            dbHelper.updateRencanaKeuanganLunas(String.valueOf(totalSaldo), namaRencana);
                        }
//                        Update Progress Biasa
                        dbHelper.updateRencanaKeuangan(String.valueOf(totalSaldo), namaRencana);
                        AndLog.ShowLog("value FP", totalSaldo + "|" + namaRencana);
                    } else {
                        totalSaldo = saldoKasUmum + Integer.parseInt(JumlahPemasukan);
                        AndLog.ShowLog("totalSaldo", String.valueOf(totalSaldo));

                        if (getCountSaldo.equals("0")) {
                            dbHelper.addKasUmum(String.valueOf(totalSaldo));
                        }
//                      Mengganti data kas umum
                        dbHelper.updateKasUmum(String.valueOf(totalSaldo));

                    }

//                float jml = Float.parseFloat(JumlahPemasukan);
//                float pemasukanskrg = 148710000;
//                float dumpPersen = (jml * 100 / pemasukanskrg);
//                AndLog.ShowLog("InfoPersen", "Naik +" + dumpPersen + "%");

//                    Input ke tabel catatan pemasukan
                    dbHelper.addPemasukan(JumlahPemasukan, TujuanPemasukan, KategoriPemasukan,
                            TanggalPemasukan, MetodeTransaksi, Catatan);

                    pDialog.dismiss();
                    mActivity.finish();
                    GlobalToast.ShowToast(mActivity, "Data Berhasil Disimpan");

                }

            }
        });

        binding.btSimpanPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = binding.rgStatusTransaksi.getCheckedRadioButtonId();

                pDialog.show();

                String etJumlahP = NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlah.getText().toString());
                String etDiambilDariP = binding.etDiambilDari.getText().toString().trim();
                String etkategoriP = binding.etKategori.getText().toString().trim();
                String etTglTransaksiP = binding.etTanggalTransaksi.getText().toString().trim();
                String etMetodeTransaksiP = binding.etMetodeTransaksi.getText().toString().trim();

                if (selectedId == binding.rbLunas.getId()) {
                    f = binding.rbLunas.getText().toString().trim();
                } else if (selectedId == binding.rbBelum.getId()) {
                    f = binding.rbBelum.getText().toString().trim();
                }

                String etCatatanP = binding.etOpsional.getText().toString().trim();

                if (etJumlahP.isEmpty()
                        || etDiambilDariP.isEmpty() || etkategoriP.isEmpty()
                        || etTglTransaksiP.isEmpty() || etMetodeTransaksiP.isEmpty()
                        || f.isEmpty()) {
                    GlobalToast.ShowToast(mActivity, "Data Tidak Boleh Kosong");
                    pDialog.dismiss();
                } else {

                    if (Jenis.equals("FP")) {
//                        Update Dana FP untuk realisasi dana
                        dbHelper.updateRencanaKeuanganRealisasi(namaRencana);
                    } else {
//                        Sisa Saldo kas Umum
                        totalSaldo = saldoKasUmum - Integer.parseInt(etJumlahP);
                        dbHelper.updateKasUmum(String.valueOf(totalSaldo));
                        AndLog.ShowLog("sisaSaldoKasUmum", String.valueOf(totalSaldo));
                    }


                    dbHelper.addPengeluaran(etJumlahP, etDiambilDariP, etkategoriP,
                            etTglTransaksiP, etMetodeTransaksiP, etCatatanP, f);

                    pDialog.dismiss();
                    mActivity.finish();
                    GlobalToast.ShowToast(mActivity, "Data Berhasil Disimpan");

                }
            }
        });

        binding.etTanggalPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String myFormat = "dd-MMM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        binding.etTanggalPemasukan.setText(sdf.format(myCalendar.getTime()));

                    }
                };
                new DatePickerDialog(mActivity, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        binding.etTanggalTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String myFormat = "dd-MMM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        binding.etTanggalTransaksi.setText(sdf.format(myCalendar.getTime()));

                    }
                };
                new DatePickerDialog(mActivity, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        getCountSaldo();
        return binding.getRoot();
    }


    @SuppressLint("Range")
    public void getCountSaldo() {
//        Untuk Ngecek Ada Brp Count Baris Saldo Umum
        Cursor cursor = dbHelper.getDataSaldoUmum();
        if (cursor.moveToFirst()) {
            getCountSaldo = cursor.getString(cursor.getColumnIndex("data"));

            AndLog.ShowLog("Saldo", getCountSaldo);
        }

    }
}