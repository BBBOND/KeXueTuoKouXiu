package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.TabContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;
import com.bbbond.kexuetuokouxiu.network.RemoteClient;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bbbond on 2017/4/30.
 */

public class TabModel implements TabContract.Model {

    /**
     * 通过类别获取节目列表
     *
     * @param category 节目类型
     * @return Observable<List<Programme>>
     */
    @Override
    public Observable<List<Programme>> getProgrammeListFromLocalByCategories(final String[] category) {
        return ProgrammeDao.getInstance().rxGetProgrammeListByCategories(category);
    }

    /**
     * 保存programmeList
     *
     * @param programmeList 需要保存的数据
     * @return Observable\<Void\>
     */
    @Override
    public Observable<Void> saveProgrammeList(List<Programme> programmeList) {
        return ProgrammeDao.getInstance().saveOrUpdate(programmeList);
    }

    /**
     * 通过服务器远程获取
     *
     * @return Observable\<List\<Programme\>\>
     */
    @Override
    public Observable<List<Programme>> fetchRemoteFromJson() {
        LogUtil.d(TabModel.class, "fetchRemoteFromJson", "");
        return RemoteClient
                .getProgrammesFromJson()
                .flatMap(new Func1<JSONArray, Observable<List<Programme>>>() {
                    @Override
                    public Observable<List<Programme>> call(final JSONArray jsonArray) {
                        return Observable.create(new Observable.OnSubscribe<List<Programme>>() {
                            @Override
                            public void call(Subscriber<? super List<Programme>> subscriber) {
                                try {
                                    List<Programme> programmes = new ArrayList<>();
                                    Gson gson = new Gson();
                                    int length = jsonArray.length();
                                    try {
                                        for (int i = 0; i < length; i++) {
                                            programmes.add(gson.fromJson(jsonArray.get(i).toString(), Programme.class));
                                        }
                                    } catch (Exception e) {
                                        programmes = null;
                                    }
                                    subscriber.onNext(programmes);
                                    subscriber.onCompleted();
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 通过官网远程获取
     *
     * @return Observable\<List\<Programme\>\>
     */
    @Override
    public Observable<List<Programme>> fetchRemoteFromXml() {
        LogUtil.d(TabModel.class, "fetchRemoteFromXml", "");
        return RemoteClient
                .getProgrammesFromXml()
                .flatMap(new Func1<String, Observable<List<Programme>>>() {
                    @Override
                    public Observable<List<Programme>> call(final String s) {
                        return Observable.create(new Observable.OnSubscribe<List<Programme>>() {
                            @Override
                            public void call(Subscriber<? super List<Programme>> subscriber) {
                                try {
                                    List<Programme> programmeList = ParseUtil.parseXml2ProgrammeList(s);
                                    subscriber.onNext(programmeList);
                                    subscriber.onCompleted();
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
