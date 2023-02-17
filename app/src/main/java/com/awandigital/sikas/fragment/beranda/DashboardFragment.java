package com.awandigital.sikas.fragment.beranda;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.BaseDetailTransaksiActivity;
import com.awandigital.sikas.adapter.APemasukanCatatanKeuangan;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.databinding.FragmentDashboardBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.model.MChartPemasukan;
import com.awandigital.sikas.model.MPemasukan;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.MyMarkerView;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.awandigital.sikas.utils.formatChart.hourMinute;
import com.awandigital.sikas.utils.formatChart.monthYear;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    Activity mActivity;
    SessionManager sessionManager;
    Integer saldoKU = 0, saldoFP = 0, grandSaldo = 0;
    APemasukanCatatanKeuangan aPemasukanCatatanKeuangan;
    SimpleDateFormat formattgl = new SimpleDateFormat("dd", new Locale("id", "ID"));
    String ftanggal, ftanggal7, getTime, jT = "pemasukan";
    AnyChartView anyChartView;
    Cartesian cartesian;

    //    UJI COBA --
// variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    //    --------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(mActivity);
        sessionManager = new SessionManager(mActivity);

        Calendar c = Calendar.getInstance();
        ftanggal = formattgl.format(c.getTime());
        binding.tvNama.setText("Hi, " + sessionManager.getNama());

//        anyChartView = binding.anyChartPemasukan;
//        cartesian = AnyChart.column();
//        anyChartView.setChart(cartesian);

        binding.btPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_blue));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_white));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_100));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_500));
                jT = "pemasukan";

                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                lineChart("day", jT);
            }
        });

        binding.btPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jT = "pengeluaran";
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_white));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_button_blue));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_500));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(requireContext(), R.color.netral_100));

                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                lineChart("day", jT);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BaseDetailTransaksiActivity.class);
                i.putExtra("prom", "3");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        binding.btHarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

//                linePDay();
                lineChart("day", jT);
            }
        });

        binding.btMingguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btHarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btMingguan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

//                linePWeekly();
                lineChart("weekly", jT);
            }
        });

        binding.btBulanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btHarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.thebutt_primari100));
                binding.btBulanan.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_300));

                lineChart("monthly", jT);
            }
        });

//        linePDay();
        lineChart("day", jT);
        barChart();
        AndLog.ShowLog("LOG_HARIAN", "range: " + ftanggal + "-" + ftanggal);

        return binding.getRoot();
    }

    private void barChart() {
//        // initializing variable for bar chart.
//        barChart = binding.chartPengeluaran;
//
//        // calling method to get bar entries.
//        getBarEntries();
//
//        // creating a new bar data set.
//        barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");
//
//        // creating a new bar data and
//        // passing our bar data set.
//        barData = new BarData(barDataSet);
//
//        // below line is to set data
//        // to our bar chart.
//        barChart.setData(barData);
//
//        // adding color to our bar data set.
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        // setting text color.
//        barDataSet.setValueTextColor(Color.BLACK);
//
//        // setting text size
//        barDataSet.setValueTextSize(12f);
//        barChart.getDescription().setEnabled(false);

    }

    @SuppressLint("Range")
    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
//        barEntriesArrayList.add(new BarEntry(1f, 4));
//        barEntriesArrayList.add(new BarEntry(2f, 6));
//        barEntriesArrayList.add(new BarEntry(3f, 8));
//        barEntriesArrayList.add(new BarEntry(4f, 2));
//        barEntriesArrayList.add(new BarEntry(5f, 4));
//        barEntriesArrayList.add(new BarEntry(6f, 1));

        Cursor cursorproduct;

        cursorproduct = dbHelper.getFilterPengeluaranDay();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                barEntriesArrayList.add(new BarEntry(cursorproduct.getInt(cursorproduct.getColumnIndex("tgl_transaksi")), // tgl transaksi
                        cursorproduct.getInt(cursorproduct.getColumnIndex("nominal")) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (barEntriesArrayList.isEmpty()) {
            barEntriesArrayList.add(new BarEntry(0, 0));
            AndLog.ShowLog("chartLineDay", "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();
    }

    private void pieChart() {
        PieChart pieChart = binding.chartDashboard;

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("saldoumum", Integer.valueOf(saldoKU));
        typeAmountMap.put("saldofp", Integer.valueOf(saldoFP));

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#0096f4"));
        colors.add(Color.parseColor("#27ae61"));

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet()) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieChart.getLegend().setEnabled(false);
        pieData.setDrawValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void lineChart(String filter, String jt) {
        LineChart mpLineChart;
        mpLineChart = binding.chartPemasukan;

        // background color
        mpLineChart.setBackgroundColor(Color.WHITE);

        // disable description text
        mpLineChart.getDescription().setEnabled(false);

        // enable touch gestures
        mpLineChart.setTouchEnabled(true);

        // set listeners
        mpLineChart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_chart_dialog);

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
                LineDataSet lineDataSet;
                if (jt.equals("pemasukan")) {
                    lineDataSet = new LineDataSet(dataValues(), "Harian");
                    lineDataSet.setFillColor(Color.parseColor("#7199FA"));
                    lineDataSet.setColors(Color.parseColor("#164af0"));
                    lineDataSet.setCircleColor(Color.parseColor("#164af0"));
                } else {
                    lineDataSet = new LineDataSet(dataValuesPengeluaran(), "Harian");
                    lineDataSet.setFillColor(Color.parseColor("#f28a7f"));
                    lineDataSet.setColors(Color.parseColor("#eb5757"));
                    lineDataSet.setCircleColor(Color.parseColor("#eb5757"));
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

//                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });
                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
                break;
            }
            case "weekly": {
                yAxis.setAxisMinimum(0f);
                IAxisValueFormatter xAxisFormatter = new monthYear(mpLineChart);
                xAxis.setValueFormatter(xAxisFormatter);
                LineDataSet lineDataSet;
                if (jt.equals("pemasukan")) {
                    lineDataSet = new LineDataSet(dataValuesWeekly(), "Mingguan");
                    lineDataSet.setFillColor(Color.parseColor("#7199FA"));
                    lineDataSet.setColors(Color.parseColor("#164af0"));
                    lineDataSet.setCircleColor(Color.parseColor("#164af0"));
                } else {
                    lineDataSet = new LineDataSet(dataValuesPengeluaranWeekly(), "Mingguan");
                    lineDataSet.setFillColor(Color.parseColor("#f28a7f"));
                    lineDataSet.setColors(Color.parseColor("#eb5757"));
                    lineDataSet.setCircleColor(Color.parseColor("#eb5757"));
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

//                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
                break;
            }
            case "monthly": {
                yAxis.setAxisMinimum(0f);
                IAxisValueFormatter xAxisFormatter = new monthYear(mpLineChart);
                xAxis.setValueFormatter(xAxisFormatter);
                LineDataSet lineDataSet;
                if (jt.equals("pemasukan")) {
                    lineDataSet = new LineDataSet(dataValuesMonthly(), "Bulanan");
                    lineDataSet.setFillColor(Color.parseColor("#7199FA"));
                    lineDataSet.setColors(Color.parseColor("#164af0"));
                    lineDataSet.setCircleColor(Color.parseColor("#164af0"));
                } else {
                    lineDataSet = new LineDataSet(dataValuesPengeluaranMonthly(), "Bulanan");
                    lineDataSet.setFillColor(Color.parseColor("#f28a7f"));
                    lineDataSet.setColors(Color.parseColor("#eb5757"));
                    lineDataSet.setCircleColor(Color.parseColor("#eb5757"));
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

//                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return mpLineChart.getAxisLeft().getAxisMinimum();
                    }
                });

                LineData data = new LineData(dataSets);
                mpLineChart.setData(data);
                mpLineChart.invalidate();
                break;
            }
        }

        mpLineChart.animateX(1500);

    }

    private ArrayList<Entry> dataValues() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanDailyLineChart();

        if (cursorproduct.moveToFirst()) {
            do {
                String[] date = cursorproduct.getString(13).split(" ");
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

        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private ArrayList<Entry> dataValuesPengeluaran() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPengeluaranDailyLineChart();

        if (cursorproduct.moveToFirst()) {
            do {
                String[] date = cursorproduct.getString(13).split(" ");
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

        cursorproduct.close();

        Collections.sort(dataVals, (o1, o2) -> {
            int a = (int) o1.getX();
            int b = (int) o2.getX();
            return a - b;
        });

        return dataVals;
    }

    private ArrayList<Entry> dataValuesWeekly() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanWeeklyLineChart();

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

    private ArrayList<Entry> dataValuesPengeluaranWeekly() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPengeluaranWeeklyLineChart();

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

    private ArrayList<Entry> dataValuesMonthly() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanMonthlyLineChart();

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

    private ArrayList<Entry> dataValuesPengeluaranMonthly() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPengeluaranMonthlyLineChart();

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

    private ArrayList<String> dataHari() {
        ArrayList<String> dates = new ArrayList<String>();
        dates.add("01-Apr-2022");
        dates.add("02-Apr-2022");
        dates.add("03-Apr-2022");
        dates.add("04-Apr-2022");
        dates.add("05-Apr-2022");
        return dates;

    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public void onResume() {
        super.onResume();


        Cursor cursor = dbHelper.dataKasUmum();

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            if (cursor.getString(cursor.getColumnIndex("nominal")) == null) {
                saldoKU = 0;
            } else {
                saldoKU = Integer.valueOf(cursor.getString(cursor.getColumnIndex("nominal")));
            }
            binding.tvSaldoKasUmum.setText("Rp." + DecimalsFormat.priceWithoutDecimal(String.valueOf(saldoKU)));

        }


        Cursor cursor1 = dbHelper.dataSaldoFp();

        if (cursor1.moveToFirst() && cursor1.getCount() > 0) {
            if (cursor1.getString(cursor1.getColumnIndex("totalfp")) == null) {
                saldoFP = 0;
            } else {

                saldoFP = Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("totalfp")));
            }
            binding.tvSeluruhSaldoFP.setText("Rp." + DecimalsFormat.priceWithoutDecimal(String.valueOf(saldoFP)));
        }

        grandSaldo = saldoKU + saldoFP;
        binding.tvSaldo.setText(DecimalsFormat.priceWithoutDecimal(String.valueOf(grandSaldo)));

        pieChart();

        lineChart("day", jT);
        barChart();
    }

    private void listDataRencanaKeuangan() {

        Cursor cursorproduct = dbHelper.getAllPemasukan();

        ArrayList<MPemasukan> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MPemasukan(cursorproduct.getString(7), // tgl transaksi
                        cursorproduct.getString(2), // tujuan finansial
                        cursorproduct.getString(3), // kategori
                        cursorproduct.getString(1) // nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            GlobalToast.ShowToast(mActivity, "No items Pemasukan Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aPemasukanCatatanKeuangan = new APemasukanCatatanKeuangan(data, mActivity, mDatabase);

        //adding the adapter to listview
//        binding.rvPemasukanCatatanKeuangan.setAdapter(aPemasukanCatatanKeuangan);

    }
}