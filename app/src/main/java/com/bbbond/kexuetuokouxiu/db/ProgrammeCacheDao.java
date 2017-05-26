package com.bbbond.kexuetuokouxiu.db;


import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
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
 * 缓存节目数据层
 * Created by bbbond on 2017/5/15.
 */

public class ProgrammeCacheDao extends BaseDao {

    private static class ProgrammeCacheDaoHolder {
        private static final ProgrammeCacheDao instance = new ProgrammeCacheDao();
    }

    public static ProgrammeCacheDao getInstance() {
        return ProgrammeCacheDaoHolder.instance;
    }

    private ProgrammeCacheDao() {
    }

    public void saveOrUpdate(final ProgrammeCache programmeCache) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(programmeCache);
            }
        });
    }

    public void deleteById(final String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ProgrammeCache.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    public ProgrammeCache getCacheById(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ProgrammeCache first = realm.where(ProgrammeCache.class).equalTo("id", id).findFirst();
        if (first == null) {
            realm.commitTransaction();
            return null;
        } else {
            ProgrammeCache cache = realm.copyFromRealm(first);
            realm.commitTransaction();
            return cache;
        }
    }

    public Long size() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        long count = realm.where(ProgrammeCache.class).count();
        realm.commitTransaction();
        return count;
    }

    public Long sizeOfCategories(final String[] category) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        long count = realm.where(ProgrammeCache.class).in("category", category).count();
        realm.commitTransaction();
        return count;
    }

    public Long sizeOfCategories(final String[] category, boolean cached) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        long count = realm.where(ProgrammeCache.class).equalTo("isFinished", cached).in("category", category).count();
        realm.commitTransaction();
        return count;
    }

    public List<ProgrammeCache> getAllProgrammeCacheList() {
        List<ProgrammeCache> programmeCaches = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        programmeCaches = realm.copyFromRealm(realm.where(ProgrammeCache.class).findAll());
        realm.commitTransaction();
        return programmeCaches;
    }

    public List<ProgrammeCache> getProgrammeCacheListByCategories(final String[] category) {
        List<ProgrammeCache> programmeCaches = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        programmeCaches = realm.copyFromRealm(realm.where(ProgrammeCache.class).in("category", category).findAll());
        realm.commitTransaction();
        return programmeCaches;
    }


    /*============================================================================================*/


    public Observable<Void> rxSaveOrUpdate(final ProgrammeCache programmeCache) {
        LogUtil.e(ProgrammeCacheDao.class, "rxSaveOrUpdate", programmeCache.toString());
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(final ObservableEmitter<Void> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(programmeCache);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Void> rxDeleteById(final String id) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(final ObservableEmitter<Void> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(ProgrammeCache.class).equalTo("id", id).findFirst().deleteFromRealm();
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ProgrammeCache> rxGetCacheById(final String id) {
        return Observable.create(new ObservableOnSubscribe<ProgrammeCache>() {
            @Override
            public void subscribe(final ObservableEmitter<ProgrammeCache> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ProgrammeCache first = realm.where(ProgrammeCache.class).equalTo("id", id).findFirst();
                        ProgrammeCache cache = realm.copyFromRealm(first);
                        e.onNext(cache);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> rxSize() {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        long count = realm.where(ProgrammeCache.class).count();
                        e.onNext(count);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> rxSizeOfCategories(final String[] category) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        long count = realm.where(ProgrammeCache.class).in("category", category).count();
                        e.onNext(count);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ProgrammeCache>> rxGetAllProgrammeCacheList() {
        return Observable.create(new ObservableOnSubscribe<List<ProgrammeCache>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<ProgrammeCache>> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<ProgrammeCache> all = realm.where(ProgrammeCache.class).findAll();
                        List<ProgrammeCache> caches = realm.copyFromRealm(all);
                        e.onNext(caches);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ProgrammeCache>> rxGetProgrammeCacheListByCategories(final String[] category) {
        return Observable.create(new ObservableOnSubscribe<List<ProgrammeCache>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<ProgrammeCache>> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<ProgrammeCache> all = realm.where(ProgrammeCache.class).in("category", category).findAll();
                        List<ProgrammeCache> caches = realm.copyFromRealm(all);
                        e.onNext(caches);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
