package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.DownloadDetailActivity;
import com.bbbond.kexuetuokouxiu.app.adapter.DownloadAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.DownloadContract;
import com.bbbond.kexuetuokouxiu.app.presenter.DownloadPresenter;
import com.bbbond.kexuetuokouxiu.bean.DownloadItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 下载简易列表界面
 * Created by bbbond on 2017/4/26.
 */

public class DownloadFragment extends Fragment implements DownloadContract.View {

    private RecyclerView content;
    private DownloadContract.Presenter presenter;

    private List<DownloadItem> downloadItemList = new ArrayList<>();
    private DownloadAdapter adapter;

    private List<String> itemTitles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        presenter = new DownloadPresenter(this);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        itemTitles = Arrays.asList(getResources().getStringArray(R.array.tabs));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.showContent(itemTitles);
    }

    private void initView(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("已缓存");

        adapter = new DownloadAdapter(getContext(), downloadItemList);
        adapter.setOnClickListener(new DownloadAdapter.OnClickListener() {
            @Override
            public void click(int position) {
                if (itemTitles != null)
                    toDetail(itemTitles.get(position), position);
            }
        });
        content = (RecyclerView) view.findViewById(R.id.rv_content);
        content.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        content.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        content.setAdapter(adapter);
    }

    @Override
    public void receiveDownloadItemList(List<DownloadItem> downloadItems) {
        if (downloadItems != null) {
            downloadItemList.clear();
            downloadItemList.addAll(downloadItems);
            adapter.notifyDataSetChanged();
        }
    }

    private void toDetail(String title, int pos) {
        Intent intent = new Intent(getContext(), DownloadDetailActivity.class);
        intent.putExtra(DownloadDetailActivity.POS, pos);
        intent.putExtra(DownloadDetailActivity.TITLE, title);
        startActivity(intent);
    }
}
