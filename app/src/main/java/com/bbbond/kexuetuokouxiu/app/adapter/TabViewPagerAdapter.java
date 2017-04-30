package com.bbbond.kexuetuokouxiu.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bbbond on 2017/4/29.
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public TabViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if (fragmentList != null && fragmentList.size() > 0)
            return fragmentList.size();
        else
            return 0;
    }
}
