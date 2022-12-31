/*
 * SiKAS
 * exportRencanaKeuPDF.java
 *
 * Created by @iqbalmafi on 28/11/2022
 * Copyright © 2022 Iqbal Mafi. All right reserved.
 */

package com.awandigital.sikas.utils.export_file.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.awandigital.sikas.model.MCatatanKeuanganSemua;
import com.awandigital.sikas.model.MDetailDataRencanaKeuangan;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class exportRencanaKeuPDF {
    private static final String TAG = exportSemuaPDF.class.getSimpleName();
    private static final Font FONT_CELL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static final Font FONT_COLUMN = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

    public static void createPdf(@NonNull Context mContext, List<MDetailDataRencanaKeuangan> dataList, @NonNull String fileName, boolean isPortrait, String titleRencana, String targetDana) throws Exception {


        if (fileName.equals("")) {
            throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formattedDate = sdf.format(new Date());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + formattedDate + ".pdf");

        if (file.exists()) {
            file.delete();
            Thread.sleep(50);
        }

        Document document = new Document();
        document.setMargins(24f, 24f, 32f, 32f);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
        pdfWriter.setFullCompression();

        document.open();

        setMetaData(document);

        addHeader(mContext, document, dataList, titleRencana, targetDana);
        addEmptyLine(document, 3);

        document.add(createDataTable(dataList));

        addEmptyLine(document, 2);
        document.close();

        try {
            pdfWriter.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error While Closing pdfWriter : " + ex.toString());
        }
    }

    public static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    public static void setMetaData(Document document) {
        document.addCreationDate();
    }

    public static void addHeader(Context mContext, Document document, List<MDetailDataRencanaKeuangan> dataList, String titleRencana, String targetDana) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 5});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Rencana Keuangan", FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph(titleRencana, FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Target Dana", FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Rp. " + DecimalsFormat.priceWithoutDecimal(targetDana), FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Dana Terkumpul", FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Rp. " + DecimalsFormat.priceWithoutDecimal(String.valueOf(totalDana(dataList))), FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Kekurangan", FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(5f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Rp. " + DecimalsFormat.priceWithoutDecimal(String.valueOf(kekuranganDana(dataList, Integer.parseInt(targetDana)))), FONT_COLUMN);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);
            table.addCell(cell);
        }

        document.add(table);
    }

    static int totalDana(List<MDetailDataRencanaKeuangan> dataList) {
        return countTotalPemasukan(dataList) - countTotalPengeluaran(dataList);
    }

    static int kekuranganDana(List<MDetailDataRencanaKeuangan> dataList, int targetDana) {
        int kekuranagan = targetDana - totalDana(dataList);
        return Math.max(kekuranagan, 0);
    }

    static int countTotalPemasukan(List<MDetailDataRencanaKeuangan> dataList) {
        int totalPemasukan = 0;
        for (MDetailDataRencanaKeuangan semua : dataList) {
            if (semua.getJenisTransaksi().equals("pemasukan")) {
                int nominal = Integer.parseInt(semua.getNominal());
                totalPemasukan += nominal;
            }
        }
        return totalPemasukan;
    }

    static int countTotalPengeluaran(List<MDetailDataRencanaKeuangan> dataList) {
        int totalPengeluaran = 0;
        for (MDetailDataRencanaKeuangan semua : dataList) {
            if (semua.getJenisTransaksi().equals("pengeluaran")) {
                int nominal = Integer.parseInt(semua.getNominal());
                totalPengeluaran += nominal;
            }
        }
        return totalPengeluaran;
    }

    public static PdfPTable createDataTable(List<MDetailDataRencanaKeuangan> dataList) throws DocumentException {
        PdfPTable table1 = new PdfPTable(5);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f});
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("No", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Tanggal Transaksi", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Kategori", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Pemasukan", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Pengeluaran", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            table1.addCell(cell);
        }
        int size = dataList.size();

        for (int i = 0; i < size; i++) {
            MDetailDataRencanaKeuangan semua = dataList.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(semua.getTglTransaksi(), FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(semua.getKategori(), FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7f);
            table1.addCell(cell);

            if (semua.getJenisTransaksi().equals("pemasukan")) {
                cell = new PdfPCell(new Phrase(DecimalsFormat.priceWithoutDecimal(semua.getNominal()), FONT_CELL));
            } else {
                cell = new PdfPCell(new Phrase("0", FONT_CELL));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7f);
            table1.addCell(cell);

            if (semua.getJenisTransaksi().equals("pengeluaran")) {
                cell = new PdfPCell(new Phrase(DecimalsFormat.priceWithoutDecimal(semua.getNominal()), FONT_CELL));
            } else {
                cell = new PdfPCell(new Phrase("0", FONT_CELL));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(7f);
            table1.addCell(cell);

        }

        return table1;
    }
}
