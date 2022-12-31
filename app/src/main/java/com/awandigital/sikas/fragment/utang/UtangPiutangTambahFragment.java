package com.awandigital.sikas.fragment.utang;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.UtangUbahActivity;
import com.awandigital.sikas.adapter.AKontakTerbaru;
import com.awandigital.sikas.adapter.AUtangPiutang;
import com.awandigital.sikas.databinding.FragmentUtangPiutangTambahBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKontakTerbaru;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.OwnProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class UtangPiutangTambahFragment extends Fragment {
    FragmentUtangPiutangTambahBinding binding;
    AKontakTerbaru aKontakTerbaru;
    Activity mActivity;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    String myFormat = "dd-MMM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String form, statusKontak;
    ArrayList<String> namakontak;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String refresh = intent.getStringExtra("refresh");
            if (refresh.equals("list")) {
                listKontakTerbaru();
            }
        }
    };

    public BroadcastReceiver getKontakReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String getKontak = intent.getStringExtra("namaKontak");
            binding.etDipinjamkanKe.setText(getKontak);
            binding.etDipinjamkanOleh.setText(getKontak);
            AndLog.ShowLog("LOG_NAMA_CTC", getKontak);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUtangPiutangTambahBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(mActivity);

        myCalendar = Calendar.getInstance();
        binding.etTanggal.setText(sdf.format(myCalendar.getTime()));

        binding.linePiutang.setVisibility(View.GONE);
        binding.lineUtang.setVisibility(View.VISIBLE);
        form = "Utang";

//        auto complete kontak
        namakontak = dbHelper.getKontak();
        ArrayAdapter<String> autoComplete = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, namakontak);
        binding.etDipinjamkanOleh.setAdapter(autoComplete);
        binding.etDipinjamkanKe.setAdapter(autoComplete);

//        Inisialisasi Refesh Automatis
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("listKontak-state"));
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(getKontakReceiver,
                new IntentFilter("getKontak-state"));


        binding.btPiutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btUtang.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPiutang.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPiutang.setTextColor(ContextCompat.getColor(requireContext(), R.color.success_500));
                binding.btUtang.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.linePiutang.setVisibility(View.VISIBLE);
                binding.lineUtang.setVisibility(View.GONE);
                form = "Piutang";
                AndLog.ShowLog("TAB_HP", form);
            }
        });

        binding.btUtang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btUtang.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_primary_catatan));
                binding.btPiutang.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.card_catatan));
                binding.btPiutang.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
                binding.btUtang.setTextColor(ContextCompat.getColor(requireContext(), R.color.danger_500));
                binding.linePiutang.setVisibility(View.GONE);
                binding.lineUtang.setVisibility(View.VISIBLE);
                form = "Utang";
                AndLog.ShowLog("TAB_HP", form);
            }
        });

        binding.rvKontakTerbaru.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 1,
                GridLayoutManager.HORIZONTAL, false);
        binding.rvKontakTerbaru.setLayoutManager(layoutManager);
        listKontakTerbaru();

        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        binding.etJumlah.addTextChangedListener(new NumberTextWatcherForThousand(binding.etJumlah));

        binding.btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                String utang = binding.etDipinjamkanOleh.getText().toString().trim();
                String nominal = NumberTextWatcherForThousand.trimCommaOfString(binding.etJumlah.getText().toString().trim());
                String piutang = binding.etDipinjamkanKe.getText().toString().trim();
                String tgl_hutang = binding.etTanggal.getText().toString().trim();
                String tgl_jatuhtempo = binding.etTanggalJatuhtempo.getText().toString().trim();
                String catatan = binding.etCatatan.getText().toString().trim();

                Cursor cursor = dbHelper.addKontak_test(utang);
                if (cursor.getCount() > 0) {
                    // Jika nama kontak telah ada
                    statusKontak = "Ada";
                } else {
                    // Jika Nama Kontak Tidak Ditemukan
                    statusKontak = "Kosong";
                }
                cursor.close();
                if (form.equals("Utang")) {
                    dbHelper.addHutang(utang, nominal, tgl_hutang, tgl_jatuhtempo, catatan);

                    if (statusKontak.equals("Kosong")) {
                        dbHelper.addKontak(utang);
                    }
                    GlobalToast.ShowToast(mActivity, "Pencatatan Hutang Selesai");

                } else if (form.equals("Piutang")) {
                    dbHelper.addPiutang(piutang, nominal, tgl_hutang, tgl_jatuhtempo, catatan);

                    if (statusKontak.equals("Kosong")) {
                        dbHelper.addKontak(piutang);
                    }
                    GlobalToast.ShowToast(mActivity, "Pencatatan Piutang Selesai");
                }

                Intent intent = new Intent("listHutangPiutang");
                intent.putExtra("refresh", "list");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

                pDialog.dismiss();
                mActivity.finish();

            }
        });

        binding.etTanggalJatuhtempo.setOnClickListener(new View.OnClickListener() {
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
                        binding.etTanggalJatuhtempo.setText(sdf.format(myCalendar.getTime()));

                    }
                };
                new DatePickerDialog(mActivity, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        return binding.getRoot();
    }

    private void listKontakTerbaru() {
        Cursor cursorproduct = dbHelper.getAllKontak();

        ArrayList<MKontakTerbaru> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MKontakTerbaru(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(1) // nama

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("LOG_KONTAK", "No items Found in database");
        }
        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aKontakTerbaru = new AKontakTerbaru(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvKontakTerbaru.setAdapter(aKontakTerbaru);

    }
}