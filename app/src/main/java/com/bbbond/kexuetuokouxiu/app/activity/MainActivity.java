package com.bbbond.kexuetuokouxiu.app.activity;

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
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.ProgrammeAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.MainContract;
import com.bbbond.kexuetuokouxiu.app.presenter.MainPresenterImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvShowList;
    private SwipeRefreshLayout srlRefresh;

    private MainContract.Presenter presenter;

    private ArrayList<Programme> programmeList;
    private ProgrammeAdapter adapter;

    public static final int DETAIL_REQUEST_CODE = 1;
    public static final String PROGRAMME = "programme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenterImpl(this);
        initView();
        presenter.getProgrammeListFromLocalFirst();
    }

    private void initView() {
        rvShowList = (RecyclerView) findViewById(R.id.rvShowList);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        srlRefresh.setColorSchemeColors(Color.parseColor("#FCC21C"), Color.parseColor("#A4D226"), Color.parseColor("#F2765F"), Color.parseColor("#77D1D6"));
        srlRefresh.setOnRefreshListener(this);

        programmeList = new ArrayList<>();
        adapter = new ProgrammeAdapter(getBaseContext(), programmeList);
        adapter.setListener(new ProgrammeAdapter.OnClickListener() {
            @Override
            public void click(int position, ImageView ivProgrammeImg) {
                LogUtil.d(MainActivity.class, "adapter.click", programmeList.get(position).toString());
                if (programmeList.get(position).getDescription() == null || programmeList.get(position).getDescription().equals("") || programmeList.get(position).getDescription().equals("null")) {
                    Uri uri = Uri.parse(programmeList.get(position).getLink());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } else {
                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                    intent.putExtra(PROGRAMME, programmeList.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create((View) ivProgrammeImg, "programmeImg"));
                        startActivityForResult(intent, DETAIL_REQUEST_CODE, option.toBundle());
                    } else {
                        startActivityForResult(intent, DETAIL_REQUEST_CODE);
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
        presenter.getProgrammeListFromRemote();
    }

    @Override
    public void receiveProgrammeList(List<Programme> programmes) {
        programmeList.clear();
        programmeList.addAll(programmes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DETAIL_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Programme programme = (Programme) data.getSerializableExtra("programme");
                    int index = programmeList.indexOf(programme);
                    if (index >= 0)
                        rvShowList.scrollToPosition(index);
                }
        }
    }
}
