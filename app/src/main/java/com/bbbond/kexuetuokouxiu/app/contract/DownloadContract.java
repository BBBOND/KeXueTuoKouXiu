package com.bbbond.kexuetuokouxiu.app.contract;

import android.view.View;

import com.bbbond.kexuetuokouxiu.bean.DownloadItem;

import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public interface DownloadContract {
    interface Model {
    }

    interface View {
        void receiveDownloadItemList(List<DownloadItem> downloadItemList);
    }

    interface Presenter {
        void showContent(List<String> itemTitles);
    }
}
