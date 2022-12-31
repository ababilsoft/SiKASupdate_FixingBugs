package com.awandigital.sikas.utils.formatChart;

import com.awandigital.sikas.utils.AndLog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


public class hourMinute implements IAxisValueFormatter {

    private final LineChart chart;

    public hourMinute(LineChart chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int val = (int) value / 60;

        return intToStringTimeFormat(val);
    }

    public static String intToStringTimeFormat(int time) {
        String strTemp;
        int minutes = time / 60;
        int seconds = time % 60;

        if (minutes < 10)
            strTemp = "0" + minutes + ":";
        else
            strTemp = minutes + ":";

        if (seconds < 10)
            strTemp = strTemp + "0" + seconds;
        else
            strTemp = strTemp + seconds;

        return strTemp;
    }
}
