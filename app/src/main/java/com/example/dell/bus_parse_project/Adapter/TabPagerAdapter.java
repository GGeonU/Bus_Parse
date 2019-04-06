package com.example.dell.bus_parse_project.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragment = new ArrayList<>();
    private final List<String> FragmentTitles = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm) { super(fm); }

    public void addFragment(Fragment fragment, String title){
        mFragment.add(fragment);
        FragmentTitles.add(title);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }
}
