package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bbbond on 2017/4/20.
 */

public class ProgrammeDao extends BaseDao {

    public static Observable<Void> saveOrUpdate(final List<Programme> programmes) {
        return createObservable(new Func1<Realm, Void>() {
            @Override
            public Void call(Realm realm) {
                LogUtil.e(ProgrammeDao.class, "saveOrUpdate", programmes.size() + "");
                realm.copyToRealmOrUpdate(programmes);
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Programme>> getAllProgrammeList() {
        return createObservable(new Func1<Realm, List<Programme>>() {
            @Override
            public List<Programme> call(Realm realm) {
                return realm.where(Programme.class).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Programme>> getProgrammeListByCategories(final String[] category) {
        return createObservable(new Func1<Realm, List<Programme>>() {
            @Override
            public List<Programme> call(Realm realm) {
                return realm.where(Programme.class).in("category", category).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Programme getProgrammeById(final String id) {
        Programme programme = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Programme first = realm.where(Programme.class).equalTo("id", id).findAll().first();
        if (first != null)
            programme = realm.copyFromRealm(first);
        realm.commitTransaction();
        return programme;
    }
}
