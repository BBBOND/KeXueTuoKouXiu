package com.bbbond.kexuetuokouxiu.db;


import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bbbond on 2017/5/15.
 */

public class ProgrammeCacheDao extends BaseDao {

    public static Observable<Void> rxSaveOrUpdate(final ProgrammeCache programmeCache) {
        return createObservable(new Func1<Realm, Void>() {
            @Override
            public Void call(Realm realm) {
                LogUtil.e(ProgrammeCacheDao.class, "saveOrUpdate", programmeCache.toString());
                realm.copyToRealm(programmeCache);
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static void saveOrUpdate(final ProgrammeCache programmeCache) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(programmeCache);
            }
        });
    }

    public static Observable<Void> rxDeleteById(final String id) {
        return createObservable(new Func1<Realm, Void>() {
            @Override
            public Void call(Realm realm) {
                realm.where(ProgrammeCache.class).equalTo("id", id).findFirst().deleteFromRealm();
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static void deleteById(final String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ProgrammeCache.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    public static Observable<Long> rxSize() {
        return createObservable(new Func1<Realm, Long>() {
            @Override
            public Long call(Realm realm) {
                return realm.where(ProgrammeCache.class).count();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Long size() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        long count = realm.where(ProgrammeCache.class).count();
        realm.commitTransaction();
        return count;
    }

    public static Observable<Long> rxSizeOfCategories(final String[] category) {
        return createObservable(new Func1<Realm, Long>() {
            @Override
            public Long call(Realm realm) {
                return realm.where(ProgrammeCache.class).in("category", category).count();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Long sizeOfCategories(final String[] category) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        long count = realm.where(ProgrammeCache.class).in("category", category).count();
        realm.commitTransaction();
        return count;
    }

    public static Observable<List<ProgrammeCache>> rxGetAllProgrammeCacheList() {
        return createObservable(new Func1<Realm, List<ProgrammeCache>>() {
            @Override
            public List<ProgrammeCache> call(Realm realm) {
                return realm.where(ProgrammeCache.class).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static List<ProgrammeCache> getAllProgrammeCacheList() {
        List<ProgrammeCache> programmeCaches = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        programmeCaches = realm.copyFromRealm(realm.where(ProgrammeCache.class).findAll());
        realm.commitTransaction();
        return programmeCaches;
    }

    public static Observable<List<ProgrammeCache>> rxGetProgrammeCacheListByCategories(final String[] category) {
        return createObservable(new Func1<Realm, List<ProgrammeCache>>() {
            @Override
            public List<ProgrammeCache> call(Realm realm) {
                return realm.where(ProgrammeCache.class).in("category", category).findAll();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static List<ProgrammeCache> getProgrammeCacheListByCategories(final String[] category) {
        List<ProgrammeCache> programmeCaches = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        programmeCaches = realm.copyFromRealm(realm.where(ProgrammeCache.class).in("category", category).findAll());
        realm.commitTransaction();
        return programmeCaches;
    }
}
