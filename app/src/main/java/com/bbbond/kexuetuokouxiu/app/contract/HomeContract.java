package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.app.model.HomeModelImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

/**
 * Created by Weya on 2016/12/3.
 */

public class HomeContract {

    public interface View {
        void showRefreshProgress();

        void hideRefreshProgress();

        void receiveProgrammeList(List<Programme> programmes);

        void setPresenter(Presenter presenter);
    }

    public interface Presenter {
//        void getScienceTalkShowFromLocalFirst();
//
//        void getScienceTalkShowFromRemote();

        void getProgrammeListFromLocalFirst();

        void getProgrammeListFromRemote();

        void loadNextPage();
    }

    public interface Model {
        void getScienceTalkShowFromJsonRemote(HomeModelImpl.ScienceTalkShowCallback callback);

        void getScienceTalkShowFromLocal(HomeModelImpl.ScienceTalkShowCallback callback);

        void getProgrammeListFromLocal(HomeModelImpl.ProgrammeListCallback callback);

        void getProgrammeListFromJsonRemote(HomeModelImpl.ProgrammeListCallback callback);

        void getProgrammeListFromRemoteByCategories(HomeModelImpl.ProgrammeListCallback callback, String... category);
    }
}