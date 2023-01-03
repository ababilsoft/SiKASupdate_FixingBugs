package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityGrafikRencanaKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.MyMarkerView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GrafikRencanaKeuangan extends AppCompatActivity {
    ActivityGrafikRencanaKeuanganBinding binding;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    String jT = "pemasukan";
    String namatransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrafikRencanaKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(GrafikRencanaKeuangan.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        namatransaksi = getIntent().getStringExtra("nama_transaksi");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.llPemasukan.setVisibility(View.VISIBLE);
        binding.llPengeluaran.setVisibility(View.GONE);
        binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_blue));
        binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_white));
        binding.btPemasukan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.netral_100));
        binding.btPengeluaran.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_200));

        binding.btPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jT = "pemasukan";

                binding.llPemasukan.setVisibility(View.VISIBLE);
                binding.llPengeluaran.setVisibility(View.GONE);
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_blue));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_white));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.netral_100));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_200));

                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                lineChart("day", jT);
            }
        });

        binding.btPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jT = "pengeluaran";

                binding.llPemasukan.setVisibility(View.GONE);
                binding.llPengeluaran.setVisibility(View.VISIBLE);
                binding.btPemasukan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_white));
                binding.btPengeluaran.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_blue));
                binding.btPemasukan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_200));
                binding.btPengeluaran.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.netral_100));

                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                lineChart("day", jT);
            }
        });

        binding.btHarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btHarian.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

//                linePDay();
                lineChart("day", jT);
            }
        });

        binding.btMingguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btHarian.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btMingguan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btBulanan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

//                linePWeekly();
                lineChart("weekly", jT);
            }
        });

        binding.btBulanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btHarian.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btHarian.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btMingguan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_tersier));
                binding.btMingguan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                binding.btBulanan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.thebutt_primari100));
                binding.btBulanan.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_300));

                lineChart("monthly", jT);
            }
        });
    }

    private void lineChart(String filter, String jt) {
        LineChart mpLineChart;
        mpLineChart = binding.lineChartGrafik;

        // background color
        mpLineChart.setBackgroundColor(Color.WHITE);

        // disable description text
        mpLineChart.getDescription().setEnabled(false);

        // enable touch gestures
        mpLineChart.setTouchEnabled(true);

        // set listeners
        mpLineChart.setDrawGridBackground(false);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(GrafikRencanaKeuangan.this, R.layout.custom_chart_dialog);

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
                LineDataSet lineDataSet;
                if (jt.equals("pemasukan")) {
                    lineDataSet = new LineDataSet(dataValues(namatransaksi), "Harian");
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
                    lineDataSet = new LineDataSet(dataValuesWeekly(namatransaksi), "Mingguan");
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
                    lineDataSet = new LineDataSet(dataValuesMonthly(namatransaksi), "Bulanan");
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

    private ArrayList<Entry> dataValues(String v) {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanHarianFinancialPlanning(v);

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

    private ArrayList<Entry> dataValuesWeekly(String v) {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanMingguanFinancialPlanning(v);

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

    private ArrayList<Entry> dataValuesMonthly(String v) {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        Cursor cursorproduct;

        cursorproduct = dbHelper.getPemasukanBulananFinancialPlanning(v);

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

}