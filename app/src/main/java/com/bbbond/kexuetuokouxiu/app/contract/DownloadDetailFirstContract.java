package com.bbbond.kexuetuokouxiu.app.contract;

import android.content.Context;

import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by bbbond on 2017/5/26.
 */

public interface DownloadDetailFirstContract {
    interface Model {
        Observable<List<ProgrammeCache>> getCachedListByCategories(String[] categories);

        Observable<Programme> getObservableById(String id);

        Observable<Void> deleteByUrl(String url);
    }

    interface View {
        void receiveDownloadDetailList(List<ProgrammeCache> programmeCaches);

        void showToast(String msg);

        void showNoData();

        void goToPlayingActivity(Programme programme);

        void refresh();
    }

    interface Presenter {
        void showCachedProgramme(int pos);

        void play(String id);

        void deleteDownloadCacheItem(Context context, String url, boolean deleteFile);
    }
}
