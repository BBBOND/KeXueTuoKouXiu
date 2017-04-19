package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.app.model.MainModelImpl;
import com.bbbond.kexuetuokouxiu.bean.ScienceTalkShow;

/**
 * Created by Weya on 2016/12/3.
 */

public class MainContract {

    public interface View {
        void showRefreshProgress();

        void hideRefreshProgress();

        void receiveScienceTalkShow(ScienceTalkShow scienceTalkShow);
    }

    public interface Presenter {
        void getScienceTalkShowFromLocal();

        void getScienceTalkShowFromRemote();
    }

    public interface Model {
        void getScienceTalkShow(MainModelImpl.Callback callback, int from);
    }
}