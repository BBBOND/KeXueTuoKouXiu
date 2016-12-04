package com.kim.kexuetuokouxiu.app.contract;

import com.kim.kexuetuokouxiu.app.model.MainModelImpl;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

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
        void getScienceTalkShow();
    }

    public interface Model {
        void getScienceTalkShow(MainModelImpl.Callback callback, int from);
    }
}