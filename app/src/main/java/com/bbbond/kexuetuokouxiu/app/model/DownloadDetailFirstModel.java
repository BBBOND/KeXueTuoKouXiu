package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.DownloadDetailFirstContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by bbbond on 2017/5/26.
 */

public class DownloadDetailFirstModel implements DownloadDetailFirstContract.Model {

    @Override
    public Observable<List<ProgrammeCache>> getCachedListByCategories(String[] categories) {
        return ProgrammeCacheDao.getInstance().rxGetProgrammeCachedListByCategories(categories);
    }

    @Override
    public Observable<Programme> getObservableById(String id) {
        return ProgrammeDao.getInstance().rxGetProgrammeById(id);
    }

    @Override
    public Observable<Void> deleteByUrl(String url) {
        return ProgrammeCacheDao.getInstance().rxDeleteByUrl(url);
    }

}
