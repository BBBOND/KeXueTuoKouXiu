package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

import rx.Observable;

/**
 * Created by bbbond on 2017/4/30.
 */

public interface TabContract {
    interface Model {
        Observable<List<Programme>> getProgrammeListFromLocalByCategories(String[] category);

        Observable<Void> saveProgrammeList(List<Programme> programmeList);

        Observable<List<Programme>> fetchRemoteFromJson();

        Observable<List<Programme>> fetchRemoteFromXml();
    }

    interface View {
        void setPresenter(Presenter presenter);

        void refreshing(boolean isRefreshing);

        void receiveProgrammeList(List<Programme> programmes);

        void showToast(String msg);
    }

    interface Presenter {
        void getProgrammeList(String[] category, boolean shouldFetchRemote);

        void getProgrammeListRemote(String[] category);
    }
}
