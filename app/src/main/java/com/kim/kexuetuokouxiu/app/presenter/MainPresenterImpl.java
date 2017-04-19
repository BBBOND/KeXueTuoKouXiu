package com.kim.kexuetuokouxiu.app.presenter;

import com.kim.kexuetuokouxiu.app.activity.MainActivity;
import com.kim.kexuetuokouxiu.app.contract.MainContract;
import com.kim.kexuetuokouxiu.app.model.MainModelImpl;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;
import com.kim.kexuetuokouxiu.helper.LogHelper;

/**
 * Created by Weya on 2016/11/10.
 */

public class MainPresenterImpl implements MainContract.Presenter {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private MainContract.View view;
    private MainContract.Model model;

    public MainPresenterImpl(MainActivity activity) {
        this.view = activity;
        model = new MainModelImpl();
    }

    @Override
    public void getScienceTalkShowFromLocal() {
        model.getScienceTalkShow(new MainModelImpl.Callback() {
            @Override
            public void onStart() {
                view.showRefreshProgress();
            }

            @Override
            public void onSucceed(ScienceTalkShow scienceTalkShow) {
                LogHelper.d(TAG, "from local ===> ", scienceTalkShow);
                view.receiveScienceTalkShow(scienceTalkShow);
            }

            @Override
            public void onFailed() {
                getScienceTalkShowFromRemote();
            }

            @Override
            public void onFinish() {
                view.hideRefreshProgress();
            }
        }, MainModelImpl.FROM_LOCAL);
    }

    @Override
    public void getScienceTalkShowFromRemote() {
        model.getScienceTalkShow(new MainModelImpl.Callback() {
            @Override
            public void onStart() {
                view.showRefreshProgress();
            }

            @Override
            public void onSucceed(ScienceTalkShow scienceTalkShow) {
                LogHelper.d(TAG, "from remote ===> ", scienceTalkShow);
                view.receiveScienceTalkShow(scienceTalkShow);
            }

            @Override
            public void onFailed() {
//                view.hideRefreshProgress();
            }

            @Override
            public void onFinish() {
                view.hideRefreshProgress();
            }
        }, MainModelImpl.FROM_REMOTE);
    }
}
