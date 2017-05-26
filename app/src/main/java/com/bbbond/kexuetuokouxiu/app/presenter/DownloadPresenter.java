package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.contract.DownloadContract;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.model.DownloadModel;
import com.bbbond.kexuetuokouxiu.bean.DownloadItem;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public class DownloadPresenter implements DownloadContract.Presenter {

    private DownloadContract.View view;
    private DownloadContract.Model model;

    public DownloadPresenter(DownloadContract.View view) {
        this.view = view;
        this.model = new DownloadModel();
    }

    @Override
    public void showContent(List<String> itemTitles) {
        List<DownloadItem> downloadItems = new ArrayList<>();
        int length = itemTitles.size() - 1;
        for (int i = 0; i < length; i++) {
            DownloadItem downloadItem = new DownloadItem();
            Long size = ProgrammeCacheDao.getInstance().sizeOfCategories(HomeFragment.categories[i]);
            Long cachedSize = ProgrammeCacheDao.getInstance().sizeOfCategories(HomeFragment.categories[i], true);
            downloadItem.setSize(size);
            downloadItem.setCachedSize(cachedSize);
            downloadItem.setTitle(itemTitles.get(i));
            downloadItem.setTitleNum(String.valueOf(size));
            downloadItems.add(downloadItem);
        }
        view.receiveDownloadItemList(downloadItems);
    }
}
