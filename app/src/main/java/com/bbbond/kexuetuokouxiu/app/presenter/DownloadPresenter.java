package com.bbbond.kexuetuokouxiu.app.presenter;

import android.view.View;

import com.bbbond.kexuetuokouxiu.app.contract.DownloadContract;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.app.model.DownloadModel;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;

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
    public void initContent(final String[] types) {
        int length = types.length - 1;
        for (int i = 0; i < length; i++) {
            final String type = types[i];
            final int pos = i;
            Long size = ProgrammeCacheDao.sizeOfCategories(HomeFragment.categories[i]);
            view.addView(types[i], size, true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.toDetail(type, pos);
                }
            });
        }
    }
}
