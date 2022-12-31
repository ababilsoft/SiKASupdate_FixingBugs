package com.awandigital.sikas.fragment.transaksi;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.APemasukanCatatanKeuangan;
import com.awandigital.sikas.databinding.ActivityLaporanKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.MPengeluaran;
import com.awandigital.sikas.adapter.APemasukanStatistikKeuangan;
import com.awandigital.sikas.adapter.APengeluaranStatistikKeuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.MyMarkerView;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.awandigital.sikas.utils.formatChart.hourMinute;
import com.awandigital.sikas.utils.formatChart.monthYear;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LaporanKeuangan extends AppCompatActivity {
    ActivityLaporanKeuanganBinding binding;
    APemasukanStatistikKeuangan aPemasukanStatistikKeuangan;
    APengeluaranStatistikKeuangan aPengeluaranStatistikKeuangan;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    int saldoKU, saldoFP;
    float prosantaseUp, prosentaseDown, pemasukanSkrg, pengeluaranSkrg, grandSaldo;
    ArrayList<MPengeluaran> data = new ArrayList<>();
    ArrayList<MPemasukan> ArrayPemasukan = new ArrayList<>();

    LineChart mpLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLaporanKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(LaporanKeuangan.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.rvPemasukanStatistikKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvPemasukanStatistikKeuangan.setLayoutManager(layoutManager);
        ArrayPemasukan.clear();
        listPemasukanAll();


        binding.rvPengeluaranStatistikKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvPengeluaranStatistikKeuangan.setLayoutManager(layoutManager2);
        data.clear();
        listPengeluaran();

        setupLineChart(listPemasukanAllChart(), listPengeluaranAllChart(), "all");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btRincianLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaporanKeuangan.this, DetailLaporanKeuangan.class);
                startActivity(intent);
            }
        });

        binding.btBulanIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayPemasukan.clear();
                data.clear();

                setupLineChart(listPemasukanBulanIniChart(), listPengeluaranBulanIniChart(), "monthly");

                listPemasukanBulanIni();
                listPengeluaranBulanIni();


                binding.btBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btBulanIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btHariIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btHariIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

            }
        });

        binding.btMingguIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayPemasukan.clear();
                data.clear();

                setupLineChart(listPemasukanWeeklyChart(), listPengeluaranWeeklyChart(), "weekly");

                listPengeluaranWeekly();
                listPemasukanWeekly();


                binding.btMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btMingguIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btHariIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btHariIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));
            }
        });

        binding.btHariIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayPemasukan.clear();
                data.clear();

                setupLineChart(listPemasukanHariIniChart(), listPengeluaranHariIniChart(), "day");

                listPemasukanHariIni();
                listPengeluaranHariIni();


                binding.btHariIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btHariIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btSemua.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));
            }
        });

        binding.btSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayPemasukan.clear();
                data.clear();

                setupLineChart(listPemasukanAllChart(), listPengeluaranAllChart(), "all");

                listPemasukanAll();
                listPengeluaran();


                binding.btSemua.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btSemua.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btHariIni.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btHariIni.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));


            }
        });

        totalUang(); // get jumlah uang

    }

    void setupLineChart(ArrayList<Entry> dataPemasukan, ArrayList<Entry> dataPengeluaran, String filter) {
        mpLineChart = binding.chartStatistikKeuangan;

        // background color
        mpLineChart.setBackgroundColor(Color.WHITE);

        // disable description text
        mpLineChart.getDescription().setEnabled(false);

        // enable touch gestures
        mpLineChart.setTouchEnabled(true);

        // set listeners
        mpLineChart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(LaporanKeuangan.this, R.layout.custom_chart_dialog);

        // Set the marker to the chart
        mv.setChartView(mpLineChart);
        mpLineChart.setMarker(mv);

        // enable scaling and dragging
        mpLineChart.setDragEnabled(true);
        mpLineChart.setScaleEnabled(true);

        // force pinch zoom along both axis
        mpLineChart.setPinchZoom(true);

        mpLineChart.resetZoom();

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = mpLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(4);

            xAxis.resetAxisMinimum();
            xAxis.resetAxisMaximum();

        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = mpLineChart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            mpLineChart.getAxisRight().setEnabled(false);

            yAxis.resetAxisMinimum();
            yAxis.resetAxisMaximum();

        }

        switch (filter) {
            case "day": {
                yAxis.setAxisMinimum(0f);
                IAxisValueFormatter xAxisFormatter = new hourMinute(mpLineChart);
                xAxis.setValueFormatter(xAxisFormatter);

                LineDataSet dataSetPemasukan = new LineDataSet(dataPemasukan, "Pemasukan");
                dataSetPemasukan.setFillColor(Color.parseColor("#7199FA"));
                dataSetPemasukan.setColors(Color.parseColor("#164af0"));
                dataSetPemasukan.setCircleColor(Color.parseColor("#164af0"));
                dataSetPemasukan.setDrawFilled(true);
                dataSetPemasukan.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                LineDataSet dataSetPengeluaran = new LineDataSet(dataPengeluaran, "Pengeluaran");
                dataSetPengeluaran.setFillColor(Color.parseColor("#f28a7f"));
                dataSetPengeluaran.setColors(Color.parseColor("#eb5757"));
                dataSetPengeluaran.setCircleColor(Color.parseColor("#eb5757"));
                dataSetPengeluaran.setDrawFilled(true);
                dataSetPengeluaran.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSetPemasukan);
                dataSets.add(dataSetPengeluaran);


                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
                break;
            }
            default: {
                yAxis.setAxisMinimum(0f);
                IAxisValueFormatter xAxisFormatter = new monthYear(mpLineChart);
                xAxis.setValueFormatter(xAxisFormatter);

                LineDataSet dataSetPemasukan = new LineDataSet(dataPemasukan, "Pemasukan");
                dataSetPemasukan.setFillColor(Color.parseColor("#7199FA"));
                dataSetPemasukan.setColors(Color.parseColor("#164af0"));
                dataSetPemasukan.setCircleColor(Color.parseColor("#164af0"));
                dataSetPemasukan.setDrawFilled(true);
                dataSetPemasukan.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                LineDataSet dataSetPengeluaran = new LineDataSet(dataPengeluaran, "Pengeluaran");
                dataSetPengeluaran.setFillColor(Color.parseColor("#f28a7f"));
                dataSetPengeluaran.setColors(Color.parseColor("#eb5757"));
                dataSetPengeluaran.setCircleColor(Color.parseColor("#eb5757"));
                dataSetPengeluaran.setDrawFilled(true);
                dataSetPengeluaran.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSetPemasukan);
                dataSets.add(dataSetPengeluaran);


                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
            }
        }
    }

    void setChartPemasukan(ArrayList<Entry> dataPemasukan, String tipe) {

    }


    @SuppressLint("Range")
    private void totalUang() {

        Cursor cursor = dbHelper.dataKasUmum();

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            if (cursor.getString(cursor.getColumnIndex("nominal")) == null) {
                saldoKU = 0;
            } else {
                saldoKU = Integer.parseInt(cursor.getString(cursor.getColumnIndex("nominal")));
            }


        }


        Cursor cursor1 = dbHelper.dataSaldoFp();

        if (cursor1.moveToFirst() && cursor1.getCount() > 0) {
            if (cursor1.getString(cursor1.getColumnIndex("totalfp")) == null) {
                saldoFP = 0;
            } else {

                saldoFP = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("totalfp")));
            }

        }

        grandSaldo = saldoKU + saldoFP;
        AndLog.ShowLog("grandsaldo", String.valueOf(grandSaldo));

        Cursor cursor2 = dbHelper.getNominalPemasukan();
        if (cursor2.moveToFirst()) {
            pemasukanSkrg = Integer.parseInt(cursor2.getString(cursor2.getColumnIndex("nominal")));
            AndLog.ShowLog("pemasukanskrg", String.valueOf(pemasukanSkrg));

        }

        Cursor cursor3 = dbHelper.getNominalPengeluaran();
        if (cursor3.moveToFirst()) {
            pengeluaranSkrg = Integer.parseInt(cursor3.getString(cursor3.getColumnIndex("nominal")));

        }

        prosantaseUp = (pemasukanSkrg / grandSaldo) * 100;
        binding.tvProsentaseUp.setText(String.valueOf(Math.round(prosantaseUp) + "%"));

        prosentaseDown = (pengeluaranSkrg / grandSaldo) * 100;
        binding.tvProsentaseDown.setText(String.valueOf(Math.round(prosentaseDown) + "%"));

        AndLog.ShowLog("InfoPersen", "Naik +" + prosantaseUp + "%" + " Turun-" + prosentaseDown + "%");

        binding.tvTotalUangKeseluruhan.setText(DecimalsFormat.priceWithoutDecimal(String.valueOf(grandSaldo)));
    }

    @SuppressLint("SetTextI18n")
    private void listPengeluaran() {
        Cursor cursorproduct = dbHelper.getAllPengeluaran();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPengeluaran(
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(2), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(5) // tgl transaksi
                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPengeluaran", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPengeluaranStatistikKeuangan = new APengeluaranStatistikKeuangan(data, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPengeluaranStatistikKeuangan.setAdapter(aPengeluaranStatistikKeuangan);
    }

    private ArrayList<Entry> listPengeluaranAllChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getAllPengeluaranChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                // add data chart
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    @SuppressLint("SetTextI18n")
    private void listPemasukanAll() {
        Cursor cursorproduct = dbHelper.getAllPemasukan();

        ArrayList<MPemasukan> ArrayPemasukan = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                ArrayPemasukan.add(new MPemasukan(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(1) // nominal
                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < ArrayPemasukan.size(); a++) {
            nominal = Integer.parseInt(ArrayPemasukan.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (ArrayPemasukan.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPemasukanAll", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanStatistikKeuangan = new APemasukanStatistikKeuangan(ArrayPemasukan, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPemasukanStatistikKeuangan.setAdapter(aPemasukanStatistikKeuangan);
    }

    private ArrayList<Entry> listPemasukanAllChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getAllPemasukanChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                // add data chart
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPemasukanBulanIni() {
        Cursor cursorproduct = dbHelper.getFilterPemasukanMonthly();

        ArrayList<MPemasukan> ArrayPemasukan = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                ArrayPemasukan.add(new MPemasukan(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < ArrayPemasukan.size(); a++) {
            nominal = Integer.parseInt(ArrayPemasukan.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (ArrayPemasukan.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPemasukanAll", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanStatistikKeuangan = new APemasukanStatistikKeuangan(ArrayPemasukan, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPemasukanStatistikKeuangan.setAdapter(aPemasukanStatistikKeuangan);
    }

    private ArrayList<Entry> listPemasukanBulanIniChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPemasukanMonthlyLineChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPengeluaranBulanIni() {
        Cursor cursorproduct = dbHelper.getFilterPengeluaranMonthly();


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPengeluaran(
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(2), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(5) // tgl transaksi

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPengeluaran", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPengeluaranStatistikKeuangan = new APengeluaranStatistikKeuangan(data, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPengeluaranStatistikKeuangan.setAdapter(aPengeluaranStatistikKeuangan);
    }

    private ArrayList<Entry> listPengeluaranBulanIniChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPengeluaranMonthlyLineChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPemasukanWeekly() {
        Cursor cursorproduct = dbHelper.getFilterPemasukanWeekly();

        ArrayList<MPemasukan> ArrayPemasukan = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                ArrayPemasukan.add(new MPemasukan(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < ArrayPemasukan.size(); a++) {
            nominal = Integer.parseInt(ArrayPemasukan.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (ArrayPemasukan.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPemasukanAll", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanStatistikKeuangan = new APemasukanStatistikKeuangan(ArrayPemasukan, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPemasukanStatistikKeuangan.setAdapter(aPemasukanStatistikKeuangan);
    }

    private ArrayList<Entry> listPemasukanWeeklyChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPemasukanWeeklyLineChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPengeluaranWeekly() {
        Cursor cursorproduct = dbHelper.getFilterPengeluaranWeekly();


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPengeluaran(
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(2), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(5) // tgl transaksi

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPengeluaran", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPengeluaranStatistikKeuangan = new APengeluaranStatistikKeuangan(data, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPengeluaranStatistikKeuangan.setAdapter(aPengeluaranStatistikKeuangan);
    }

    private ArrayList<Entry> listPengeluaranWeeklyChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPengeluaranWeeklyLineChart();


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                String strdate = cursorproduct.getString(8) + "-" + cursorproduct.getString(9) + "-" + cursorproduct.getString(10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date startDate = null;
                try {
                    startDate = dateFormat.parse(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date nowDate = null;
                try {
                    nowDate = dateFormat.parse("01-01-2022");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (startDate != null && nowDate != null) {
                    String days = String.valueOf((Math.abs(nowDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
                    dateFloat = Float.parseFloat(days);
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPemasukanHariIni() {
        Cursor cursorproduct = dbHelper.getFilterPemasukanDay();

        ArrayList<MPemasukan> ArrayPemasukan = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                ArrayPemasukan.add(new MPemasukan(
                        cursorproduct.getString(5), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < ArrayPemasukan.size(); a++) {
            nominal = Integer.parseInt(ArrayPemasukan.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));
        AndLog.ShowLog("totalPemasukan", DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (ArrayPemasukan.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPemasukanAll", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanStatistikKeuangan = new APemasukanStatistikKeuangan(ArrayPemasukan, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPemasukanStatistikKeuangan.setAdapter(aPemasukanStatistikKeuangan);
    }

    private ArrayList<Entry> listPemasukanHariIniChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPemasukanDailyLineChart();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                String[] date = cursorproduct.getString(13).split(",");
                String[] time = date[1].split(":");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date timeFormat = null;
                try {
                    timeFormat = dateFormat.parse(time[0] + ":" + time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (timeFormat != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timeFormat);
                    int hourToSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60;
                    int minutesToSeconds = calendar.get(Calendar.MINUTE) * 60;

                    int totalSeconds = hourToSeconds + minutesToSeconds;
                    dateFloat = Float.parseFloat(String.valueOf(totalSeconds));
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private void listPengeluaranHariIni() {

        Cursor cursorproduct = dbHelper.getFilterPengeluaranDay();


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPengeluaran(
                        cursorproduct.getString(1), // nominal
                        cursorproduct.getString(2), // diambil dari
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(5) // tgl transaksi

                ));
            } while (cursorproduct.moveToNext());
        }

        int nominal = 0;
        int total = 0;
        for (int a = 0; a < data.size(); a++) {
            nominal = Integer.parseInt(data.get(a).getNominal());
            total = total + nominal;
        }

        binding.tvTotalPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(String.valueOf(total)));

        if (data.isEmpty()) {
            AndLog.ShowLog("LaporanKeuangan.this.listPengeluaran", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPengeluaranStatistikKeuangan = new APengeluaranStatistikKeuangan(data, LaporanKeuangan.this, mDatabase);

        //adding the adapter to listview
        binding.rvPengeluaranStatistikKeuangan.setAdapter(aPengeluaranStatistikKeuangan);
    }

    private ArrayList<Entry> listPengeluaranHariIniChart() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct = dbHelper.getPengeluaranDailyLineChart();
        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                String[] date = cursorproduct.getString(13).split(",");
                String[] time = date[1].split(":");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getDefault());

                Date timeFormat = null;
                try {
                    timeFormat = dateFormat.parse(time[0] + ":" + time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                float dateFloat = 0;
                if (timeFormat != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timeFormat);
                    int hourToSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60;
                    int minutesToSeconds = calendar.get(Calendar.MINUTE) * 60;

                    int totalSeconds = hourToSeconds + minutesToSeconds;
                    dateFloat = Float.parseFloat(String.valueOf(totalSeconds));
                }

                dataVals.add(new Entry(dateFloat, // tgl transaksi
                        cursorproduct.getInt(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (dataVals.isEmpty()) {
            dataVals.add(new Entry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

}