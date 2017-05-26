package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 节目数据层
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

    public List<Programme> searchProgrammeListByKey(String key) {
        List<Programme> programmes = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Programme> all = realm.where(Programme.class).contains("title", key).or().contains("category", key).findAll();
        programmes = realm.copyFromRealm(all);
        realm.commitTransaction();
        return programmes;
    }

    public Observable<Programme> rxGetProgrammeById(final String id) {
        return Observable.create(new ObservableOnSubscribe<Programme>() {
            @Override
            public void subscribe(final ObservableEmitter<Programme> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Programme first = realm.where(Programme.class).equalTo("id", id).findAll().first();
                        Programme programme = realm.copyFromRealm(first);
                        e.onNext(programme);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Programme>> rxSearchProgrammeListByKey(final String key) {
        return Observable.create(new ObservableOnSubscribe<List<Programme>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Programme>> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Programme> all = realm.where(Programme.class).contains("title", key).or().contains("category", key).findAll();
                        List<Programme> programmes = realm.copyFromRealm(all);
                        e.onNext(programmes);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Void> rxSaveOrUpdate(final List<Programme> programmes) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(final ObservableEmitter<Void> e) throws Exception {
                LogUtil.e(ProgrammeDao.class, "rxSaveOrUpdate", programmes.size() + "");
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(programmes);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Programme>> rxGetAllProgrammeList() {
        return Observable.create(new ObservableOnSubscribe<List<Programme>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Programme>> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Programme> all = realm.where(Programme.class).findAll();
                        List<Programme> programmes = realm.copyFromRealm(all);
                        e.onNext(programmes);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Programme>> rxGetProgrammeListByCategories(final String[] category) {
        return Observable.create(new ObservableOnSubscribe<List<Programme>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Programme>> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Programme> all = realm.where(Programme.class).in("category", category).findAll();
                        List<Programme> programmes = realm.copyFromRealm(all);
                        e.onNext(programmes);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
