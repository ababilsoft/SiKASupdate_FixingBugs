package com.awandigital.sikas.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.awandigital.sikas.R;
import com.awandigital.sikas.databinding.ActivityDashboardBinding;
import com.awandigital.sikas.adapter.AdapterNav;


public class DashboardActivity extends AppCompatActivity {

    private AdapterNav mAdapter;
    private int[] layout;
    private ActivityDashboardBinding binding;

    private Button beranda;
    private Button transaksi;
    private Button utang;
    private Button profil;
    private ViewPager2 mViewPager;
    private AdapterNav mPagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewPager = findViewById(R.id.viewpager);
        beranda = findViewById(R.id.bt_beranda);
        transaksi = findViewById(R.id.bt_transaksi);
        utang = findViewById(R.id.bt_utang);
        profil = findViewById(R.id.bt_profile);

        beranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        utang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3);
            }
        });

        //harusnya fragmentManager
        mPagerViewAdapter = new AdapterNav(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(mPagerViewAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }
        });

        Intent intent = getIntent();
        String from = intent.getStringExtra("MainHome");

        if (from == "1") {
            mViewPager.setCurrentItem(0);
        } else if (from == "2") {
            mViewPager.setCurrentItem(1);
        } else if (from == "3") {
            mViewPager.setCurrentItem(2);
        } else if (from == "4") {
            mViewPager.setCurrentItem(3);
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeTabs(int position) {
        int fontSelect = ContextCompat.getColor(this, R.color.primary_500);
        int fontUnselect = ContextCompat.getColor(this, R.color.primary_300);

        Drawable homeAct, homeNon, TransaksiAct, TransaksiNon, HutangAct, HutangNon, ProfileAct, ProfileNon;

        homeAct = getResources().getDrawable(R.drawable.ic_home_active);
        homeAct.setBounds(0, 0, 60, 60);

        homeNon = getResources().getDrawable(R.drawable.ic_home_nonactive);
        homeNon.setBounds(0, 0, 60, 60);

        TransaksiAct = getResources().getDrawable(R.drawable.ic_transaksi_active);
        TransaksiAct.setBounds(0, 0, 60, 60);

        TransaksiNon = getResources().getDrawable(R.drawable.ic_transaksi_nonactive);
        TransaksiNon.setBounds(0, 0, 60, 60);

        HutangAct = getResources().getDrawable(R.drawable.ic_hutang_active);
        HutangAct.setBounds(0, 0, 60, 60);

        HutangNon = getResources().getDrawable(R.drawable.ic_hutang_nonactive);
        HutangNon.setBounds(0, 0, 60, 60);

        ProfileAct = getResources().getDrawable(R.drawable.ic_profile_active);
        ProfileAct.setBounds(0, 0, 60, 60);

        ProfileNon = getResources().getDrawable(R.drawable.ic_profile_nonactive);
        ProfileNon.setBounds(0, 0, 60, 60);

        if (position == 0) {
            beranda.setTextColor(fontSelect);
            beranda.setCompoundDrawables(null, homeAct, null, null);
            transaksi.setTextColor(fontUnselect);
            transaksi.setCompoundDrawables(null, TransaksiNon, null, null);
            utang.setTextColor(fontUnselect);
            utang.setCompoundDrawables(null, HutangNon, null, null);
            profil.setTextColor(fontUnselect);
            profil.setCompoundDrawables(null, ProfileNon, null, null);
        }
        if (position == 1) {
            beranda.setTextColor(fontUnselect);
            beranda.setCompoundDrawables(null, homeNon, null, null);
            transaksi.setTextColor(fontSelect);
            transaksi.setCompoundDrawables(null, TransaksiAct, null, null);
            utang.setTextColor(fontUnselect);
            utang.setCompoundDrawables(null, HutangNon, null, null);
            profil.setTextColor(fontUnselect);
            profil.setCompoundDrawables(null, ProfileNon, null, null);
        }
        if (position == 2) {
            beranda.setTextColor(fontUnselect);
            beranda.setCompoundDrawables(null, homeNon, null, null);
            transaksi.setTextColor(fontUnselect);
            transaksi.setCompoundDrawables(null, TransaksiNon, null, null);
            utang.setTextColor(fontSelect);
            utang.setCompoundDrawables(null, HutangAct, null, null);
            profil.setTextColor(fontUnselect);
            profil.setCompoundDrawables(null, ProfileNon, null, null);
        }
        if (position == 3) {
            beranda.setTextColor(fontUnselect);
            beranda.setCompoundDrawables(null, homeNon, null, null);
            transaksi.setTextColor(fontUnselect);
            transaksi.setCompoundDrawables(null, TransaksiNon, null, null);
            utang.setTextColor(fontUnselect);
            utang.setCompoundDrawables(null, HutangNon, null, null);
            profil.setTextColor(fontSelect);
            profil.setCompoundDrawables(null, ProfileAct, null, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}