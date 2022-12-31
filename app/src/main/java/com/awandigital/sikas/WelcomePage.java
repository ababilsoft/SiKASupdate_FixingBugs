package com.awandigital.sikas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awandigital.sikas.db.SessionManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener {
    Button btLnjut;
    TextView btSkip, tvMasuk;
    ViewPager viewPager;
    Adapter adapter;
    DotsIndicator dotsIndicator;
    int page = 0;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        setupViewPager();
        sessionManager = new SessionManager(this);

        btLnjut = findViewById(R.id.btLanjut);
        btSkip = findViewById(R.id.btSkip);
        tvMasuk = findViewById(R.id.tvMasuk);

        tvMasuk.setVisibility(View.GONE);

        btLnjut.setOnClickListener(this);
        btSkip.setOnClickListener(this);
        tvMasuk.setOnClickListener(this);
        pageChange();
    }

    private void pageChange() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                switch (position) {
                    case 0:
                    case 1:
                        btLnjut.setText("Lanjut");
                        tvMasuk.setVisibility(View.GONE);
                        btSkip.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        btLnjut.setText("Daftar Sekarang");
                        tvMasuk.setVisibility(View.VISIBLE);
                        btSkip.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager() {
        adapter = new Adapter(this);
        viewPager = findViewById(R.id.viewpager);
        dotsIndicator = findViewById(R.id.dots);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);


    }

    private class Adapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;

        public Adapter(Context context) {
            this.context = context;
        }

        int[] list_img = {
                R.drawable.ic_img1svg,
                R.drawable.ic_img2_svg,
                R.drawable.ic_img3_svg,
        };

        int[] img_gradient = {
                R.drawable.img_gradient_back,
                R.drawable.img_gradient_back,
                R.drawable.img_gradient_back,
        };


        int[] desk = {
                R.string.teks_welcome1,
                R.string.teks_welcome2,
                R.string.teks_welcome3,
        };

        int[] bg = {
                getResources().getColor(R.color.primary_500),
                getResources().getColor(R.color.primary_500),
                getResources().getColor(R.color.primary_500)
        };

        int[] title = {
                R.string.title1,
                R.string.title2,
                R.string.title3,
        };

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.welcome_layout, container, false);
            RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);
            ImageView ivew1 = view.findViewById(R.id.image);
            ImageView ivew2 = view.findViewById(R.id.image2);
            TextView judul = view.findViewById(R.id.title);
            TextView deskripsi = view.findViewById(R.id.desk);

            relativeLayout.setBackgroundColor(bg[position]);
            ivew1.setImageResource(img_gradient[position]);
            ivew2.setImageResource(list_img[position]);
            judul.setText(title[position]);
            deskripsi.setText(desk[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btLanjut:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                if (btLnjut.getText().equals("Daftar Sekarang")) {
                    Intent intent = new Intent(WelcomePage.this, Registrasi.class);
                    startActivity(intent);
                }
                sessionManager.setFirstlook();
                break;
            case R.id.btSkip:
                viewPager.setCurrentItem(3);
                btSkip.setVisibility(View.GONE);
                tvMasuk.setVisibility(View.VISIBLE);
                break;
            case R.id.tvMasuk:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                sessionManager.setFirstlook();
                break;
        }
    }
}