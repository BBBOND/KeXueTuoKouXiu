package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.DownloadDetailActivity;
import com.bbbond.kexuetuokouxiu.app.adapter.DownloadTabSecondAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.DownloadDetailSecondContract;
import com.bbbond.kexuetuokouxiu.app.presenter.DownloadDetailSecondPresenter;
import com.bbbond.kexuetuokouxiu.bean.DownloadingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/30.
 */

public class DownloadDetailSecondTabFragment extends Fragment implements DownloadDetailSecondContract.View, View.OnClickListener {

    private int pos;

    private LinearLayout llTop;
    private TextView tvClearAll;
    private RecyclerView rvContent;
    private TextView tvNoData;

    private DownloadDetailSecondContract.Presenter presenter;
    private List<DownloadingItem> downloadingItemList;
    private DownloadTabSecondAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_detail, null);
        presenter = new DownloadDetailSecondPresenter(this);
        initData();
        initView(view);
        initEvent();

        presenter.showDownloadingProgramme(getContext(), pos);
        return view;
    }

    private void initData() {
        pos = getArguments().getInt(DownloadDetailActivity.POS);
        if (downloadingItemList == null)
            downloadingItemList = new ArrayList<>();
    }

    private void initView(View view) {
        llTop = (LinearLayout) view.findViewById(R.id.llTop);
        tvClearAll = (TextView) view.findViewById(R.id.clear_all);
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);

        llTop.setVisibility(View.VISIBLE);
        adapter = new DownloadTabSecondAdapter(getContext(), downloadingItemList);
        adapter.setOnClickListener(new DownloadTabSecondAdapter.OnClickListener() {
            @Override
            public void click(int position, DownloadingItem downloadingItem, boolean isDownloading) {
                if (isDownloading) {
                    presenter.pauseDownloadingItem(getContext(), downloadingItem.getUrl());
                } else {
                    Toast.makeText(getContext(), "开始下载", Toast.LENGTH_SHORT).show();
                    presenter.startDownloadingItem(getContext(), downloadingItem.getUrl());
                }
            }
        });
        adapter.setOnCloseClickListener(new DownloadTabSecondAdapter.OnCloseClickListener() {
            @Override
            public void click(int position, final DownloadingItem downloadingItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setTitle("提示")
                        .setMessage("是否删除本地已下载文件？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.deleteDownloadingItem(getContext(), downloadingItem.getUrl(), true);
                            }
                        })
                        .setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.deleteDownloadingItem(getContext(), downloadingItem.getUrl(), false);
                            }
                        })
                        .setCancelable(true)
                        .create()
                        .show();
            }
        });
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvContent.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvContent.setAdapter(adapter);
        tvNoData.setText("无任何缓存任务");
    }

    private void initEvent() {
        tvClearAll.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showNoData();
    }

    @Override
    public void receiveDownloadingItemList(List<DownloadingItem> downloadingItems) {
        if (downloadingItems != null) {
            downloadingItemList.clear();
            downloadingItemList.addAll(downloadingItems);
            adapter.notifyDataSetChanged();
        }
        showNoData();
    }

    @Override
    public void showNoData() {
        boolean visible = downloadingItemList != null && downloadingItemList.size() > 0;
        if (llTop != null)
            llTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (tvNoData != null)
            tvNoData.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setTitle("提示")
                        .setMessage("是否删除本地已下载文件？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.deleteAll(downloadingItemList, getContext(), true);
                            }
                        })
                        .setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.deleteAll(downloadingItemList, getContext(), false);
                            }
                        })
                        .setCancelable(true)
                        .create()
                        .show();
                break;
        }
    }

    @Override
    public void refresh() {
        presenter.showDownloadingProgramme(getContext(), pos);
    }
}
