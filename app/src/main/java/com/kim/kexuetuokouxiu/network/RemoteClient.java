package com.kim.kexuetuokouxiu.network;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.StringRequest;

/**
 * Created by Weya on 2016/11/10.
 */

public class RemoteClient {

    private static RequestQueue queue = NoHttp.newRequestQueue();

    public static void getScienceTalkShow(OnResponseListener<String> response) {
        Request<String> request = new StringRequest(MyUrl.ARTICLE_RSS);
        queue.add(0, request, response);
    }

    public static void getComments(String url, OnResponseListener<String> response) {
        Request<String> request = new StringRequest(url);
        queue.add(1, request, response);
    }

}
