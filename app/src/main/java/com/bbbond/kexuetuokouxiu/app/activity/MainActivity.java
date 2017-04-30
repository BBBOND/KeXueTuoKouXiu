package com.bbbond.kexuetuokouxiu.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.fragment.DownloadFragment;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.fragment.MeFragment;
import com.bbbond.kexuetuokouxiu.utils.DensityUtil;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private DownloadFragment downloadFragment;
    private MeFragment meFragment;

    private Fragment currentFragment;
    private Fragment targetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initView();
        initEvent();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        downloadFragment = new DownloadFragment();
        meFragment = new MeFragment();
        currentFragment = homeFragment;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, currentFragment);
        transaction.commit();
    }

    private void initView() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        int bottom = checkDeviceHasNavigationBar(this) ? 48 : 0;
        navigationView.setPadding(0, 0, 0, DensityUtil.dip2px(this, bottom));
    }

    private void initEvent() {
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (currentFragment == homeFragment) return true;
                if (homeFragment == null)
                    homeFragment = new HomeFragment();
                targetFragment = homeFragment;
                break;
            case R.id.nav_download:
                if (currentFragment == downloadFragment) return true;
                if (downloadFragment == null)
                    downloadFragment = new DownloadFragment();
                targetFragment = downloadFragment;
                break;
            case R.id.nav_me:
                if (currentFragment == meFragment) return true;
                if (meFragment == null)
                    meFragment = new MeFragment();
                targetFragment = meFragment;
                break;
            default:
                if (currentFragment == homeFragment) return true;
                if (homeFragment == null)
                    homeFragment = new HomeFragment();
                targetFragment = homeFragment;
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (targetFragment.isAdded()) {
            transaction.hide(currentFragment).show(targetFragment);
        } else {
            transaction.hide(currentFragment).add(R.id.content, targetFragment);
        }
        transaction.commit();
        currentFragment = targetFragment;
        return true;
    }
}
