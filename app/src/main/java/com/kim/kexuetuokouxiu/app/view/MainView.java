package com.kim.kexuetuokouxiu.app.view;

import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

import java.util.List;

/**
 * Created by Weya on 2016/11/10.
 */

public interface MainView {

    void showRefreshProgress();

    void hideRefreshProgress();

    void receiveScienceTalkShow(ScienceTalkShow scienceTalkShow);
}
