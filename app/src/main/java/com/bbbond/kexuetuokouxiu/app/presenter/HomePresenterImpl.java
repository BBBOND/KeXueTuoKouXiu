package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.activity.Main1Activity;
import com.bbbond.kexuetuokouxiu.app.contract.HomeContract;
import com.bbbond.kexuetuokouxiu.app.model.HomeModelImpl;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.PageUtil;

import java.util.List;

/**
 * Created by Weya on 2016/11/10.
 */

public class HomePresenterImpl implements HomeContract.Presenter {

    private HomeContract.View view;
    private HomeContract.Model model;

    private List<Programme> programmeAllList;
    private int pageIndex = 1;

    public HomePresenterImpl(HomeContract.View view) {
        this.view = view;
        model = new HomeModelImpl();
        view.setPresenter(this);
    }

//    @Override
//    public void getScienceTalkShowFromLocalFirst() {
//        model.getScienceTalkShowFromLocal(new HomeModelImpl.ScienceTalkShowCallback() {
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
//        model.getScienceTalkShowFromJsonRemote(new HomeModelImpl.ScienceTalkShowCallback() {
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
        model.getProgrammeListFromLocal(new HomeModelImpl.ProgrammeListCallback() {
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
        model.getProgrammeListFromJsonRemote(new HomeModelImpl.ProgrammeListCallback() {
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

    public void getProgrammeListByCategories(String ...category) {

    }

    @Override
    public void loadNextPage() {
        if (PageUtil.hasNext(programmeAllList, pageIndex))
            view.receiveProgrammeList(PageUtil.paging(programmeAllList, ++pageIndex));
        else
            view.receiveProgrammeList(programmeAllList);
    }
}
