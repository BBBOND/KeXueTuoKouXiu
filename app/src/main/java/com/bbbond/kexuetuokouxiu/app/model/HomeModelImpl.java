package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.HomeContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ScienceTalkShow;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;
import com.bbbond.kexuetuokouxiu.db.ScienceTalkShowDao;
import com.bbbond.kexuetuokouxiu.network.RemoteClient;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;
import com.google.gson.Gson;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据获取
 * Created by Weya on 2016/12/03
 */

public class HomeModelImpl implements HomeContract.Model {

    /**
     * 从官网获取ScienceTalkShow
     *
     * @param callback
     */
    private void getScienceTalkShowFromRemote(final ScienceTalkShowCallback callback) {
        RemoteClient.getScienceTalkShow(new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String xml = response.get();
                ScienceTalkShow scienceTalkShow = ParseUtil.parseXml2ScienceTalkShow(xml);
                callback.onSucceed(scienceTalkShow);
                ScienceTalkShowDao.saveOrUpdate(scienceTalkShow);
                LogUtil.d(HomeModelImpl.class, "ScienceTalkShow", "从官网获取");
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                callback.onFailed();
            }
        });
    }

    /**
     * 从服务器获取ScienceTalkShow
     *
     * @param callback
     */
    @Override
    public void getScienceTalkShowFromJsonRemote(final ScienceTalkShowCallback callback) {
        RemoteClient.getScienceTalkShowFromJson(new SimpleResponseListener<String>() {
            @Override
            public void onStart(int what) {
                callback.onStart();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Gson gson = new Gson();
                ScienceTalkShow scienceTalkShow = gson.fromJson(response.get(), ScienceTalkShow.class);
                callback.onSucceed(scienceTalkShow);
                ScienceTalkShowDao.saveOrUpdate(scienceTalkShow);
                LogUtil.d(HomeModelImpl.class, "ScienceTalkShow", "从服务器获取");
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                getScienceTalkShowFromRemote(callback);
            }
        });
    }

    /**
     * 从本地获取ScienceTalkShow
     *
     * @param callback
     */
    @Override
    public void getScienceTalkShowFromLocal(final ScienceTalkShowCallback callback) {
        callback.onStart();
        try {
            ScienceTalkShow scienceTalkShow = ScienceTalkShowDao.getScienceTalkShow();
            callback.onSucceed(scienceTalkShow);
            LogUtil.d(HomeModelImpl.class, "ScienceTalkShow", "从本地获取");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed();
        }
    }

    /**
     * 从本地获取ProgrammeList
     *
     * @param callback
     */
    @Override
    public void getProgrammeListFromLocal(ProgrammeListCallback callback) {
        callback.onStart();
        try {
//            List<Programme> programmes = ProgrammeDao.getAllProgrammeList();
//            callback.onSucceed(programmes);
            LogUtil.d(HomeModelImpl.class, "ProgrammeList", "从本地获取");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed();
        }
    }

    /**
     * 从服务器获取ProgrammeList
     *
     * @param callback
     */
    @Override
    public void getProgrammeListFromJsonRemote(final ProgrammeListCallback callback) {
        RemoteClient.getProgrammesFromJson(new SimpleResponseListener<JSONArray>() {
            @Override
            public void onStart(int what) {
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
                LogUtil.d(HomeModelImpl.class, "ProgrammeList", "从服务器获取");
            }

            @Override
            public void onFailed(int what, Response<JSONArray> response) {
                getProgrammeListFromRemote(callback);
            }
        });
    }

    @Override
    public void getProgrammeListFromRemoteByCategories(ProgrammeListCallback callback, String... category) {
        callback.onStart();
        try {
//            List<Programme> programmeList = ProgrammeDao.getProgrammeListByCategories(category);
//            callback.onSucceed(programmeList);
        } catch (Exception e) {
            LogUtil.e(HomeModelImpl.class, "getProgrammeListFromRemoteByCategories", e.getMessage());
            callback.onFailed();
        }
    }

    /**
     * 从官网获取ProgrammeList
     *
     * @param callback
     */
    private void getProgrammeListFromRemote(final ProgrammeListCallback callback) {
        RemoteClient.getScienceTalkShow(new SimpleResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                List<Programme> programmeList = ParseUtil.parseXml2ProgrammeList(response.get());
                ProgrammeDao.saveOrUpdate(programmeList);
                callback.onSucceed(programmeList);
                LogUtil.d(HomeModelImpl.class, "ProgrammeList", "从官网获取");
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                callback.onFailed();
            }
        });
    }

    public interface ScienceTalkShowCallback {

        void onStart();

        void onSucceed(ScienceTalkShow scienceTalkShow);

        void onFailed();
    }

    public interface ProgrammeListCallback {

        void onStart();

        void onSucceed(List<Programme> programmes);

        void onFailed();
    }
}