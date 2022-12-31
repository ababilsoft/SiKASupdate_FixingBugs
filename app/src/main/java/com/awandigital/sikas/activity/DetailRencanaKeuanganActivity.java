package com.awandigital.sikas.activity;

import static android.content.ContentValues.TAG;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.nama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ADataDetailRencanaKeuangan;
import com.awandigital.sikas.adapter.ARencanaKeuanganAll;
import com.awandigital.sikas.databinding.ActivityDetailRencanaKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.model.MDetailDataRencanaKeuangan;
import com.awandigital.sikas.model.SuperHeroModel;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.DecimalsFormat;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.awandigital.sikas.utils.PDFUtils;
import com.awandigital.sikas.utils.export_file.excel.exportPemasukanExcel;
import com.awandigital.sikas.utils.export_file.excel.exportPengeluaranExcel;
import com.awandigital.sikas.utils.export_file.excel.exportRecanaKeuExcel;
import com.awandigital.sikas.utils.export_file.excel.exportSemuaExcel;
import com.awandigital.sikas.utils.export_file.pdf.exportPemasukanPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportPengeluaranPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportRencanaKeuPDF;
import com.awandigital.sikas.utils.export_file.pdf.exportSemuaPDF;
import com.awandigital.sikas.utils.pdfDocumentAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailRencanaKeuanganActivity extends AppCompatActivity {
    ActivityDetailRencanaKeuanganBinding binding;
    ADataDetailRencanaKeuangan aDataDetailRencanaKeuangan;
    ArrayList<MDetailDataRencanaKeuangan> data = new ArrayList<>();
    ArrayList<MDetailDataRencanaKeuangan> dataCopy = new ArrayList<>();
    SessionManager sessionManager;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    float dana_terkumpul, target_dana, prosentase;
    int dumProsentase;
    RadioButton rbPdf, rbExcel;

    // pdf
    private static final String FILE_PRINT = "last_file_print.pdf";
    List<SuperHeroModel> superHeroModels = new ArrayList<SuperHeroModel>();
    private AlertDialog dialog;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRencanaKeuanganBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pDialog = new OwnProgressDialog(DetailRencanaKeuanganActivity.this);
        sessionManager = new SessionManager(DetailRencanaKeuanganActivity.this);
        dbHelper = new DatabaseHelper(DetailRencanaKeuanganActivity.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        dialog = new AlertDialog.Builder(this).setCancelable(false).setMessage("Mohon Tunggu...").create();


        String namatujuan = getIntent().getStringExtra("namatujuan");
        binding.toolbarTitle.setText(namatujuan);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Cursor cursor = dbHelper.pemasukanRencanaKeuangan(namatujuan);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {

            if (cursor.getString(cursor.getColumnIndex("nominal")) == null &&
                    cursor.getString(cursor.getColumnIndex("nominal")).isEmpty()) {
                binding.tvPemasukan.setText("Rp0");
                AndLog.ShowLog("vpemasukan", cursor.getString(cursor.getColumnIndex("nominal")));
            } else {
                String nominal = cursor.getString(cursor.getColumnIndex("nominal"));
                binding.tvPemasukan.setText("Rp" + DecimalsFormat.priceWithoutDecimal(nominal));
            }
//            binding.tvPengeluaran.setText("Rp0");


        }

        Cursor cursor1 = dbHelper.pengeluaranRencanaKeuangan(namatujuan);

        if (cursor1.moveToFirst() && cursor1.getCount() > 0) {

            if (cursor1.getString(cursor1.getColumnIndex("nominal")) == null &&
                    cursor1.getString(cursor1.getColumnIndex("nominal")).isEmpty()) {
                AndLog.ShowLog("vpengeluaran", cursor1.getString(cursor1.getColumnIndex("nominal")));
                binding.tvPengeluaran.setText("Rp0");
            } else {
                String nominal = cursor1.getString(cursor1.getColumnIndex("nominal"));
                binding.tvPengeluaran.setText("Rp" + DecimalsFormat.priceWithoutDecimal(nominal));
            }
//            binding.tvPemasukan.setText("Rp0");

        }

        binding.rvDetailDataRencanaKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(DetailRencanaKeuanganActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvDetailDataRencanaKeuangan.setLayoutManager(layoutManager);
        listDataRencanaKeuangan(namatujuan);

//        ArrayList<MDetailDataRencanaKeuangan> arrayList = listDataRencanaKeuangan();
//        aDataDetailRencanaKeuangan = new ADataDetailRencanaKeuangan(arrayList);
//        binding.rvDetailDataRencanaKeuangan.setAdapter(aDataDetailRencanaKeuangan);

        binding.btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentCatatanKeuanganFilterBinding filterBinding;
//                filterBinding = FragmentCatatanKeuanganFilterBinding.inflate(inflater, container, false);
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        DetailRencanaKeuanganActivity.this, R.style.BottomShetDialogTheme);

                bsd.setContentView(R.layout.lbs_filter);

                bsd.show();
            }
        });

        binding.btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(
                        DetailRencanaKeuanganActivity.this, R.style.BottomShetDialogTheme);
                View bsdView = LayoutInflater.from(DetailRencanaKeuanganActivity.this)
                        .inflate(R.layout.lbs_catatan_keuangan_download,
                                DetailRencanaKeuanganActivity.this.findViewById(R.id.lbs_download));

                RadioGroup radioGroup = bsdView.findViewById(R.id.radioSex);
                RadioButton pdf = bsdView.findViewById(R.id.rbPdf);
                RadioButton excel = bsdView.findViewById(R.id.rbExcel);
                Button btnDownload = bsdView.findViewById(R.id.btSimpan);

                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cvTargetDana = DecimalsFormat.priceWithoutDecimal(String.valueOf(dana_terkumpul)).replace(",", "");
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        if (selectedId == pdf.getId()) {
                            if (data.isEmpty()) {
                                Toast.makeText(DetailRencanaKeuanganActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                exportRencanaKeuPDF.createPdf(v.getContext(), data, "Rencana_keuangan_" + namatujuan, true, namatujuan, cvTargetDana);
                                Toast.makeText(DetailRencanaKeuanganActivity.this, "File berhasil disimpan di folder Download", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(DetailRencanaKeuanganActivity.this, "File gagal disimpan", Toast.LENGTH_SHORT).show();
                            }
                            bsd.dismiss();
                        } else if (selectedId == excel.getId()) {
                            if (data.isEmpty()) {
                                Toast.makeText(DetailRencanaKeuanganActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            exportRecanaKeuExcel.exportDataIntoWorkbook(DetailRencanaKeuanganActivity.this, "Rencana_keuangan_" + namatujuan, data, namatujuan, cvTargetDana);
                            bsd.dismiss();
                        }
                    }
                });


                bsd.setContentView(bsdView);

                bsd.show();
            }
        });


        binding.btGrafikrencanaKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRencanaKeuanganActivity.this, GrafikRencanaKeuangan.class);
                intent.putExtra("nama_transaksi", namatujuan);
                startActivity(intent);
            }
        });

        binding.lineTargetRencanaKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(DetailRencanaKeuanganActivity.this, TargetKeuangan.class);
//                startActivity(intent);

            }
        });

        listData(namatujuan);

        binding.etCariCatatan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aDataDetailRencanaKeuangan.filter(String.valueOf(charSequence), dataCopy);
                aDataDetailRencanaKeuangan.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void createPDFFile(String path, String namatujuan) {
        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            // save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open
            document.open();
            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("PDF Test");
            document.addCreator("iqbalxjo");

            //font setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 20.0f;
            BaseFont fontName = BaseFont.createFont("res/font/fpoppins_medium.ttf", "UTF-8", BaseFont.EMBEDDED);

            //create title of document
            Font titleFont = new Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK);
            PDFUtils.addNewItem(document, "DETAIL RENCANA KEUANGAN", Element.ALIGN_CENTER, titleFont);

            //add more information
            Font textFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
            PDFUtils.addNewItem(document, "Dibuat Oleh :", Element.ALIGN_LEFT, titleFont);
            PDFUtils.addNewItem(document, "iqbal xjo", Element.ALIGN_LEFT, titleFont);

            PDFUtils.addLineSeparator(document);

            //detail
            PDFUtils.addLineSpace(document);
            PDFUtils.addNewItem(document, "DETAIL", Element.ALIGN_CENTER, titleFont);
            PDFUtils.addLineSeparator(document);

            Cursor cursorproduct = dbHelper.getDetailRencanaKeuangan(namatujuan);


            //if the cursor has some data
            if (cursorproduct.moveToFirst()) {
                //looping through all the records
                do {
                    //pushing each record in the employee list
                    data.add(new MDetailDataRencanaKeuangan(
                            cursorproduct.getString(0), //
                            cursorproduct.getString(5), // tgl
                            cursorproduct.getString(7), // jenis transaksi
                            cursorproduct.getString(4), // kategori
                            cursorproduct.getString(11), // catatan
                            cursorproduct.getString(1)// nominal

                    ));
                } while (cursorproduct.moveToNext());
            }
            if (data.isEmpty()) {
                AndLog.ShowLog("listDataDetailRencanaKeuangan", "Data Kosong");
//            GlobalToast.ShowToast(Detail, "No items Found in database");
            }

//        binding.Swipes.setRefreshing(false);
            //closing the cursor
            cursorproduct.close();

            //user RxJava, fetch data and add to pdf
            Observable.fromIterable(data)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        //on next
                        //each item, we will add detail
                        PDFUtils.addNewItemWithLeftAndRight(document, data.getNominal(), "", titleFont, textFont);
                        PDFUtils.addLineSeparator(document);
                        PDFUtils.addNewItem(document, data.getKategori(), Element.ALIGN_LEFT, textFont);
                        PDFUtils.addLineSeparator(document);
                    }, throwable -> {
                        //on eror
                        dialog.dismiss();
                        GlobalToast.ShowToast(this, "eror");
                    }, () -> {
                        // on complete
                        //when complete, close document
                        document.close();
                        dialog.dismiss();
                        GlobalToast.ShowToast(this, "suksesss");
                        printPDF();
                    });

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dialog.dismiss();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            pdfDocumentAdapter documentAdapter = new pdfDocumentAdapter(this, new StringBuilder(getAppPath())
                    .append(FILE_PRINT).toString(), FILE_PRINT);
            printManager.print("Document", documentAdapter, new PrintAttributes.Builder().build());

        } catch (Exception e) {
            GlobalToast.ShowToast(this, "" + e.getMessage());
        }
    }

    private String getAppPath() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + getResources().getString(R.string.app_name)
                + File.separator);
        if (!dir.exists()) dir.mkdir();
        return dir.getPath() + File.separator;

    }

    @SuppressLint("Range")
    private void listDataRencanaKeuangan(String nama_rencana) {
        Cursor cursor = dbHelper.dataRencanaKeuanganBy(nama_rencana);

        if (cursor.moveToFirst()) {

            target_dana = cursor.getFloat(cursor.getColumnIndex("target_dana"));
            dana_terkumpul = cursor.getFloat(cursor.getColumnIndex("dana_terkumpul"));
            String target_waktu = cursor.getString(cursor.getColumnIndex("target_waktu"));

            binding.tvTotalUang.setText(DecimalsFormat.priceWithoutDecimal(String.valueOf(dana_terkumpul)));

//            hitung prosentase
            prosentase = (dana_terkumpul / target_dana) * 100;
            AndLog.ShowLog("prosentaseTarget", String.valueOf(prosentase));

            binding.progressBar.setProgress((int) prosentase);
            binding.prosentase.setText(String.valueOf(Math.round(prosentase) + "%"));

        }


    }

    private void listData(String v) {
        data.clear();
//        binding.Swipes.setRefreshing(true);
        Cursor cursorproduct = dbHelper.getDetailRencanaKeuangan(v);


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MDetailDataRencanaKeuangan(
                        cursorproduct.getString(0), //
                        cursorproduct.getString(5), // tgl
                        cursorproduct.getString(7), // jenis transaksi
                        cursorproduct.getString(4), // kategori
                        cursorproduct.getString(11), // catatan
                        cursorproduct.getString(1)// nominal

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            AndLog.ShowLog("listDataDetailRencanaKeuangan", "Data Kosong");
//            GlobalToast.ShowToast(Detail, "No items Found in database");
        }

//        binding.Swipes.setRefreshing(false);
        //closing the cursor
        cursorproduct.close();
        dataCopy.clear();
        dataCopy.addAll(data);

        //creating the adapter object
        aDataDetailRencanaKeuangan = new ADataDetailRencanaKeuangan(data, DetailRencanaKeuanganActivity.this, mDatabase);

        //adding the adapter to listview
        binding.rvDetailDataRencanaKeuangan.setAdapter(aDataDetailRencanaKeuangan);

    }

}