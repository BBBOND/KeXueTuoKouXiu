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

    public static void saveOrUpdateAsync(final List<Programme> programmes) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(programmes);
            }
        });
    }

    public static void saveOrUpdate(final List<Programme> programmes) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(programmes);
            }
        });
    }

    public static List<Programme> getAllProgrammeList() {
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

    public static List<Programme> getProgrammeListByCategories(final String[] category) {
        final List<Programme> programmeList = new ArrayList<>();
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Programme> all = realm.where(Programme.class).in("category", category).findAll();
                programmeList.addAll(all);
            }
        });
        return programmeList;
    }
}
