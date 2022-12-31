package com.awandigital.sikas.fragment.transaksi.rencana;

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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.awandigital.sikas.R;
import com.awandigital.sikas.adapter.ARencanaKeuanganAll;
import com.awandigital.sikas.adapter.AdapterRencanaKeuangan;
import com.awandigital.sikas.databinding.FragmentRencanaKeuanganBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.model.rencana_keuangan;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.NumberTextWatcherForThousand;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RencanaKeuanganFragment extends Fragment {

    SimpleDateFormat created_at = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", new Locale("id", "ID"));
    FragmentRencanaKeuanganBinding binding;
    ARencanaKeuanganAll adapterRencanaKeuangan;
    Activity mActivity;
    String getBulan, getTahun;
    EditText etTglTarget;
    Calendar cal = Calendar.getInstance();
    String[] nama_bulan;
    SQLiteDatabase mDatabase;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    ArrayList<rencana_keuangan> data = new ArrayList<>();
    ArrayList<rencana_keuangan> dataCopy = new ArrayList<>();


    String fcreated_at;
    Handler handler;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String refresh = intent.getStringExtra("refresh");
            if (refresh.equals("list")) {
                listDataRencanaKeuangan();
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRencanaKeuanganBinding.inflate(inflater, container, false);
        nama_bulan = getResources().getStringArray(R.array.month);
        mActivity = getActivity();

        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        pDialog = new OwnProgressDialog(mActivity);

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver,
                new IntentFilter("get-data-fp"));

        binding.btTambahRencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Bottom Sheet Dialog Pilih TujuanFinansial
                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
                        R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_pilih_tujuan_finansial);
                ImageView imLainnya = bsd.findViewById(R.id.imLainnya);

                LinearLayout ll_property = bsd.findViewById(R.id.ll_property);
                Objects.requireNonNull(ll_property).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                        // Bottom Sheet Dialog Tambahkan Tujuan Finansial
                        final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

                                GlobalToast.ShowToast(mActivity, "Rencana Keuangan Berhasil Ditambahkan");
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
                        final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                                GlobalToast.ShowToast(mActivity, "Rencan Keuangan Berhasil Ditambahkan");
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
                        final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

                                GlobalToast.ShowToast(mActivity, "Rencan Keuangan Berhasil Ditambahkan");
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
                        final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

                                GlobalToast.ShowToast(mActivity, "Rencan Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });

                        bsd.show();
                    }
                });

                Objects.requireNonNull(imLainnya).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Bottom Sheet Dialog 2
                        bsd.dismiss();
                        final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
                                R.style.BottomShetDialogTheme);
                        bsd.setContentView(R.layout.lbs_lainnya2);
                        TextView tvBack = bsd.findViewById(R.id.tv_lainnya);
                        EditText etNamaTujuanFinansial = bsd.findViewById(R.id.etNamaKebutuhanFinansial);
                        EditText etTargetBudget = bsd.findViewById(R.id.etNominal);

                        etTargetBudget.addTextChangedListener(new NumberTextWatcherForThousand(etTargetBudget));
                        etTglTarget = bsd.findViewById(R.id.etTentukanWaktuTarget);
                        Button btSimpan = bsd.findViewById(R.id.btTambahkan);

                        int bulan = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        etTglTarget.setText(String.valueOf(nama_bulan[bulan] + " " + year));


                        Objects.requireNonNull(tvBack).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bsd.dismiss();
                            }
                        });


                        etTglTarget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Bottom Sheet Dialog 3
                                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
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
                                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

                                GlobalToast.ShowToast(mActivity, "Rencan Keuangan Berhasil Ditambahkan");
                                bsd.dismiss();
                            }
                        });
                        bsd.show();
                    }
                });

                bsd.show();
            }
        });

        binding.rvRencanaKeuangan.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1,
                GridLayoutManager.VERTICAL, false);
        binding.rvRencanaKeuangan.setLayoutManager(layoutManager);


        binding.Swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                listDataRencanaKeuangan();
            }
        });


        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {

                fcreated_at = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault()).format(new Date());
                AndLog.ShowLog("asd", fcreated_at);
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(r);

        binding.etCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapterRencanaKeuangan.filter(String.valueOf(charSequence), dataCopy);
                adapterRencanaKeuangan.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return binding.getRoot();
    }

    private void listDataRencanaKeuangan() {
        data.clear();
        binding.Swipe.setRefreshing(true);
        Cursor cursorproduct = dbHelper.getAllRencanaKeuangan();


        //if the cursor has some data
        if (cursorproduct.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                data.add(new rencana_keuangan(
                        cursorproduct.getString(0), //
                        cursorproduct.getString(1), // nama rencana keuangan
                        cursorproduct.getString(3), // target dana
                        cursorproduct.getString(2)// dana terkumpul

                ));
            } while (cursorproduct.moveToNext());
        }
        if (data.isEmpty()) {
            GlobalToast.ShowToast(mActivity, "No items Found in database");
        }

        binding.Swipe.setRefreshing(false);
        //closing the cursor
        cursorproduct.close();
        dataCopy.clear();
        dataCopy.addAll(data);
        //creating the adapter object
        adapterRencanaKeuangan = new ARencanaKeuanganAll(data, mActivity, mDatabase);

        //adding the adapter to listview
        binding.rvRencanaKeuangan.setAdapter(adapterRencanaKeuangan);

    }

    @Override
    public void onResume() {
        super.onResume();
        listDataRencanaKeuangan();
    }
}