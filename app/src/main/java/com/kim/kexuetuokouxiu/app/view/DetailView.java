package com.kim.kexuetuokouxiu.app.view;

/**
 * Created by Weya on 2016/11/15.
 */

public interface DetailView {

    void changeProgress(int progress);

    void secondaryProgressChanged(int progress);

    void onCompletion();

    void onPlaying();

    void onPaused();

    void showProgress();

    void hideProgress();
}
