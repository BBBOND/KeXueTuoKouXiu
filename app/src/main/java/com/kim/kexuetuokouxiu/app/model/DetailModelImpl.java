package com.kim.kexuetuokouxiu.app.model;

import com.kim.kexuetuokouxiu.app.contract.DetailContract;
import com.kim.kexuetuokouxiu.bean.Comment;
import com.kim.kexuetuokouxiu.network.RemoteClient;
import com.kim.kexuetuokouxiu.utils.ParseUtil;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

/**
 * Created by Weya on 2016/12/04
 */

public class DetailModelImpl implements DetailContract.Model {

    @Override
    public void getComments(String link, final Callback callback) {
        RemoteClient.getComments(link, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                callback.onStart();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String xml = response.get();
                callback.onSucceed(ParseUtil.parseXml2Comments(xml));
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

        void onSucceed(List<Comment> comments);

        void onFailed();

        void onFinish();
    }
}