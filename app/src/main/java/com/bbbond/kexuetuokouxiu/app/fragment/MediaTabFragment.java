package com.bbbond.kexuetuokouxiu.app.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.PlayingActivity;
import com.bbbond.kexuetuokouxiu.app.adapter.ProgrammeAdapter;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

/**
 * Created by bbbond on 2017/5/4.
 */

public class MediaTabFragment extends BaseTabFragment {

    public static final int DETAIL_REQUEST_CODE = 1;

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

    @Override
    public void initEvent() {
        super.initEvent();
        ((ProgrammeAdapter) adapter).setListener(new ProgrammeAdapter.OnClickListener() {
            @Override
            public void click(int position, ImageView ivProgrammeImg) {
                LogUtil.d(MediaTabFragment.class, "adapter.click", programmeList.get(position).toString());
                if (programmeList.get(position).getMediaUrl() == null || programmeList.get(position).getMediaUrl().equals("") || programmeList.get(position).getMediaUrl().equals("null")) {
                    Uri uri = Uri.parse(programmeList.get(position).getLink());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } else {
                    Intent intent = new Intent(getActivity().getBaseContext(), PlayingActivity.class);
                    intent.putExtra(PlayingActivity.PROGRAMME, programmeList.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(getActivity(), Pair.create((View) ivProgrammeImg, "programmeImg"));
                        startActivityForResult(intent, DETAIL_REQUEST_CODE, option.toBundle());
                    } else {
                        startActivityForResult(intent, DETAIL_REQUEST_CODE);
                    }
                }
            }
        });
    }
}
