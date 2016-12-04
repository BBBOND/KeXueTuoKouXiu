package com.kim.kexuetuokouxiu.app.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.kim.kexuetuokouxiu.R;
import com.kim.kexuetuokouxiu.app.adapter.ProgrammeAdapter;
import com.kim.kexuetuokouxiu.app.contract.MainContract;
import com.kim.kexuetuokouxiu.app.presenter.MainPresenterImpl;
import com.kim.kexuetuokouxiu.bean.Programme;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvShowList;
    private SwipeRefreshLayout srlRefresh;

    private MainContract.Presenter presenter;

    private ArrayList<Programme> programmeList;
    private ProgrammeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        rvShowList = (RecyclerView) findViewById(R.id.rvShowList);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        srlRefresh.setColorSchemeColors(Color.parseColor("#FCC21C"), Color.parseColor("#A4D226"), Color.parseColor("#F2765F"), Color.parseColor("#77D1D6"));
        srlRefresh.setOnRefreshListener(this);

        programmeList = new ArrayList<>();
        adapter = new ProgrammeAdapter(getBaseContext(), programmeList);
        adapter.setListener(new ProgrammeAdapter.OnClickListener() {
            @Override
            public void click(int position, ImageView ivProgrammeImg) {
                Log.d("==========>", programmeList.get(position).toString());
                if (programmeList.get(position).getEnclosureUrl() == null || programmeList.get(position).getEnclosureUrl().equals("") || programmeList.get(position).getEnclosureUrl().equals("null")) {
                    Uri uri = Uri.parse(programmeList.get(position).getLink());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } else {
                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                    intent.putExtra("programme", programmeList.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create((View) ivProgrammeImg, "programmeImg"));
                        startActivity(intent, option.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvShowList.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        } else {
            rvShowList.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        }
        rvShowList.setAdapter(adapter);

        presenter = new MainPresenterImpl(this);
        presenter.getScienceTalkShow();
    }

    @Override
    public void showRefreshProgress() {
        if (!srlRefresh.isRefreshing()) {
            srlRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshProgress() {
        if (srlRefresh.isRefreshing()) {
            srlRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (presenter == null)
            presenter = new MainPresenterImpl(this);
        presenter.getScienceTalkShow();
    }

    @Override
    public void receiveScienceTalkShow(ScienceTalkShow scienceTalkShow) {
        programmeList.clear();
        programmeList.addAll(scienceTalkShow.getProgrammes());
        adapter.notifyDataSetChanged();

        Log.d("---->", "===========================");
        Log.d("---->", scienceTalkShow.toString());
        Log.d("---->", "===========================");
    }


}
