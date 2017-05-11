package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.bean.Programme;

/**
 * Created by bbbond on 2017/5/11.
 */

public interface PlayingContract {
    interface Model {
        Programme getProgrammeById(String id);
    }

    interface View {
    }

    interface Presenter {
        Programme getProgrammeById(String id);
    }
}
