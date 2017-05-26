package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.ScienceTalkShow;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * 科学脱口秀数据层
 * Created by Weya on 2016/12/7.
 */

public class ScienceTalkShowDao {

    private static class ScienceTalkShowDaoHolder {
        private static final ScienceTalkShowDao instance = new ScienceTalkShowDao();
    }

    public static ScienceTalkShowDao getInstance() {
        return ScienceTalkShowDaoHolder.instance;
    }

    private ScienceTalkShowDao() {
    }

    public void saveOrUpdate(final ScienceTalkShow scienceTalkShow) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(scienceTalkShow);
            }
        });
    }

    public ScienceTalkShow getScienceTalkShow() {
        ScienceTalkShow scienceTalkShow;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ScienceTalkShow first = realm.where(ScienceTalkShow.class).findFirst();
        scienceTalkShow = realm.copyFromRealm(first);
        realm.commitTransaction();
        return scienceTalkShow;
    }

    /*============================================================================================*/

    public Observable<Void> rxSaveOrUpdate(final ScienceTalkShow scienceTalkShow) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(scienceTalkShow);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ScienceTalkShow> rxGetScienceTalkShow() {
        return Observable.create(new ObservableOnSubscribe<ScienceTalkShow>() {
            @Override
            public void subscribe(final ObservableEmitter<ScienceTalkShow> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ScienceTalkShow first = realm.where(ScienceTalkShow.class).findFirst();
                        ScienceTalkShow scienceTalkShow = realm.copyFromRealm(first);
                        e.onNext(scienceTalkShow);
                        e.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
