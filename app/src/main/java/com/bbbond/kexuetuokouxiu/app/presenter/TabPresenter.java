package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.contract.TabContract;
import com.bbbond.kexuetuokouxiu.app.model.TabModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bbbond on 2017/4/30.
 */

public class TabPresenter implements TabContract.Presenter {

//    private static final int PAGE_SIZE = 100;
//    int page = 0;

    private TabContract.View view;
    private TabContract.Model model;
    private List<Programme> allCategoryProgrammeList;
    private List<Programme> allProgrammeList;

    public TabPresenter(TabContract.View view) {
        this.view = view;
        model = new TabModel();
        view.setPresenter(this);
    }

    @Override
    public void getProgrammeList(final String[] category, final boolean shouldFetchRemote) {
        model.getProgrammeListFromLocalByCategories(category)
                .subscribe(new Observer<List<Programme>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.refreshing(true);
                    }

                    @Override
                    public void onNext(List<Programme> programmes) {
                        allCategoryProgrammeList = programmes;
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TabPresenter.class, "getProgrammeList", e.getMessage());
                        view.refreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        if ((allCategoryProgrammeList == null || allCategoryProgrammeList.size() == 0) && shouldFetchRemote)
                            getProgrammeListRemote(category);
                        else {
                            view.receiveProgrammeList(allCategoryProgrammeList);
                            view.refreshing(false);
                        }
                    }
                });
    }

    @Override
    public void getProgrammeListRemote(final String[] category) {
        LogUtil.d(TabPresenter.class, "getProgrammeListRemote", "");
        model.fetchRemoteFromJson()
                .subscribe(new Observer<List<Programme>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.refreshing(true);
                    }

                    @Override
                    public void onNext(List<Programme> programmes) {
                        allProgrammeList = programmes;
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TabPresenter.class, "getProgrammeListRemote", e.getMessage());
                        view.refreshing(false);
                        view.showToast("获取失败，请检查网络！");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TabPresenter.class, "getProgrammeListRemote", allProgrammeList.size());
                        if (allProgrammeList == null || allProgrammeList.size() == 0) {
                            LogUtil.d(TabPresenter.class, "getProgrammeListRemote", "getProgrammeListRemoteFromXml");
                            getProgrammeListRemoteFromXml(category);
                        } else {
                            LogUtil.d(TabPresenter.class, "getProgrammeListRemote", "saveProgrammeList");
                            saveProgrammeList(category);
                        }
                    }
                });
    }

    private void getProgrammeListRemoteFromXml(final String[] category) {
        LogUtil.d(TabPresenter.class, "getProgrammeListRemoteFromXml", "");
        model.fetchRemoteFromXml()
                .subscribe(new Observer<List<Programme>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.refreshing(true);
                    }

                    @Override
                    public void onNext(List<Programme> programmes) {
                        allProgrammeList = programmes;
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TabPresenter.class, "getProgrammeListRemoteFromXml", e.getMessage());
                        view.refreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        if (allCategoryProgrammeList != null && allCategoryProgrammeList.size() > 0)
                            saveProgrammeList(category);
                        else
                            view.refreshing(false);
                    }
                });
    }

    private void saveProgrammeList(final String[] category) {
        LogUtil.d(TabPresenter.class, "saveProgrammeList", "");
        model.saveProgrammeList(allProgrammeList)
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.refreshing(true);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        LogUtil.d(TabPresenter.class, "saveProgrammeList", "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TabPresenter.class, "saveProgrammeList", e.getMessage());
                        view.refreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TabPresenter.class, "saveProgrammeList", "onCompleted");
                        getProgrammeList(category, false);
                    }
                });
    }

//    private void saveProgrammeList(final String[] category) {
//        LogUtil.d(TabPresenter.class, "saveProgrammeList", "page: " + Math.min(page * PAGE_SIZE, allProgrammeList.size()) + "-" + Math.min((page + 1) * PAGE_SIZE, allProgrammeList.size()));
//        model.saveProgrammeList(allProgrammeList.subList(Math.min(page * PAGE_SIZE, allProgrammeList.size()), Math.min((page + 1) * PAGE_SIZE, allProgrammeList.size())))
//                .subscribe(new Subscriber<Void>() {
//                    @Override
//                    public void onStart() {
//                        view.refreshing(true);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.d(TabPresenter.class, "saveProgrammeList", "page: " + page + " onCompleted");
//                        page++;
//                        if (page * PAGE_SIZE <= allProgrammeList.size()) {
//                            saveProgrammeList(category);
//                        } else {
//                            page = 0;
//                            getProgrammeList(category, false);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e(TabPresenter.class, "saveProgrammeList", e.getCause().getMessage());
//                        view.refreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(Void aVoid) {
//                        LogUtil.d(TabPresenter.class, "saveProgrammeList", "onNext");
//                    }
//                });
//    }
}
