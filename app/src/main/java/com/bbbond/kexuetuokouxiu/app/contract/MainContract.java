package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.app.model.MainModelImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ScienceTalkShow;

import java.util.List;

/**
 * Created by Weya on 2016/12/3.
 */

public class MainContract {

    public interface View {
        void showRefreshProgress();

        void hideRefreshProgress();

        void receiveProgrammeList(List<Programme> programmes);
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
        void getScienceTalkShowFromJsonRemote(MainModelImpl.ScienceTalkShowCallback callback);

        void getScienceTalkShowFromLocal(MainModelImpl.ScienceTalkShowCallback callback);

        void getProgrammeListFromLocal(MainModelImpl.ProgrammeListCallback callback);

        void getProgrammeListFromJsonRemote(MainModelImpl.ProgrammeListCallback callback);
    }
}