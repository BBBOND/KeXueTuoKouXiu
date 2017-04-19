package com.bbbond.kexuetuokouxiu.db;

import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bbbond on 2017/4/20.
 */

public class ProgrammeDao {

    public static void saveOrUpdate(final List<Programme> programmes) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(programmes);
            }
        });
    }

    public static List<Programme> getScienceTalkShow() {
        final List<Programme> programmes = new ArrayList<>();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Programme> all = realm.where(Programme.class).findAll();
                programmes.addAll(all);
            }
        });
        return programmes;
    }
}
