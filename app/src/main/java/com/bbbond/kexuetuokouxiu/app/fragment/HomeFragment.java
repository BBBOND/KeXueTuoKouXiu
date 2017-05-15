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

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.TabViewPagerAdapter;
import com.bbbond.kexuetuokouxiu.app.presenter.TabPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/4/26.
 */

public class HomeFragment extends Fragment {

    public static final String[][] categories = new String[][]{
            {"科学脱口秀", "未分类"},
            {"听众互动"},
            {"科脱在别处"},
            {"户外版"},
            {"科学单口秀"},
            {"午间版", "OX果壳问答", "节目花絮", "实录", "科学勾搭", "打赏互动"},
            {"广告", "看板", "桌面下载", "科学逛吃会"},
    };

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

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
        BaseTabFragment fragment;
        for (int i = 0; i < length - 1; i++) {
            fragment = new MediaTabFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray(BaseTabFragment.CATEGORIES, categories[i]);
            fragment.setArguments(bundle);
            new TabPresenter(fragment);
            fragmentList.add(fragment);
        }
        fragment = new ArticleTabFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(BaseTabFragment.CATEGORIES, categories[length - 1]);
        fragment.setArguments(bundle);
        new TabPresenter(fragment);
        fragmentList.add(fragment);
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setPadding(0, tabLayout.getHeight(), 0, 0);

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
