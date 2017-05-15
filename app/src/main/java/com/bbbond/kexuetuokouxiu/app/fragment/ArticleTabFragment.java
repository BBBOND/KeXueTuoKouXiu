package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.ArticleDetailActivity;
import com.bbbond.kexuetuokouxiu.app.adapter.ArticleAdapter;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

/**
 * Created by bbbond on 2017/5/6.
 */

public class ArticleTabFragment extends BaseTabFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, null);
        categories = getArguments().getStringArray(CATEGORIES);
        LogUtil.d(ArticleTabFragment.class, "onCreateView", categories);
        initView(view);
        initEvent();
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        adapter = new ArticleAdapter(getActivity(), programmeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        if (programmeList == null || programmeList.size() == 0)
            showNoData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ((ArticleAdapter) adapter).setListener(new ArticleAdapter.OnClickListener() {
            @Override
            public void click(int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailActivity.ARTICLE, programmeList.get(position));
                startActivity(intent);
            }
        });
    }
}
