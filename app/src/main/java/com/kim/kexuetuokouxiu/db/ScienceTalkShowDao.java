package com.kim.kexuetuokouxiu.db;

import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Weya on 2016/12/7.
 */

public class ScienceTalkShowDao {

    public static void saveOrUpdate(final ScienceTalkShow scienceTalkShow) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(scienceTalkShow);
            }
        });
    }

    public static ScienceTalkShow getScienceTalkShow() {
        final ScienceTalkShow[] scienceTalkShow = {null};
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                scienceTalkShow[0] = realm.where(ScienceTalkShow.class).findFirst();
            }
        });
        return scienceTalkShow[0];
    }

}
