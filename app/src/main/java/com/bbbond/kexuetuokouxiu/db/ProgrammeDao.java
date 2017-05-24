package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bbbond on 2017/4/20.
 */

public class ProgrammeDao extends BaseDao {

    private static class ProgrammeDaoHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static final ProgrammeDao instance = new ProgrammeDao();
    }

    public static ProgrammeDao getInstance() {
        return ProgrammeDaoHolder.instance;
    }

    private ProgrammeDao() {
    }

    public Observable<Void> saveOrUpdate(final List<Programme> programmes) {
        return createObservable(new Func1<Realm, Void>() {
            @Override
            public Void call(Realm realm) {
                LogUtil.e(ProgrammeDao.class, "saveOrUpdate", programmes.size() + "");
                realm.copyToRealmOrUpdate(programmes);
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Programme>> getAllProgrammeList() {
        return createObservable(new Func1<Realm, List<Programme>>() {
            @Override
            public List<Programme> call(Realm realm) {
                return realm.where(Programme.class).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Programme>> rxGetProgrammeListByCategories(final String[] category) {
        return createObservable(new Func1<Realm, List<Programme>>() {
            @Override
            public List<Programme> call(Realm realm) {
                return realm.where(Programme.class).in("category", category).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Programme getProgrammeById(final String id) {
        Programme programme = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Programme first = realm.where(Programme.class).equalTo("id", id).findAll().first();
        if (first != null)
            programme = realm.copyFromRealm(first);
        realm.commitTransaction();
        return programme;
    }

    public List<Programme> getProgrammeListByKey(String key) {
        List<Programme> programmes = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Programme> all = realm.where(Programme.class).contains("title", key).or().contains("category", key).findAll();
        programmes = realm.copyFromRealm(all);
        realm.commitTransaction();
        return programmes;
    }
}
