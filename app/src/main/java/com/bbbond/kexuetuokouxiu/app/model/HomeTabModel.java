package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.HomeTabContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;
import com.bbbond.kexuetuokouxiu.network.RemoteClient;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;
import com.google.gson.Gson;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/4/30.
 */

public class HomeTabModel implements HomeTabContract.Model {

    /**
     * 通过类别获取节目列表
     * 本地优先
     * @param callback 异步获取节目的回调
     * @param category 节目类型
     */
    @Override
    public void getProgrammeListByCategories(final ProgrammeListCallback callback, final String[] category) {
        fetchRemoteIfNeed(new ProgrammeListCallback() {
            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                List<Programme> programmeList = ProgrammeDao.getProgrammeListByCategories(category);
//                LogUtil.d(HomeTabModel.class, "getProgrammeListByCategories", programmeList.toString());
                callback.onSucceed(programmeList);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    /**
     * 通过类别获取节目列表
     * 获取远程，服务器优先
     * @param callback 异步获取节目的回调
     * @param category 节目类型
     */
    @Override
    public void getProgrammeListRemoteByCategories(final ProgrammeListCallback callback, final String[] category) {
        fetchRemote(new ProgrammeListCallback() {
            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                List<Programme> programmeList = ProgrammeDao.getProgrammeListByCategories(category);
                callback.onSucceed(programmeList);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    /**
     * 如果有需要通过远程获取
     * @param callback 异步获取节目的回调
     */
    private void fetchRemoteIfNeed(ProgrammeListCallback callback) {
        callback.onStart();
        List<Programme> programmeList = ProgrammeDao.getAllProgrammeList();
        if (programmeList == null || programmeList.size() == 0) {
            fetchJsonRemote(callback);
        } else {
            callback.onSucceed(programmeList);
//            LogUtil.d(HomeTabModel.class, "fetchRemoteIfNeed", programmeList.toString());
        }
    }

    /**
     * 通过服务器远程获取
     * @param callback 异步获取节目的回调
     */
    @Override
    public void fetchJsonRemote(final ProgrammeListCallback callback) {
        RemoteClient.getProgrammesFromJson(new SimpleResponseListener<JSONArray>() {
            @Override
            public void onStart(int what) {
                super.onStart(what);
                callback.onStart();
            }

            @Override
            public void onSucceed(int what, Response<JSONArray> response) {
                List<Programme> programmes = new ArrayList<>();
                JSONArray jsonArray = response.get();
                Gson gson = new Gson();
                int length = jsonArray.length();
                try {
                    for (int i = 0; i < length; i++) {
                        programmes.add(gson.fromJson(jsonArray.get(i).toString(), Programme.class));
                    }
                } catch (Exception e) {
                    programmes = null;
                }
                ProgrammeDao.saveOrUpdate(programmes);
                callback.onSucceed(programmes);
//                LogUtil.d(HomeTabModel.class, "fetchJsonRemote", "从服务器获取");
//                LogUtil.d(HomeTabModel.class, "fetchJsonRemote", programmes.toString());
            }

            @Override
            public void onFailed(int what, Response<JSONArray> response) {
                fetchRemote(callback);
            }
        });
    }

    /**
     * 通过官网远程获取
     * @param callback 异步获取节目的回调
     */
    private void fetchRemote(final ProgrammeListCallback callback) {
        RemoteClient.getScienceTalkShow(new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                List<Programme> programmes = ParseUtil.parseXml2ProgrammeList(response.get());
                ProgrammeDao.saveOrUpdate(programmes);
                callback.onSucceed(programmes);
//                LogUtil.d(HomeTabModel.class, "fetchRemote", "从官网获取");
//                LogUtil.d(HomeTabModel.class, "fetchRemote", programmes);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                callback.onFailed();
            }
        });
    }

    public interface ProgrammeListCallback {

        void onStart();

        void onSucceed(List<Programme> programmes);

        void onFailed();
    }
}
