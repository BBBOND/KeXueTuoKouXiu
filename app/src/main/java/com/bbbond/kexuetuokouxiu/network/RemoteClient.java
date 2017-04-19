package com.bbbond.kexuetuokouxiu.network;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.JsonArrayRequest;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.StringRequest;

import org.json.JSONArray;

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
}
