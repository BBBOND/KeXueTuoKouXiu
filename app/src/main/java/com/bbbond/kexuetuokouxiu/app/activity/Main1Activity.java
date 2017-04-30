package com.bbbond.kexuetuokouxiu.app.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.ProgrammeAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.HomeContract;
import com.bbbond.kexuetuokouxiu.app.presenter.HomePresenterImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Main1Activity extends AppCompatActivity implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvShowList;
    private SwipeRefreshLayout srlRefresh;

    private HomeContract.Presenter presenter;

    private ArrayList<Programme> programmeShowList;
    private ProgrammeAdapter adapter;
    private GridLayoutManager manager;

    public static final int DETAIL_REQUEST_CODE = 1;
    public static final String PROGRAMME = "programme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        presenter = new HomePresenterImpl(this);
        initView();
        presenter.getProgrammeListFromLocalFirst();
    }

    private void initView() {
        rvShowList = (RecyclerView) findViewById(R.id.rvShowList);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        srlRefresh.setColorSchemeColors(Color.parseColor("#FCC21C"), Color.parseColor("#A4D226"), Color.parseColor("#F2765F"), Color.parseColor("#77D1D6"));
        srlRefresh.setOnRefreshListener(this);

        programmeShowList = new ArrayList<>();
        adapter = new ProgrammeAdapter(getBaseContext(), programmeShowList);
        adapter.setListener(new ProgrammeAdapter.OnClickListener() {
            @Override
            public void click(int position, ImageView ivProgrammeImg) {
                LogUtil.d(Main1Activity.class, "adapter.click", programmeShowList.get(position).toString());
                if (programmeShowList.get(position).getDescription() == null || programmeShowList.get(position).getDescription().equals("") || programmeShowList.get(position).getDescription().equals("null")) {
                    Uri uri = Uri.parse(programmeShowList.get(position).getLink());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } else {
                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                    intent.putExtra(PROGRAMME, programmeShowList.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(Main1Activity.this, Pair.create((View) ivProgrammeImg, "programmeImg"));
                        startActivityForResult(intent, DETAIL_REQUEST_CODE, option.toBundle());
                    } else {
                        startActivityForResult(intent, DETAIL_REQUEST_CODE);
                    }
                }
            }
        });
        int spanCount;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        } else {
            spanCount = 3;
        }
        manager = new GridLayoutManager(getBaseContext(), spanCount);
        rvShowList.setLayoutManager(manager);

        rvShowList.setAdapter(adapter);
        rvShowList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= manager.getItemCount() - 5){
                        presenter.loadNextPage();
                    }
                }
            }
        });
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
            presenter = new HomePresenterImpl(this);
        presenter.getProgrammeListFromRemote();
        programmeShowList.clear();
    }

    @Override
    public void receiveProgrammeList(List<Programme> programmes) {
        if (programmeShowList.size() == programmes.size()) {
            Snackbar.make(rvShowList, "我是有底线的!", Snackbar.LENGTH_SHORT).show();
        } else {
            programmeShowList.clear();
            programmeShowList.addAll(programmes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {

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
                    int index = programmeShowList.indexOf(programme);
                    if (index >= 0)
                        rvShowList.scrollToPosition(index);
                }
        }
    }
}
