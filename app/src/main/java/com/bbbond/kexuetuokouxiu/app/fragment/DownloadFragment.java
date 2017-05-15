package com.bbbond.kexuetuokouxiu.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.activity.DownloadDetailActivity;
import com.bbbond.kexuetuokouxiu.app.contract.DownloadContract;
import com.bbbond.kexuetuokouxiu.app.presenter.DownloadPresenter;
import com.bbbond.kexuetuokouxiu.utils.DensityUtil;

import org.w3c.dom.Text;

/**
 * Created by bbbond on 2017/4/26.
 */

public class DownloadFragment extends Fragment implements DownloadContract.View {

    private ScrollView content;
    private LinearLayout llContent;
    private DownloadContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        presenter = new DownloadPresenter(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("已缓存");
        content = (ScrollView) view.findViewById(R.id.rv_content);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        presenter.initContent(getResources().getStringArray(R.array.tabs));
    }

    @Override
    public void addView(String title, long num, boolean complete, View.OnClickListener listener, String... a) {
        View view = View.inflate(getContext(), R.layout.item_programme_cache, null);
        if (view.findViewById(R.id.title) != null) {
            ((TextView) view.findViewById(R.id.title)).setText(title);
        }
        if (view.findViewById(R.id.title_num) != null) {
            ((TextView) view.findViewById(R.id.title_num)).setText(getResources().getString(R.string.download_num, String.valueOf(num)));
        }
        if (view.findViewById(R.id.subTitle) != null) {
            ((TextView) view.findViewById(R.id.subTitle))
                    .setText(complete ?
                            (num > 0 ? getResources().getString(R.string.download_finish) : getResources().getString(R.string.download_no_data)) :
                            getResources().getString(R.string.download_process, a[0], a[1]));
        }
        llContent.addView(view);
        View line = new View(getContext());
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundColor(getResources().getColor(R.color.border));
        llContent.addView(line);
        if (listener != null) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    public void toDetail(String title, int pos) {
        Intent intent = new Intent(getContext(), DownloadDetailActivity.class);
        intent.putExtra(DownloadDetailActivity.POS, pos);
        intent.putExtra(DownloadDetailActivity.TITLE, title);
        startActivity(intent);
    }
}
