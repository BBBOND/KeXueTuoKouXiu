package com.bbbond.kexuetuokouxiu.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.bbbond.kexuetuokouxiu.app.adapter.DownloadTabFirstAdapter;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public class DownloadDetailFirstTabFragment extends Fragment {

    private int pos;

    private RecyclerView rvContent;
    private DownloadTabFirstAdapter adapter;
    private List<ProgrammeCache> programmeCacheList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_detail, null);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        pos = getArguments().getInt(DownloadDetailActivity.POS);
        if (programmeCacheList == null)
            programmeCacheList = new ArrayList<>();
        ProgrammeCache cache;
        for (int i = 0; i < 10; i++) {
            cache = new ProgrammeCache();
            cache.setId("" + i);
            cache.setTitle("item " + i);
            cache.setCreator("123");
            programmeCacheList.add(cache);
        }
    }

    private void initView(View view) {
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        adapter = new DownloadTabFirstAdapter(getContext(), programmeCacheList);
        adapter.setOnClickListener(new DownloadTabFirstAdapter.OnClickListener() {
            @Override
            public void click(int position, String id) {
                Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
            }
        });
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvContent.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvContent.setAdapter(adapter);
    }
}
