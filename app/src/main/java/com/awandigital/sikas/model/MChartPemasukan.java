package com.awandigital.sikas.model;

import com.anychart.chart.common.dataentry.DataEntry;

public class MChartPemasukan extends DataEntry {
    String waktu;
    Double v1,v2,v3;

    public MChartPemasukan(String waktu, Double v1, Double v2, Double v3) {
        this.waktu = waktu;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public Double getV1() {
        return v1;
    }

    public void setV1(Double v1) {
        this.v1 = v1;
    }

    public Double getV2() {
        return v2;
    }

    public void setV2(Double v2) {
        this.v2 = v2;
    }

    public Double getV3() {
        return v3;
    }

    public void setV3(Double v3) {
        this.v3 = v3;
    }
}
