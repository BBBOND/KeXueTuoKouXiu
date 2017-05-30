package com.bbbond.kexuetuokouxiu.app.presenter;

import android.content.Context;

import com.bbbond.kexuetuokouxiu.app.contract.DownloadDetailFirstContract;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.model.DownloadDetailFirstModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;

/**
 * Created by bbbond on 2017/5/26.
 */

public class DownloadDetailFirstPresenter implements DownloadDetailFirstContract.Presenter {

    private DownloadDetailFirstContract.View view;
    private DownloadDetailFirstContract.Model model;

    public DownloadDetailFirstPresenter(DownloadDetailFirstContract.View view) {
        this.view = view;
        this.model = new DownloadDetailFirstModel();
    }

    @Override
    public void showCachedProgramme(int pos) {
        String[] category = HomeFragment.categories[pos];
        model.getCachedListByCategories(category).subscribe(new Observer<List<ProgrammeCache>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<ProgrammeCache> programmeCaches) {
                view.receiveDownloadDetailList(programmeCaches);
            }

            @Override
            public void onError(Throwable e) {
                view.showToast(e.getMessage());
                view.showNoData();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void play(String id) {
        model.getObservableById(id).subscribe(new Consumer<Programme>() {
            @Override
            public void accept(Programme programme) throws Exception {
                if (programme != null)
                    view.goToPlayingActivity(programme);
            }
        });
    }

    @Override
    public void deleteDownloadCacheItem(Context context, String url, boolean deleteFile) {
        RxDownload
                .getInstance(context)
                .deleteServiceDownload(url, deleteFile)
                .subscribe();
        model
                .deleteByUrl(url)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.refresh();
                    }
                });

    }
}
