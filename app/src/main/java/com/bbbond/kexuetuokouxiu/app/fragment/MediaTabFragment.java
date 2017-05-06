package com.bbbond.kexuetuokouxiu.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.ProgrammeAdapter;

/**
 * Created by bbbond on 2017/5/4.
 */

public class MediaTabFragment extends BaseTabFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, null);
        categories = getArguments().getStringArray(CATEGORIES);
        initView(view);
        initEvent();
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        GridLayoutManager manager = new GridLayoutManager(getActivity().getBaseContext(), 2);
        adapter = new ProgrammeAdapter(getActivity(), programmeList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (programmeList == null || programmeList.size() == 0)
            showNoData();
    }
}
