package com.bbbond.kexuetuokouxiu.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.fragment.DownloadFragment;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.fragment.MeFragment;
import com.bbbond.kexuetuokouxiu.helper.NetHelper;

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

        initView();
        initFragments();
        initEvent();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        downloadFragment = new DownloadFragment();
//        meFragment = new MeFragment();
        currentFragment = homeFragment;
        if (NetHelper.getAPNType(getApplicationContext()) == NetHelper.NO_NETWORK) {
            currentFragment = downloadFragment;
            Toast.makeText(getApplicationContext(), "当前无网络!", Toast.LENGTH_SHORT).show();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, currentFragment);
        transaction.commit();
    }

    private void initView() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setSelectedItemId(NetHelper.getAPNType(getApplicationContext()) == NetHelper.NO_NETWORK ? R.id.nav_download : R.id.nav_home);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void initEvent() {
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
        }
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
//            case R.id.nav_me:
//                if (currentFragment == meFragment) return true;
//                if (meFragment == null)
//                    meFragment = new MeFragment();
//                targetFragment = meFragment;
//                break;
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
//        transaction.replace(R.id.content, targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
