package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.app.model.HomeTabModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

/**
 * Created by bbbond on 2017/4/30.
 */

public interface HomeTabContract {
    interface Model {
        void getProgrammeListByCategories(HomeTabModel.ProgrammeListCallback callback, String[] category);

        void getProgrammeListRemoteByCategories(HomeTabModel.ProgrammeListCallback callback, String[] category);

        void fetchJsonRemote(HomeTabModel.ProgrammeListCallback callback);
    }

    interface View {
        void setPresenter(Presenter presenter);

        void refreshing(boolean isRefreshing);

        void receiveProgrammeList(List<Programme> programmes);
    }

    interface Presenter {
        void getProgrammeList(String[] category);

        void getProgrammeListRemote(String[] category);
    }
}
