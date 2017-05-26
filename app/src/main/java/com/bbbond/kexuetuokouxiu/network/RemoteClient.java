package com.bbbond.kexuetuokouxiu.network;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.JsonArrayRequest;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;
import com.yolanda.nohttp.rest.StringRequest;

import org.json.JSONArray;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求
 * Created by Weya on 2016/11/10.
 */

public class RemoteClient {

    private static RequestQueue queue = NoHttp.newRequestQueue();

    public static void getScienceTalkShow(OnResponseListener<String> response) {
        StringRequest request = new StringRequest(GlobalUrl.ARTICLE_RSS);
        queue.add(0, request, response);
    }

    public static void getScienceTalkShowFromJson(OnResponseListener<String> response) {
        StringRequest request = new StringRequest(GlobalUrl.SCIENCE_TALK_SHOW + "?time=" + System.currentTimeMillis());
        queue.add(1, request, response);
    }

    public static void getProgrammesFromJson(OnResponseListener<JSONArray> response) {
        JsonArrayRequest request = new JsonArrayRequest(GlobalUrl.PROGRAMMES + "?time=" + System.currentTimeMillis());
        queue.add(2, request, response);
    }

    public static void getComments(String url, OnResponseListener<String> response) {
        Request<String> request = new StringRequest(url);
        queue.add(3, request, response);
    }

    public static Observable<String> getProgrammesFromXml() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                getScienceTalkShow(new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        e.onNext(response.get());
                        e.onComplete();
                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        e.onError(response.getException());
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<JSONArray> getProgrammesFromJson() {
        return Observable.create(new ObservableOnSubscribe<JSONArray>() {
            @Override
            public void subscribe(final ObservableEmitter<JSONArray> e) throws Exception {
                getProgrammesFromJson(new SimpleResponseListener<JSONArray>() {
                    @Override
                    public void onSucceed(int what, Response<JSONArray> response) {
                        e.onNext(response.get());
                        e.onComplete();
                    }

                    @Override
                    public void onFailed(int what, Response<JSONArray> response) {
                        e.onError(response.getException());
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
