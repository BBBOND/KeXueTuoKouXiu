package com.bbbond.kexuetuokouxiu.app.contract;

import android.view.View;

/**
 * Created by bbbond on 2017/5/15.
 */

public interface DownloadContract {
    interface Model {
    }

    interface View {
        void addView(String title, long num, boolean complete, android.view.View.OnClickListener listener, String... a);

        void toDetail(String title, int pos);
    }

    interface Presenter {
        void initContent(String[] types);
    }
}
