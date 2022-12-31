/*
 * SiKAS
 * exportRecanaKeuExcel.java
 *
 * Created by @iqbalmafi on 28/11/2022
 * Copyright © 2022 Iqbal Mafi. All right reserved.
 */

package com.awandigital.sikas.utils.export_file.excel;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.awandigital.sikas.model.MHutangDetail;
import com.awandigital.sikas.utils.DecimalsFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class exportDetailRiwayatHutangExcel {
    static Workbook workbook;
    static Sheet sheet;
    static Cell cell;

    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     * @param dataList - Contains the actual data to be displayed in excel
     */
    public static boolean exportDataIntoWorkbook(Context context, String fileName, List<MHutangDetail> dataList, String title, String targetDana, String statusUtang) {
        boolean isWorkbookWrittenIntoStorage;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formattedDate = sdf.format(new Date());

        // Check if available and not read only
        if (!isStoragePermissionGranted(context)) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet("Semua Data");
        sheet.setColumnWidth(1, (15 * 200));
        sheet.setColumnWidth(2, (15 * 500));

        CellStyle cellStyle = workbook.createCellStyle();

        setHeaderRow(dataList, title, targetDana, statusUtang);
        setHeaderRow2(cellStyle);
        fillDataIntoExcel(dataList);
        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName + formattedDate + ".xls");

        return isWorkbookWrittenIntoStorage;
    }

    /**
     * Checks if Storage is READ-ONLY
     *
     * @return boolean
     */
    public static boolean isStoragePermissionGranted(Context context) {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    /**
     * Setup header cell style
     */
    private static void setHeaderCellStyle() {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    /**
     * Setup Header Row
     */
    private static void setHeaderRow(List<MHutangDetail> dataList, String title, String targetDana, String statusUtang) {
        // title
        Row rowTitleRencana = sheet.createRow(0);
        cell = rowTitleRencana.createCell(0);
        cell.setCellValue("Keterangan");
        cell = rowTitleRencana.createCell(2);
        cell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // title
        Row rowTitleStatus = sheet.createRow(1);
        cell = rowTitleStatus.createCell(0);
        cell.setCellValue("Status Utang");
        cell = rowTitleStatus.createCell(2);
        cell.setCellValue(statusUtang);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // target dana
        Row rowTargetDana = sheet.createRow(2);
        cell = rowTargetDana.createCell(0);
        cell.setCellValue("Total Utang");
        cell = rowTargetDana.createCell(2);
        cell.setCellValue("Rp. " + DecimalsFormat.priceWithoutDecimal(targetDana));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));

        // total dana
        Row rowDanaTerkumpul = sheet.createRow(3);
        cell = rowDanaTerkumpul.createCell(0);
        cell.setCellValue("Utang Terbayar");
        cell = rowDanaTerkumpul.createCell(2);
        cell.setCellValue("Rp. " + DecimalsFormat.priceWithoutDecimal(String.valueOf(countTotalDibayar(dataList))));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));

        // kekurangan dana
        Row rowKekuranganData = sheet.createRow(4);
        cell = rowKekuranganData.createCell(0);
        cell.setCellValue("Kekurangan");
        cell = rowKekuranganData.createCell(2);
        cell.setCellValue("Rp. " + DecimalsFormat.priceWithoutDecimal(String.valueOf(kekuranganDana(dataList, Integer.parseInt(targetDana)))));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
    }

    static int kekuranganDana(List<MHutangDetail> dataList, int targetDana) {
        int kekuranagan = targetDana - countTotalDibayar(dataList);
        return Math.max(kekuranagan, 0);
    }

    static int countTotalDibayar(List<MHutangDetail> dataList) {
        int totalPemasukan = 0;
        for (MHutangDetail semua : dataList) {
            int nominal = Integer.parseInt(semua.getJml_cicil());
            totalPemasukan += nominal;
        }
        return totalPemasukan;
    }

    /**
     * Setup Header2 Row
     */
    private static void setHeaderRow2(CellStyle cellStyle) {
        sheet.setColumnWidth(0, (15 * 80));
        sheet.setColumnWidth(1, (15 * 500));
        sheet.setColumnWidth(2, (15 * 500));

        Row row = sheet.createRow(6);

        cell = row.createCell(0);
        cell.setCellValue("No");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Tanggal Transaksi");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Nominal");
        cell.setCellStyle(cellStyle);
    }

    /**
     * Fills Data into Excel Sheet
     * <p>
     * NOTE: Set row index as i+1 since 0th index belongs to header row
     *
     * @param dataList - List containing data to be filled into excel
     */
    private static void fillDataIntoExcel(List<MHutangDetail> dataList) {
        int firstitem = 7;
        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + firstitem);
            MHutangDetail semua = dataList.get(i);
            // Create Cells for each row
            cell = rowData.createCell(0);
            // numbering
            cell.setCellValue(i + 1);

            // tanggal
            cell = rowData.createCell(1);
            cell.setCellValue(semua.getTgl_transaksi());

            // kategori
            cell = rowData.createCell(2);
            cell.setCellValue(semua.getNominal());
            ;
        }
    }

    /**
     * Store Excel Workbook in external storage
     *
     * @param context  - application context
     * @param fileName - name of workbook which will be stored in device
     * @return boolean - returns state whether workbook is written into storage or not
     */
    private static boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Log.e(TAG, "Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save file due to Exception: ", e);
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSuccess) {
            Toast.makeText(context, "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "File gagal disimpan", Toast.LENGTH_SHORT).show();
        }


        return isSuccess;
    }
}
