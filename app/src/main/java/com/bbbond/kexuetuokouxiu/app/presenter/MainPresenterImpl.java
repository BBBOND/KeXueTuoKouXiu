package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.activity.MainActivity;
import com.bbbond.kexuetuokouxiu.app.contract.MainContract;
import com.bbbond.kexuetuokouxiu.app.model.MainModelImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weya on 2016/11/10.
 */

public class MainPresenterImpl implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Model model;

    private List<Programme> programmeAllList;
    private int pageIndex = 1;

    public MainPresenterImpl(MainActivity activity) {
        this.view = activity;
        model = new MainModelImpl();
    }

//    @Override
//    public void getScienceTalkShowFromLocalFirst() {
//        model.getScienceTalkShowFromLocal(new MainModelImpl.ScienceTalkShowCallback() {
//            @Override
//            public void onStart() {
//                view.showRefreshProgress();
//            }
//
//            @Override
//            public void onSucceed(ScienceTalkShow scienceTalkShow) {
//                view.receiveScienceTalkShow(scienceTalkShow);
//                view.hideRefreshProgress();
//            }
//
//            @Override
//            public void onFailed() {
//                view.hideRefreshProgress();
//                getScienceTalkShowFromRemote();
//            }
//        });
//    }
//
//    @Override
//    public void getScienceTalkShowFromRemote() {
//        model.getScienceTalkShowFromJsonRemote(new MainModelImpl.ScienceTalkShowCallback() {
//            @Override
//            public void onStart() {
//                view.showRefreshProgress();
//            }
//
//            @Override
//            public void onSucceed(ScienceTalkShow scienceTalkShow) {
//                view.receiveScienceTalkShow(scienceTalkShow);
//                view.hideRefreshProgress();
//            }
//
//            @Override
//            public void onFailed() {
//                view.hideRefreshProgress();
//            }
//        });
//    }

    @Override
    public void getProgrammeListFromLocalFirst() {
        model.getProgrammeListFromLocal(new MainModelImpl.ProgrammeListCallback() {
            @Override
            public void onStart() {
                view.showRefreshProgress();
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                if (programmes == null || programmes.size() <= 0) {
                    getProgrammeListFromRemote();
                } else {
                    pageIndex = 1;
                    programmeAllList = programmes;
                    view.receiveProgrammeList(PageUtil.paging(programmeAllList, pageIndex));
                    view.hideRefreshProgress();
                }
            }

            @Override
            public void onFailed() {
                getProgrammeListFromRemote();
            }
        });
    }

    @Override
    public void getProgrammeListFromRemote() {
        model.getProgrammeListFromJsonRemote(new MainModelImpl.ProgrammeListCallback() {
            @Override
            public void onStart() {
                view.showRefreshProgress();
            }

            @Override
            public void onSucceed(List<Programme> programmes) {
                pageIndex = 1;
                programmeAllList = programmes;
                view.receiveProgrammeList(PageUtil.paging(programmeAllList, pageIndex));
                view.hideRefreshProgress();
            }

            @Override
            public void onFailed() {
                view.hideRefreshProgress();
                getProgrammeListFromRemote();
            }
        });
    }

    @Override
    public void loadNextPage() {
        view.receiveProgrammeList(PageUtil.paging(programmeAllList, ++pageIndex));
    }
}
