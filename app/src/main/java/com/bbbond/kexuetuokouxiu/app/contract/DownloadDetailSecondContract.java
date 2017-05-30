package com.bbbond.kexuetuokouxiu.app.contract;

import android.content.Context;

import com.bbbond.kexuetuokouxiu.bean.DownloadingItem;

import java.util.List;

/**
 * Created by bbbond on 2017/5/30.
 */

public interface DownloadDetailSecondContract {
    interface Model {
    }

    interface View {
        void receiveDownloadingItemList(List<DownloadingItem> downloadingItems);

        void showNoData();

        void refresh();
    }

    interface Presenter {
        void showDownloadingProgramme(Context context, int pos);

        void deleteDownloadingItem(Context context, String url, boolean deleteFile);

        void pauseDownloadingItem(Context context, String url);

        void startDownloadingItem(Context context, String url);

        void deleteAll(List<DownloadingItem> downloadingItems, Context context, boolean deleteFile);
    }
}
