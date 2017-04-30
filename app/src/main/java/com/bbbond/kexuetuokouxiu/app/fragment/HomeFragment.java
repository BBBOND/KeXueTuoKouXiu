package com.bbbond.kexuetuokouxiu.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.TabViewPagerAdapter;
import com.bbbond.kexuetuokouxiu.app.presenter.HomeTabPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/4/26.
 */

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private AppBarLayout appBar;

    private TabViewPagerAdapter adapter;

    private List<Fragment> fragmentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initPages();
        initView(view);
        initEvent();
        return view;
    }

    private void initPages() {
        if (fragmentList == null)
            fragmentList = new ArrayList<>();
        String[] tabs = getResources().getStringArray(R.array.tabs);
        int length = tabs.length;
        if (fragmentList.size() == length) {
            return;
        }
        HomeTabFragment fragment;
        for (int i = 0; i < length; i++) {
            fragment = new HomeTabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(HomeTabFragment.POS, i);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
            new HomeTabPresenter(fragment);
        }
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        appBar = (AppBarLayout) view.findViewById(R.id.appBar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        adapter = new TabViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        String[] tabs = getResources().getStringArray(R.array.tabs);
        int length = fragmentList.size();
        for (int i = 0; i < length; i++) {
            if (i < tabs.length)
                tabLayout.getTabAt(i).setText(tabs[i]);
        }
    }

    private void initEvent() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
