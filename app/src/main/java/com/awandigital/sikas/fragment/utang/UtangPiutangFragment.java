package com.awandigital.sikas.fragment.utang;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.BaseDetailUtangActivity;
import com.awandigital.sikas.adapter.AUtangPiutang;
import com.awandigital.sikas.databinding.FragmentUtangPiutangBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MUtangPiutang;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;

import java.util.ArrayList;


public class UtangPiutangFragment extends Fragment {

    FragmentUtangPiutangBinding binding;
    AUtangPiutang aUtangPiutang;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    Activity mActivity;
    String utangSaya, utangTeman;
    ArrayList<MUtangPiutang> data = new ArrayList<>();
    ArrayList<MUtangPiutang> arrayListCopy = new ArrayList<>();
    public static ArrayList<MUtangPiutang> valuesWhere = new ArrayList<>();

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String refresh = intent.getStringExtra("refresh");
            if (refresh.equals("list")) {
                listHutangPiutang();

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
            }
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

        binding = FragmentUtangPiutangBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.rvUtang.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvUtang.setLayoutManager(layoutManager);

        //        Inisialisasi Refesh Automatis
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("listHutangPiutang"));

        binding.swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefesh.setRefreshing(true);
                data.clear();
                listHutangPiutang();
            }
        });
        listHutangPiutang();

        binding.etCariCatatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aUtangPiutang.filter(String.valueOf(charSequence), arrayListCopy);
                aUtangPiutang.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btRiwayatUtang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailUtangActivity.class);
                i.putExtra("pindah", "1");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BaseDetailUtangActivity.class);
                i.putExtra("pindah", "2");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

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


        binding.btSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listHutangPiutang();
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangSaya.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangSaya.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangTeman.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangTeman.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

            }
        });

        binding.btUtangSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUtangSaya("Utang");
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangSaya.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btUtangSaya.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangTeman.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangTeman.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btUtangTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUtangSaya("Piutang");
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangSaya.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangSaya.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangTeman.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btUtangTeman.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btLunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUtangPiutangLunas("Lunas");
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangSaya.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangSaya.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangTeman.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangTeman.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });

        binding.btBLunas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUtangPiutangLunas("Belum Lunas");
                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangSaya.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangSaya.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btUtangTeman.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btUtangTeman.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBLunas.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btBLunas.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));
            }
        });


        return binding.getRoot();
    }

    private void listHutangPiutang() {
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
        aUtangPiutang = new AUtangPiutang(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvUtang.setAdapter(aUtangPiutang);
        binding.swipeRefesh.setRefreshing(false);
    }

    private void listUtangSaya(String val) {
        data.clear();
        Cursor cursorproduct = dbHelper.getListHutangPiutangWhere(val);

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

        //creating the adapter object
        aUtangPiutang = new AUtangPiutang(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvUtang.setAdapter(aUtangPiutang);

    }

    private void listUtangPiutangLunas(String value) {
        data.clear();
        Cursor cursorproduct = dbHelper.getListHutangPiutangLunas(value);

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

        //creating the adapter object
        aUtangPiutang = new AUtangPiutang(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvUtang.setAdapter(aUtangPiutang);

    }

}