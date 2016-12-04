package com.kim.kexuetuokouxiu.app.presenter;

import android.os.Handler;
import android.os.Message;

import com.kim.kexuetuokouxiu.app.activity.DetailActivity;
import com.kim.kexuetuokouxiu.app.contract.DetailContract;
import com.kim.kexuetuokouxiu.app.model.DetailModelImpl;
import com.kim.kexuetuokouxiu.bean.Comment;
import com.kim.kexuetuokouxiu.utils.Player;

import java.util.List;

/**
 * Created by Weya on 2016/11/15.
 */

public class DetailPresenterImpl implements DetailContract.Presenter {

    private DetailContract.View view;
    private Player player;
    private DetailContract.Model model;

    private boolean isPlaying = false;
    private boolean isPlayed = false;

    private Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            view.onPlaying();
            return true;
        }
    });

    public DetailPresenterImpl(DetailActivity activity) {
        this.view = activity;
        player = new Player();
        model = new DetailModelImpl();
    }

    @Override
    public void showComments(String link) {
        if (link == null || link.equals("")) {
            view.showNoComment();
            return;
        }
        model.getComments(link, new DetailModelImpl.Callback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSucceed(List<Comment> comments) {
                if (comments == null || comments.size() <= 0)
                    view.showNoComment();
                else
                    view.showComments(comments);
            }

            @Override
            public void onFailed() {
                view.showNoComment();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void playFirst(final String url) {
        view.showProgress();
        player.setListener(new Player.OnProgressChangeListener() {
            @Override
            public void progressChanged(int progress) {
                view.changeProgress(progress);
            }

            @Override
            public void secondaryProgressChanged(int progress) {
                view.secondaryProgressChanged(progress);
            }

            @Override
            public void completion() {
                view.onCompletion();
                isPlaying = false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                player.playUrl(url);
                isPlayed = true;
                progressHandler.sendEmptyMessage(1);
                view.hideProgress();
            }
        }).start();
        isPlaying = true;
    }

    @Override
    public void play() {
        player.play();
        isPlaying = true;
        view.onPlaying();
    }

    @Override
    public void pause() {
        player.pause();
        isPlaying = false;
        view.onPaused();
    }

    @Override
    public void stop() {
        player.stop();
        isPlaying = false;
        view.onPaused();
    }

    @Override
    public int getDuration() {
        return player.mediaPlayer.getDuration();
    }

    @Override
    public void startChangeProgress() {
        player.setPressed(true);
    }

    @Override
    public void changeProgress(int progress) {
        player.mediaPlayer.seekTo(progress);
    }

    @Override
    public void endChangeProgress() {
        player.setPressed(false);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public boolean isPlayed() {
        return isPlayed;
    }
}
