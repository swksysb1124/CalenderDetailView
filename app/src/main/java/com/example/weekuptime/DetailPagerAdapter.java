package com.example.weekuptime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;


public class DetailPagerAdapter extends FragmentStateAdapter {

    private static final String TAG = "LOG_" + DetailPagerAdapter.class.getSimpleName();
    private List<Long> times;

    public DetailPagerAdapter(@NonNull FragmentActivity fm, List<Long> times) {
        super(fm);
        this.times = times;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DetailPageFragment.newInstance(times.get(position));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
