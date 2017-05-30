package com.bbbond.kexuetuokouxiu.app.presenter;

import android.content.Context;
import android.content.Intent;

import com.bbbond.kexuetuokouxiu.app.contract.DownloadDetailSecondContract;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.model.DownloadDetailSecondModel;
import com.bbbond.kexuetuokouxiu.app.service.DownloadService;
import com.bbbond.kexuetuokouxiu.bean.DownloadingItem;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

/**
 * Created by bbbond on 2017/5/30.
 */

public class DownloadDetailSecondPresenter implements DownloadDetailSecondContract.Presenter {

    private DownloadDetailSecondContract.View view;
    private DownloadDetailSecondContract.Model model;

    public DownloadDetailSecondPresenter(DownloadDetailSecondContract.View view) {
        this.view = view;
        this.model = new DownloadDetailSecondModel();
    }

    @Override
    public void showDownloadingProgramme(Context context, int pos) {
        final String[] category = HomeFragment.categories[pos];
        RxDownload
                .getInstance(context)
                .getTotalDownloadRecords()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<DownloadRecord>, List<DownloadingItem>>() {
                    @Override
                    public List<DownloadingItem> apply(List<DownloadRecord> downloadRecords) throws Exception {
                        List<String> stringList = new ArrayList<>();
                        for (int i = 0, length = downloadRecords.size(); i < length; i++) {
                            stringList.add(downloadRecords.get(i).getUrl());
                        }
                        String[] urls = stringList.toArray(new String[]{});
                        List<ProgrammeCache> programmeCaches = ProgrammeCacheDao.getInstance().getProgrammeCacheListByCategoriesAndUrl(category, urls);
                        List<DownloadingItem> downloadingItems = new ArrayList<>();
                        for (ProgrammeCache cache : programmeCaches) {
                            DownloadingItem item = new DownloadingItem();
                            item.setId(cache.getId());
                            item.setPath(cache.getPath());
                            item.setTitle(cache.getTitle());
                            item.setUrl(cache.getUrl());
                            downloadingItems.add(item);
                        }
                        return downloadingItems;
                    }
                })
                .subscribe(new Consumer<List<DownloadingItem>>() {
                    @Override
                    public void accept(List<DownloadingItem> downloadingItems) throws Exception {
                        view.receiveDownloadingItemList(downloadingItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(DownloadDetailSecondPresenter.class, "accept", throwable.getMessage());
                    }
                });
    }

    @Override
    public void deleteDownloadingItem(Context context, String url, boolean deleteFile) {
        RxDownload
                .getInstance(context)
                .deleteServiceDownload(url, deleteFile)
                .subscribe();
        ProgrammeCacheDao
                .getInstance()
                .rxDeleteByUrl(url)
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

    @Override
    public void pauseDownloadingItem(Context context, String url) {
        RxDownload
                .getInstance(context)
                .pauseServiceDownload(url)
                .subscribe();
        context.getApplicationContext().sendBroadcast(new Intent(DownloadService.ACTION_PAUSE));
    }

    @Override
    public void startDownloadingItem(final Context context, String url) {
        ProgrammeCacheDao
                .getInstance()
                .rxGetCacheByUrl(url)
                .subscribe(new Consumer<ProgrammeCache>() {
                    @Override
                    public void accept(ProgrammeCache cache) throws Exception {
                        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
                        intent.putExtra(DownloadService.PROGRAMME, cache);
                        context.getApplicationContext().startService(intent);
                        context.getApplicationContext().sendBroadcast(new Intent(DownloadService.ACTION_RESUME));
                    }
                });
    }

    @Override
    public void deleteAll(List<DownloadingItem> downloadingItems, Context context, boolean deleteFile) {
        for (DownloadingItem item : downloadingItems) {
            RxDownload
                    .getInstance(context)
                    .deleteServiceDownload(item.getUrl(), deleteFile)
                    .subscribe();
            ProgrammeCacheDao
                    .getInstance()
                    .rxDeleteByUrl(item.getUrl())
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
}
