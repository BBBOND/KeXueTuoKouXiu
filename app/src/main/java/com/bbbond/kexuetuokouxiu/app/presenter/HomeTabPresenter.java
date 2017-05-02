package com.bbbond.kexuetuokouxiu.app.presenter;

import android.content.Context;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.app.contract.HomeTabContract;
import com.bbbond.kexuetuokouxiu.app.model.HomeTabModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.List;

import rx.Subscriber;

/**
 * Created by bbbond on 2017/4/30.
 */

public class HomeTabPresenter implements HomeTabContract.Presenter {

    private HomeTabContract.View view;
    private HomeTabContract.Model model;
    private List<Programme> allCategoryProgrammeList;
    private List<Programme> allProgrammeList;

    public HomeTabPresenter(HomeTabContract.View view) {
        this.view = view;
        model = new HomeTabModel();
        view.setPresenter(this);
    }

    @Override
    public void getProgrammeList(final String[] category, final boolean shouldFetchRemote) {
        model.getProgrammeListFromLocalByCategories(category)
                .subscribe(new Subscriber<List<Programme>>() {

                    @Override
                    public void onStart() {
                        view.refreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                        if ((allCategoryProgrammeList == null || allCategoryProgrammeList.size() == 0) && shouldFetchRemote)
                            getProgrammeListRemote(category);
                        else
                            view.receiveProgrammeList(allCategoryProgrammeList);
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(HomeTabPresenter.class, "getProgrammeList", e.getMessage());
                        view.refreshing(false);
                    }

                    @Override
                    public void onNext(List<Programme> programmeList) {
                        allCategoryProgrammeList = programmeList;
                    }
                });
    }

    @Override
    public void getProgrammeListRemote(final String[] category) {
        model.fetchRemoteFromJson()
                .subscribe(new Subscriber<List<Programme>>() {

                    @Override
                    public void onStart() {
                        view.refreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                        if (allProgrammeList == null || allProgrammeList.size() == 0)
                            getProgrammeListRemoteFromXml(category);
                        else
                            saveProgrammeList(category);
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(HomeTabPresenter.class, "getProgrammeListRemote", e.getMessage());
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<Programme> programmeList) {
                        allProgrammeList = programmeList;
                    }
                });
    }

    private void getProgrammeListRemoteFromXml(final String[] category) {
        model.fetchRemoteFromXml()
                .subscribe(new Subscriber<List<Programme>>() {

                    @Override
                    public void onStart() {
                        view.refreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                        if (allCategoryProgrammeList != null && allCategoryProgrammeList.size() > 0)
                            saveProgrammeList(category);
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(HomeTabPresenter.class, "getProgrammeListRemoteFromXml", e.getMessage());
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<Programme> programmeList) {
                        allProgrammeList = programmeList;
                    }
                });
    }

    private void saveProgrammeList(final String[] category) {
        model.saveProgrammeList(allProgrammeList)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onStart() {
                        view.refreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                        getProgrammeList(category, false);
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.refreshing(false);
                        if (!this.isUnsubscribed())
                            this.unsubscribe();
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }
}
