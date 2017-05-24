package com.bbbond.kexuetuokouxiu.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.TabViewPagerAdapter;
import com.bbbond.kexuetuokouxiu.app.fragment.DownloadDetailFirstTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public class DownloadDetailActivity extends BaseActivity {

    public static final String TITLE = "TITLE";
    public static final String POS = "POS";

    private TabLayout tabLayout;
    private ViewPager vpContent;

    private String title;
    private int pos;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_detail);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), "打开异常", Toast.LENGTH_SHORT).show();
            finishActivity();
            return;
        } else {
            title = intent.getStringExtra(TITLE);
            pos = intent.getIntExtra(POS, -1);
            if (pos == -1) {
                Toast.makeText(getApplicationContext(), "打开异常", Toast.LENGTH_SHORT).show();
                finishActivity();
                return;
            }
        }
        DownloadDetailFirstTabFragment firstTabFragment = new DownloadDetailFirstTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POS, pos);
        firstTabFragment.setArguments(bundle);

        DownloadDetailFirstTabFragment firstTabFragment1 = new DownloadDetailFirstTabFragment();
        Bundle bundle1 = new Bundle();
        bundle.putInt(POS, pos);
        firstTabFragment1.setArguments(bundle1);
        fragmentList.add(firstTabFragment);
        fragmentList.add(firstTabFragment1);
    }

    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        vpContent = (ViewPager) findViewById(R.id.vp_content);

        vpContent.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(), fragmentList));
        tabLayout.setupWithViewPager(vpContent);

        tabLayout.getTabAt(0).setText("已缓存");
        tabLayout.getTabAt(1).setText("正在缓存");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return true;
    }
}
