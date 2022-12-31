package com.awandigital.sikas.activity;

import static com.awandigital.sikas.db.DatabaseHelper.database_name;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ATujuanFinancial;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.databinding.ActivityTujuanFinancialBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.model.MKategori;
import com.awandigital.sikas.model.MTujuanFinansial;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class TujuanFinancialActivity extends AppCompatActivity {
    ActivityTujuanFinancialBinding binding;
    ATujuanFinancial aTujuanFinancial;

    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    EditText etTglTarget;
    Calendar cal = Calendar.getInstance();
    String[] nama_bulan;
    String fcreated_at;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String refresh = intent.getStringExtra("refresh");
            if (refresh.equals("list")) {
                listRencanaKeuangan();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTujuanFinancialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nama_bulan = getResources().getStringArray(R.array.month);
        dbHelper = new DatabaseHelper(TujuanFinancialActivity.this);
        mDatabase = openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).registerReceiver(broadcastReceiver,
                new IntentFilter("get-data-fp"));

        binding.rvKategoriTambahan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(TujuanFinancialActivity.this, 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvKategoriTambahan.setLayoutManager(layoutManager);
        listRencanaKeuangan();


        binding.imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                        R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_pilih_tujuan_finansial);

                LinearLayout ll_property = bsd.findViewById(R.id.ll_property);
                Objects.requireNonNull(ll_property).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                        // Bottom Sheet Dialog Tambahkan Tujuan Finansial
                        final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_tambah_tujuan_finansial);
                        TextView tvBack = bsd.findViewById(R.id.tv_back);
                        EditText etNamaTujuanFinansial = bsd.findViewById(R.id.etNamaKebutuhanFinansial);
                        EditText etTargetBudget = bsd.findViewById(R.id.etNominal);

                        Objects.requireNonNull(etTargetBudget).addTextChangedListener(new NumberTextWatcherForThousand(etTargetBudget));
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        Button btSimpan = bsd.findViewById(R.id.btTambahkan);

                        Objects.requireNonNull(etNamaTujuanFinansial).setText("Property");
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        int bulan = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        etTglTarget.setText(String.valueOf(nama_bulan[bulan] + " " + year));

                        if (tvBack != null) {
                            tvBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bsd.dismiss();
                                }
                            });
                        }

                        etTglTarget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Bottom Sheet Dialog 3
                                final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                        R.style.BottomShetDialogTheme);
                                bsd.setContentView(R.layout.lbs_pilih_tanggal);

                                int bulan;

                                NumberPicker npBulan, npTahun;
                                npBulan = bsd.findViewById(R.id.npBulan);
                                npTahun = bsd.findViewById(R.id.npTahun);


                                bulan = cal.get(Calendar.MONTH);

                                int year = cal.get(Calendar.YEAR);
                                npTahun.setMinValue(2021);
                                npTahun.setMaxValue(2222);
                                npTahun.setValue(year);

                                npBulan.setMinValue(0);
                                npBulan.setMaxValue(11);
                                npBulan.setDisplayedValues(nama_bulan);
                                npBulan.setValue(bulan);

                                TextView tvOK = bsd.findViewById(R.id.tvOK);
                                Objects.requireNonNull(tvOK).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        etTglTarget.setText(String.valueOf(nama_bulan[npBulan.getValue()] + " " +
                                                npTahun.getValue()));
                                        bsd.dismiss();
                                    }
                                });

                                TextView tvCancel = bsd.findViewById(R.id.tvBatal);
                                Objects.requireNonNull(tvCancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bsd.dismiss();
                                    }
                                });
                                bsd.show();
                            }

                        });

                        Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Fp = etNamaTujuanFinansial.getText().toString().trim();
                                String targetBudget = NumberTextWatcherForThousand.trimCommaOfString(etTargetBudget.getText().toString().trim());
                                String targetWaktu = etTglTarget.getText().toString().trim();

                                dbHelper.addRencanaKeuangan(Fp, targetBudget, targetWaktu, fcreated_at);

                                Intent intent = new Intent("get-data-fp");
                                intent.putExtra("refresh", "list");
                                LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).sendBroadcast(intent);

                                GlobalToast.ShowToast(TujuanFinancialActivity.this, "Rencan Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });

                        bsd.show();
                    }
                });

                LinearLayout ll_pendidikan = bsd.findViewById(R.id.ll_pendidikan);
                Objects.requireNonNull(ll_pendidikan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();

                        // Bottom Sheet Dialog Tambahkan Tujuan Finansial
                        final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_tambah_tujuan_finansial);
                        TextView tvBack = bsd.findViewById(R.id.tv_back);
                        EditText etNamaTujuanFinansial = bsd.findViewById(R.id.etNamaKebutuhanFinansial);
                        EditText etTargetBudget = bsd.findViewById(R.id.etNominal);
                        etTargetBudget.addTextChangedListener(new NumberTextWatcherForThousand(etTargetBudget));
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        Button btSimpan = bsd.findViewById(R.id.btTambahkan);

                        Objects.requireNonNull(etNamaTujuanFinansial).setText("Pendidikan");
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        int bulan = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        etTglTarget.setText(String.valueOf(nama_bulan[bulan] + " " + year));

                        if (tvBack != null) {
                            tvBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bsd.dismiss();
                                }
                            });
                        }

                        etTglTarget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Bottom Sheet Dialog 3
                                final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                        R.style.BottomShetDialogTheme);
                                bsd.setContentView(R.layout.lbs_pilih_tanggal);

                                int bulan;

                                NumberPicker npBulan, npTahun;
                                npBulan = bsd.findViewById(R.id.npBulan);
                                npTahun = bsd.findViewById(R.id.npTahun);


                                bulan = cal.get(Calendar.MONTH);

                                int year = cal.get(Calendar.YEAR);
                                npTahun.setMinValue(2021);
                                npTahun.setMaxValue(2222);
                                npTahun.setValue(year);

                                npBulan.setMinValue(0);
                                npBulan.setMaxValue(11);
                                npBulan.setDisplayedValues(nama_bulan);
                                npBulan.setValue(bulan);

                                TextView tvOK = bsd.findViewById(R.id.tvOK);
                                Objects.requireNonNull(tvOK).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        etTglTarget.setText(String.valueOf(nama_bulan[npBulan.getValue()] + " " +
                                                npTahun.getValue()));
                                        bsd.dismiss();
                                    }
                                });

                                TextView tvCancel = bsd.findViewById(R.id.tvBatal);
                                Objects.requireNonNull(tvCancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bsd.dismiss();
                                    }
                                });
                                bsd.show();
                            }

                        });

                        Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Fp = etNamaTujuanFinansial.getText().toString().trim();
                                String targetBudget = NumberTextWatcherForThousand.trimCommaOfString(etTargetBudget.getText().toString().trim());
                                String targetWaktu = etTglTarget.getText().toString().trim();

                                dbHelper.addRencanaKeuangan(Fp, targetBudget, targetWaktu, fcreated_at);

                                Intent intent = new Intent("get-data-fp");
                                intent.putExtra("refresh", "list");
                                LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).sendBroadcast(intent);
                                GlobalToast.ShowToast(TujuanFinancialActivity.this, "Rencana Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });

                        bsd.show();
                    }
                });

                LinearLayout ll_kesehatan = bsd.findViewById(R.id.ll_kesehatan);
                Objects.requireNonNull(ll_kesehatan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                        // Bottom Sheet Dialog Tambahkan Tujuan Finansial
                        final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_tambah_tujuan_finansial);
                        TextView tvBack = bsd.findViewById(R.id.tv_back);
                        EditText etNamaTujuanFinansial = bsd.findViewById(R.id.etNamaKebutuhanFinansial);
                        EditText etTargetBudget = bsd.findViewById(R.id.etNominal);

                        etTargetBudget.addTextChangedListener(new NumberTextWatcherForThousand(etTargetBudget));
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        Button btSimpan = bsd.findViewById(R.id.btTambahkan);

                        Objects.requireNonNull(etNamaTujuanFinansial).setText("Kesehatan");
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        int bulan = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        etTglTarget.setText(String.valueOf(nama_bulan[bulan] + " " + year));

                        if (tvBack != null) {
                            tvBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bsd.dismiss();
                                }
                            });
                        }

                        etTglTarget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Bottom Sheet Dialog 3
                                final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                        R.style.BottomShetDialogTheme);
                                bsd.setContentView(R.layout.lbs_pilih_tanggal);

                                int bulan;

                                NumberPicker npBulan, npTahun;
                                npBulan = bsd.findViewById(R.id.npBulan);
                                npTahun = bsd.findViewById(R.id.npTahun);


                                bulan = cal.get(Calendar.MONTH);

                                int year = cal.get(Calendar.YEAR);
                                npTahun.setMinValue(2021);
                                npTahun.setMaxValue(2222);
                                npTahun.setValue(year);

                                npBulan.setMinValue(0);
                                npBulan.setMaxValue(11);
                                npBulan.setDisplayedValues(nama_bulan);
                                npBulan.setValue(bulan);

                                TextView tvOK = bsd.findViewById(R.id.tvOK);
                                Objects.requireNonNull(tvOK).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        etTglTarget.setText(String.valueOf(nama_bulan[npBulan.getValue()] + " " +
                                                npTahun.getValue()));
                                        bsd.dismiss();
                                    }
                                });

                                TextView tvCancel = bsd.findViewById(R.id.tvBatal);
                                Objects.requireNonNull(tvCancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bsd.dismiss();
                                    }
                                });
                                bsd.show();
                            }

                        });

                        Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Fp = etNamaTujuanFinansial.getText().toString().trim();
                                String targetBudget = NumberTextWatcherForThousand.trimCommaOfString(etTargetBudget.getText().toString().trim());
                                String targetWaktu = etTglTarget.getText().toString().trim();

                                dbHelper.addRencanaKeuangan(Fp, targetBudget, targetWaktu, fcreated_at);

                                Intent intent = new Intent("get-data-fp");
                                intent.putExtra("refresh", "list");
                                LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).sendBroadcast(intent);

                                GlobalToast.ShowToast(TujuanFinancialActivity.this, "Rencan Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });

                        bsd.show();
                    }
                });

                LinearLayout ll_hiburan = bsd.findViewById(R.id.ll_hiburan);
                Objects.requireNonNull(ll_hiburan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bsd.dismiss();
                        // Bottom Sheet Dialog Tambahkan Tujuan Finansial
                        final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_tambah_tujuan_finansial);
                        TextView tvBack = bsd.findViewById(R.id.tv_back);
                        EditText etNamaTujuanFinansial = bsd.findViewById(R.id.etNamaKebutuhanFinansial);
                        EditText etTargetBudget = bsd.findViewById(R.id.etNominal);

                        etTargetBudget.addTextChangedListener(new NumberTextWatcherForThousand(etTargetBudget));
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        Button btSimpan = bsd.findViewById(R.id.btTambahkan);

                        Objects.requireNonNull(etNamaTujuanFinansial).setText("Hiburan");
                        etTglTarget = bsd.findViewById(R.id.etTargetWaktu);
                        int bulan = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        etTglTarget.setText(String.valueOf(nama_bulan[bulan] + " " + year));


                        tvBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bsd.dismiss();
                            }
                        });


                        etTglTarget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Bottom Sheet Dialog 3
                                final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                        R.style.BottomShetDialogTheme);
                                bsd.setContentView(R.layout.lbs_pilih_tanggal);

                                int bulan;

                                NumberPicker npBulan, npTahun;
                                npBulan = bsd.findViewById(R.id.npBulan);
                                npTahun = bsd.findViewById(R.id.npTahun);


                                bulan = cal.get(Calendar.MONTH);

                                int year = cal.get(Calendar.YEAR);
                                npTahun.setMinValue(2021);
                                npTahun.setMaxValue(2222);
                                npTahun.setValue(year);

                                npBulan.setMinValue(0);
                                npBulan.setMaxValue(11);
                                npBulan.setDisplayedValues(nama_bulan);
                                npBulan.setValue(bulan);

                                TextView tvOK = bsd.findViewById(R.id.tvOK);
                                Objects.requireNonNull(tvOK).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        etTglTarget.setText(String.valueOf(nama_bulan[npBulan.getValue()] + " " +
                                                npTahun.getValue()));
                                        bsd.dismiss();
                                    }
                                });

                                TextView tvCancel = bsd.findViewById(R.id.tvBatal);
                                Objects.requireNonNull(tvCancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bsd.dismiss();
                                    }
                                });
                                bsd.show();
                            }

                        });

                        Objects.requireNonNull(btSimpan).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Fp = etNamaTujuanFinansial.getText().toString().trim();
                                String targetBudget = NumberTextWatcherForThousand.trimCommaOfString(etTargetBudget.getText().toString().trim());
                                String targetWaktu = etTglTarget.getText().toString().trim();

                                dbHelper.addRencanaKeuangan(Fp, targetBudget, targetWaktu, fcreated_at);

                                Intent intent = new Intent("get-data-fp");
                                intent.putExtra("refresh", "list");
                                LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).sendBroadcast(intent);

                                GlobalToast.ShowToast(TujuanFinancialActivity.this, "Rencan Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });

                        bsd.show();
                    }
                });

                
                ImageView imLainnya = bsd.findViewById(R.id.imLainnya);
                imLainnya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final BottomSheetDialog bsd = new BottomSheetDialog(TujuanFinancialActivity.this,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_lainnya);
                        TextView tvBack = bsd.findViewById(R.id.tv_lainnya);
                        tvBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bsd.dismiss();
                            }
                        });
                        bsd.show();
                    }
                });
                
                bsd.show();
            }
        });

        binding.tvSaldoUmum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("get-data-saldoumum");
                intent.putExtra("jenis", "saldoUmum");
                LocalBroadcastManager.getInstance(TujuanFinancialActivity.this).sendBroadcast(intent);
                TujuanFinancialActivity.this.finish();
            }
        });
    }

    private void listRencanaKeuangan() {

        Cursor cursorproduct = dbHelper.getAllRencanaKeuangan();

        ArrayList<MTujuanFinansial> data = new ArrayList<>();

        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new MTujuanFinansial(
                        cursorproduct.getString(0), // id
                        cursorproduct.getString(1), // nama rencana keuangan
                        cursorproduct.getString(2), // target dana
                        cursorproduct.getString(3), // dana terkumpul
                        cursorproduct.getString(7)
                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            GlobalToast.ShowToast(TujuanFinancialActivity.this, "No items Found in database");
        }

        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        aTujuanFinancial = new ATujuanFinancial(data, TujuanFinancialActivity.this, mDatabase);

        //adding the adapter to listview
        binding.rvKategoriTambahan.setAdapter(aTujuanFinancial);
    }

}