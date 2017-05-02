package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by bbbond on 2017/4/20.
 */

public class ProgrammeDao extends BaseDao {

    public static Observable<Void> saveOrUpdate(final List<Programme> programmes) {
        return createObservable(new Func1<Realm, Void>() {
            @Override
            public Void call(Realm realm) {
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
}
