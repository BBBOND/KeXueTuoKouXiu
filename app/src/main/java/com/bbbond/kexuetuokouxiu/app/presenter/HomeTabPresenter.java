package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.contract.HomeTabContract;
import com.bbbond.kexuetuokouxiu.app.model.HomeTabModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

/**
 * Created by bbbond on 2017/4/30.
 */

public class HomeTabPresenter implements HomeTabContract.Presenter {

    private HomeTabContract.View view;
    private HomeTabContract.Model model;

    public HomeTabPresenter(HomeTabContract.View view) {
        this.view = view;
        model = new HomeTabModel();
        view.setPresenter(this);
    }

    @Override
    public void getProgrammeList(String[] category) {
        model.getProgrammeListByCategories(new HomeTabModel.ProgrammeListCallback() {
            @Override
            public void onStart() {
                view.refreshing(true);
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                view.receiveProgrammeList(programmes);
                view.refreshing(false);
            }

            @Override
            public void onFailed() {
                view.refreshing(false);
            }
        }, category);
    }

    @Override
    public void getProgrammeListRemote(String[] category) {
        model.getProgrammeListRemoteByCategories(new HomeTabModel.ProgrammeListCallback() {
            @Override
            public void onStart() {
                view.refreshing(true);
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                view.receiveProgrammeList(programmes);
                view.refreshing(false);
            }

            @Override
            public void onFailed() {
                view.refreshing(false);
            }
        }, category);
    }
}
