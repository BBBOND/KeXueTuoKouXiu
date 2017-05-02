package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.ArticleAdapter;
import com.bbbond.kexuetuokouxiu.app.adapter.ProgrammeAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.HomeTabContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/4/27.
 */

public class HomeTabFragment extends Fragment implements HomeTabContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String TYPE_MEDIA = "media";
    public static final String TYPE_PAGE = "page";
    public static final String POS = "POS";

    private final String[][] categories = new String[][]{
            {"科学脱口秀", "未分类"},
            {"听众互动"},
            {"科脱在别处"},
            {"户外版"},
            {"科学单口秀"},
            {"午间版", "OX果壳问答", "节目花絮", "实录", "科学勾搭", "打赏互动"},
            {"广告", "看板", "桌面下载", "科学逛吃会"},
    };

    private List<Programme> programmeList = new ArrayList<>();
    private String type;
    private int tabPosition;
    private HomeTabContract.Presenter presenter;
    private RecyclerView.Adapter adapter;

    private RecyclerView recyclerView;
    private RelativeLayout noDataLayout;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, null);
        initView(view);
        initEvent();
        initData();
        showContent();
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noDataLayout = (RelativeLayout) view.findViewById(R.id.noDataLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.mainColor_yellow),
                getResources().getColor(R.color.mainColor_green),
                getResources().getColor(R.color.mainColor_red),
                getResources().getColor(R.color.mainColor_blue));
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        }
    }

    private void showContent() {
        if (programmeList != null && programmeList.size() > 0) {
            showNoData();
            GridLayoutManager manager = null;
            int spanCount;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                spanCount = 2;
            } else {
                spanCount = 3;
            }
            switch (type) {
                case TYPE_MEDIA:
                    // TODO: 2017/4/28 音频风格
                    manager = new GridLayoutManager(getActivity().getBaseContext(), spanCount);
                    adapter = new ProgrammeAdapter(getActivity(), programmeList);
                    break;
                case TYPE_PAGE:
                    // TODO: 2017/4/28 文章风格
                    manager = new GridLayoutManager(getActivity().getBaseContext(), 1);
                    adapter = new ArticleAdapter(getActivity(), programmeList);
                    break;
                default:
                    // TODO: 2017/4/28 默认音频风格
                    manager = new GridLayoutManager(getActivity().getBaseContext(), spanCount);
                    adapter = new ProgrammeAdapter(getActivity(), programmeList);
                    break;
            }
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            showNoData();
        }
    }

    private void showNoData() {
        noDataLayout.setVisibility(programmeList != null && programmeList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    private void initData() {
        tabPosition = getArguments().getInt(POS, 0);
        type = tabPosition == categories.length ? TYPE_PAGE : TYPE_MEDIA;
        if (presenter != null && programmeList.size() == 0)
            presenter.getProgrammeList(categories[tabPosition], true);
        LogUtil.d(HomeTabFragment.class, "initData", categories[tabPosition][0]);
    }

    @Override
    public void onRefresh() {
        if (presenter != null) {
            presenter.getProgrammeListRemote(categories[tabPosition]);
        }
        LogUtil.d(HomeTabFragment.class, "onRefresh", "开始刷新");
        LogUtil.d(HomeTabFragment.class, "onRefresh", categories[tabPosition][0]);
    }

    @Override
    public void setPresenter(HomeTabContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void refreshing(boolean isRefreshing) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(isRefreshing);
        LogUtil.d(HomeTabFragment.class, "refreshing", isRefreshing ? "正在刷新" : "刷新结束");
    }

    @Override
    public void receiveProgrammeList(List<Programme> programmes) {
        if (programmes != null) {
//            LogUtil.d(HomeTabFragment.class, "receiveProgrammeList", programmes.toString());
            programmeList.clear();
            programmeList.addAll(programmes);
        }
        showNoData();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        LogUtil.d(HomeTabFragment.class, "receiveProgrammeList", programmes);
    }
}
