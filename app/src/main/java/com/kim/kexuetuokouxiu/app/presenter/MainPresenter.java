package com.kim.kexuetuokouxiu.app.presenter;

import com.kim.kexuetuokouxiu.app.activity.MainActivity;
import com.kim.kexuetuokouxiu.network.RemoteClient;
import com.kim.kexuetuokouxiu.utils.ParseUtil;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Weya on 2016/11/10.
 */

public class MainPresenter {
    private MainActivity activity;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
    }

    public void getScienceTalkShow() {
        RemoteClient.getScienceTalkShow(new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                activity.showRefreshProgress();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String xml = response.get();
                activity.receiveScienceTalkShow(ParseUtil.parseXml2Obj(xml));
                activity.hideRefreshProgress();
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                activity.hideRefreshProgress();
            }

            @Override
            public void onFinish(int what) {
                activity.hideRefreshProgress();
            }
        });
    }
}
