package com.kim.kexuetuokouxiu.app.model;

import com.kim.kexuetuokouxiu.app.contract.MainContract;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;
import com.kim.kexuetuokouxiu.network.RemoteClient;
import com.kim.kexuetuokouxiu.utils.ParseUtil;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Weya on 2016/12/03
 */

public class MainModelImpl implements MainContract.Model {

    public static final int FROM_REMOTE = 1;
    public static final int FROM_LOCAL = 2;

    @Override
    public void getScienceTalkShow(final Callback callback, int from) {
        switch (from) {
            case FROM_REMOTE:
                getScienceTalkShowFromRemote(callback);
                break;
            case FROM_LOCAL:
                break;
        }
    }

    private void getScienceTalkShowFromRemote(final Callback callback) {
        RemoteClient.getScienceTalkShow(new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                callback.onStart();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String xml = response.get();
                callback.onSucceed(ParseUtil.parseXml2ScienceTalkShow(xml));
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                callback.onFailed();
            }

            @Override
            public void onFinish(int what) {
                callback.onFinish();
            }
        });
    }

    public interface Callback {

        void onStart();

        void onSucceed(ScienceTalkShow scienceTalkShow);

        void onFailed();

        void onFinish();
    }
}