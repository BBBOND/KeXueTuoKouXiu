package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.app.model.DetailModelImpl;
import com.bbbond.kexuetuokouxiu.bean.Comment;

import java.util.List;

/**
 * Created by Weya on 2016/12/4.
 */

public class DetailContract {

    public interface View {
        void changeProgress(int progress);

        void secondaryProgressChanged(int progress);

        void onCompletion();

        void onPlaying();

        void onPaused();

        void showProgress();

        void hideProgress();

        void showComments(List<Comment> comments);

        void showNoComment();
    }

    public interface Presenter {
        void showComments(String link);

        void playFirst(String url);

        void play();

        void pause();

        void stop();

        int getDuration();

        void startChangeProgress();

        void changeProgress(int progress);

        void endChangeProgress();

        boolean isPlaying();

        boolean isPlayed();
    }

    public interface Model {
        void getComments(String link, DetailModelImpl.Callback callback);
    }
}