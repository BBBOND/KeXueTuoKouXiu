package com.bbbond.kexuetuokouxiu.app.fragment;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.contract.TabContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.helper.NetHelper;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/4/27.
 */

public class BaseTabFragment extends Fragment implements TabContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String CATEGORIES = "CATEGORIES";

    public String[] categories;

    public List<Programme> programmeList = new ArrayList<>();
    public TabContract.Presenter presenter;
    public RecyclerView.Adapter adapter;

    public RecyclerView recyclerView;
    public RelativeLayout noDataLayout;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onRefresh() {
        if (presenter != null) {
            presenter.getProgrammeListRemote(categories);
        }
        LogUtil.d(BaseTabFragment.class, "onRefresh", "开始刷新");
        LogUtil.d(BaseTabFragment.class, "onRefresh", categories);
    }

    @Override
    public void setPresenter(TabContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void refreshing(boolean isRefreshing) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(isRefreshing);
        LogUtil.d(BaseTabFragment.class, "refreshing", isRefreshing ? "正在刷新" : "刷新结束");
    }

    @Override
    public void receiveProgrammeList(List<Programme> programmes) {
        if (programmes != null) {
//            LogUtil.d(BaseTabFragment.class, "receiveProgrammeList", programmes.toString());
            programmeList.clear();
            programmeList.addAll(programmes);
        }
        if (adapter != null)
            adapter.notifyDataSetChanged();
        showNoData();
        LogUtil.d(BaseTabFragment.class, "receiveProgrammeList", programmes.size());
    }

    @Override
    public void showToast(String msg) {
        if (getContext() != null && msg != null && !msg.equals(""))
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        showNoData();
    }

    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noDataLayout = (RelativeLayout) view.findViewById(R.id.noDataLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.mainColor_yellow),
                getResources().getColor(R.color.mainColor_green),
                getResources().getColor(R.color.mainColor_red),
                getResources().getColor(R.color.mainColor_blue));
    }

    public void initData() {
        if (presenter != null && programmeList.size() == 0) {
            presenter.getProgrammeList(categories, NetHelper.getAPNType(getContext()) != NetHelper.NO_NETWORK);
            refreshing(true);
        }
        LogUtil.d(BaseTabFragment.class, "initData", categories);
    }

    public void showNoData() {
        noDataLayout.setVisibility(programmeList != null && programmeList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                }
            });
        }
    }
}
