package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.DownloadDetailActivity;
import com.bbbond.kexuetuokouxiu.app.activity.PlayingActivity;
import com.bbbond.kexuetuokouxiu.app.adapter.DownloadTabFirstAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.DownloadDetailFirstContract;
import com.bbbond.kexuetuokouxiu.app.presenter.DownloadDetailFirstPresenter;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public class DownloadDetailFirstTabFragment extends Fragment implements DownloadDetailFirstContract.View {

    private int pos;

    private RecyclerView rvContent;
    private TextView tvNoData;
    private DownloadTabFirstAdapter adapter;
    private List<ProgrammeCache> programmeCacheList;

    private DownloadDetailFirstContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_detail, null);
        presenter = new DownloadDetailFirstPresenter(this);
        initData();
        initView(view);
        presenter.showCachedProgramme(pos);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showNoData();
    }

    private void initData() {
        pos = getArguments().getInt(DownloadDetailActivity.POS);
        if (programmeCacheList == null)
            programmeCacheList = new ArrayList<>();
    }

    private void initView(View view) {
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
        adapter = new DownloadTabFirstAdapter(getContext(), programmeCacheList);
        adapter.setOnClickListener(new DownloadTabFirstAdapter.OnClickListener() {
            @Override
            public void click(int position, String id) {
                presenter.play(id);
            }
        });
        adapter.setOnLongClickListener(new DownloadTabFirstAdapter.OnLongClickListener() {
            @Override
            public boolean longClick(final int position, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setTitle("提示")
                        .setMessage("是否删除本地已下载文件？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.deleteDownloadCacheItem(getContext(), programmeCacheList.get(position).getUrl(), true);
                            }
                        })
                        .setNegativeButton("不删除", null)
                        .create()
                        .show();
                return true;
            }
        });
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvContent.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvContent.setAdapter(adapter);
    }

    @Override
    public void receiveDownloadDetailList(List<ProgrammeCache> programmeCaches) {
        if (programmeCaches != null) {
            programmeCacheList.clear();
            programmeCacheList.addAll(programmeCaches);
            adapter.notifyDataSetChanged();
        }
        showNoData();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoData() {
        if (tvNoData != null)
            tvNoData.setVisibility(programmeCacheList != null && programmeCacheList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void goToPlayingActivity(Programme programme) {
        Intent intent = new Intent(getContext(), PlayingActivity.class);
        intent.putExtra(PlayingActivity.PROGRAMME, programme);
        startActivity(intent);
    }

    @Override
    public void refresh() {
        Snackbar.make(rvContent, "删除成功", Snackbar.LENGTH_SHORT).show();
        presenter.showCachedProgramme(pos);
    }
}
