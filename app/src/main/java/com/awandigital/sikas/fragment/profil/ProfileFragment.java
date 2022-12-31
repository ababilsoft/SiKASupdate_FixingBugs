package com.awandigital.sikas.fragment.profil;

import static android.content.Context.MODE_PRIVATE;
import static com.awandigital.sikas.db.DatabaseHelper.database_name;
import static com.awandigital.sikas.db.DatabaseHelper.email;
import static com.awandigital.sikas.db.DatabaseHelper.image_profile;
import static com.awandigital.sikas.db.DatabaseHelper.nama;
import static com.awandigital.sikas.db.DatabaseHelper.username;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.awandigital.sikas.Login;
import com.awandigital.sikas.R;
import com.awandigital.sikas.activity.TujuanFinancialActivity;
import com.awandigital.sikas.activity.UbahProfile;
import com.awandigital.sikas.databinding.FragmentProfileBinding;
import com.awandigital.sikas.databinding.FragmentTransaksiBinding;
import com.awandigital.sikas.db.DatabaseHelper;
import com.awandigital.sikas.db.SessionManager;
import com.awandigital.sikas.utils.AndLog;
import com.awandigital.sikas.utils.GlobalToast;
import com.awandigital.sikas.utils.OwnProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileFragment extends Fragment {
    Activity mActivity;
    FragmentProfileBinding binding;
    private boolean isVisible = true;
    SessionManager sessionManager;
    OwnProgressDialog pDialog;
    DatabaseHelper dbHelper;
    SQLiteDatabase mDatabase;
    int progress = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mActivity = getActivity();
        pDialog = new OwnProgressDialog(mActivity);
        sessionManager = new SessionManager(mActivity);
        dbHelper = new DatabaseHelper(mActivity);
        mDatabase = mActivity.openOrCreateDatabase(database_name, MODE_PRIVATE, null);

        getDetailAkun();

        binding.tvKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
                        R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_alert_logout);
                TextView tvBatal = bsd.findViewById(R.id.tvBatal);
                tvBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                    }
                });

                Button btHapusAkun = bsd.findViewById(R.id.btLogout);
                btHapusAkun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sessionManager.isLoggedOut();
                        mActivity.finish();
                        Intent intent = new Intent(mActivity, Login.class);
                        startActivity(intent);
                        GlobalToast.ShowToast(mActivity, "Anda Telah Keluar dari Aplikasi");
                    }
                });

                bsd.show();
            }
        });

        binding.lineGrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivArrowUp.setVisibility(View.VISIBLE);
                binding.ivArrowDown.setVisibility(View.GONE);

                if (isVisible) {
                    binding.lineSelesaikanProfile.setVisibility(View.GONE);
                    binding.ivArrowUp.setVisibility(View.GONE);
                    binding.ivArrowDown.setVisibility(View.VISIBLE);
                    binding.lineSelesaikanProfile.setAlpha(0.0f);
                    isVisible = false;
                } else {
                    binding.lineSelesaikanProfile.setVisibility(View.VISIBLE);
                    binding.lineSelesaikanProfile.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setListener(null);
                    binding.ivArrowUp.setVisibility(View.VISIBLE);
                    binding.ivArrowDown.setVisibility(View.GONE);
                    isVisible = true;
                }
            }
        });

        binding.tvHapusAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bsd = new BottomSheetDialog(mActivity,
                        R.style.BottomShetDialogTheme);
                bsd.setContentView(R.layout.lbs_alert_hapus_akun);
                TextView tvBatal = bsd.findViewById(R.id.tvBatal);
                tvBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsd.dismiss();
                    }
                });

                Button btHapusAkun = bsd.findViewById(R.id.btHapus);
                btHapusAkun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        GlobalToast.ShowToast(mActivity, "Get it");
                        dbHelper.hapusAkun();
                        sessionManager.isLoggedOut();
                        mActivity.finish();
                        Intent intent = new Intent(mActivity, Login.class);
                        startActivity(intent);
                        GlobalToast.ShowToast(mActivity, "Anda Telah Keluar dari Aplikasi");
                    }
                });

                bsd.show();
            }
        });

        binding.ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UbahProfile.class);
                mActivity.startActivity(intent);
            }
        });

        return binding.getRoot();


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    void checkCompleteProfile() {
        if (binding.progressGrafik.getProgress() == 100) {
            binding.tvSelesaikan.setText("Profile Anda Sudah Lengkap");
            binding.lineSelesaikanProfile.setVisibility(View.GONE);
            binding.lineGrafik.setVisibility(View.GONE);
            binding.ivArrowUp.setVisibility(View.GONE);
            binding.ivArrowDown.setVisibility(View.GONE);
        }

    }

    @SuppressLint("Range")
    private void getDetailAkun() {
        Cursor cursor = dbHelper.getAllUser();

        if (cursor.moveToFirst()) {
            String getNamal = cursor.getString(cursor.getColumnIndex(nama));
            String getUsername = cursor.getString(cursor.getColumnIndex(username));
            String getEmail = cursor.getString(cursor.getColumnIndex(email));
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(image_profile));

            binding.tvNamaShow.setText(getNamal);
            binding.tvusernameShow.setText(getUsername);
            binding.tvNamaL.setText(getNamal);
            binding.tvUsername.setText(getUsername);
            binding.tvEmail.setText(getEmail);

            if (getNamal != null) {
                binding.progressGrafik.setProgress(25);
                binding.tvNamaPro.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheklist_hijau, 0);
            }

            if (getUsername != null) {
                binding.progressGrafik.setProgress(50);
                binding.tvUsernamePro.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheklist_hijau, 0);
            }

            if (getEmail != null) {
                binding.progressGrafik.setProgress(75);
                binding.tvEmailPro.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheklist_hijau, 0);
            }

            if (imgByte != null) {
                binding.progressGrafik.setProgress(100);
                binding.tvFotoPPro.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheklist_hijau, 0);
                if (imgByte.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    binding.ivImage.setImageBitmap(getResizedBitmap(bmp, 512));
                }
            }

            checkCompleteProfile();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDetailAkun();
    }
}