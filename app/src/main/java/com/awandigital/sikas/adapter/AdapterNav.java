package com.awandigital.sikas.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.awandigital.sikas.fragment.TransaksiFragment;
import com.awandigital.sikas.fragment.beranda.DashboardFragment;
import com.awandigital.sikas.fragment.profil.ProfileFragment;
import com.awandigital.sikas.fragment.utang.UtangPiutangFragment;


public class AdapterNav extends FragmentStateAdapter {

    //harusnya fragment manager
    public AdapterNav(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);

    }

//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        if (position == 0) {
//            return new DashboardFragment();
//        } else if (position == 1) {
//            return new TransaksiFragment();
//        } else if (position == 2) {
//            return new UtangPiutangFragment();
//        } else if (position == 3) {
//            return new ProfileFragment();
//        } else {
//            return new DashboardFragment();
//        }
//    }


//    @Override
//    public int getCount() {
//        return 4;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new DashboardFragment();
        } else if (position == 1) {
            return new TransaksiFragment();
        } else if (position == 2) {
            return new UtangPiutangFragment();
        } else if (position == 3) {
            return new ProfileFragment();
        } else {
            return new DashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
