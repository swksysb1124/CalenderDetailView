package com.example.weekuptime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "LOG_" + DetailPagerAdapter.class.getSimpleName();
    private List<Long> times;

    public DetailPagerAdapter(@NonNull FragmentManager fm, List<Long> times) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.times = times;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DetailPageFragment.newInstance(times.get(position));
    }

    @Override
    public int getCount() {
        return times.size();
    }
}
