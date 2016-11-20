package com.kim.kexuetuokouxiu.app.presenter;

import com.kim.kexuetuokouxiu.app.activity.DetailActivity;
import com.kim.kexuetuokouxiu.utils.Player;

/**
 * Created by Weya on 2016/11/15.
 */

public class DetailPresenter {

    private DetailActivity activity;
    private Player player;

    private boolean isPlaying = false;
    private boolean isPlayed = false;

    public DetailPresenter(DetailActivity activity) {
        this.activity = activity;
        player = new Player();
    }

    public void playFirst(final String url) {
        activity.showProgress();
        player.setListener(new Player.OnProgressChangeListener() {
            @Override
            public void progressChanged(int progress) {
                activity.changeProgress(progress);
            }

            @Override
            public void secondaryProgressChanged(int progress) {
                activity.secondaryProgressChanged(progress);
            }

            @Override
            public void completion() {
                activity.onCompletion();
                isPlaying = false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                player.playUrl(url);
                isPlayed = true;
                activity.hideProgress();
            }
        }).start();
        isPlaying = true;
        activity.onPlaying();
    }

    public void play() {
        player.play();
        isPlaying = true;
        activity.onPlaying();
    }

    public void pause() {
        player.pause();
        isPlaying = false;
        activity.onPaused();
    }

    public void stop() {
        player.stop();
        isPlaying = false;
        activity.onPaused();
    }

    public int getDuration() {
        return player.mediaPlayer.getDuration();
    }

    public void startChangeProgress() {
        player.setPressed(true);
    }

    public void changeProgress(int progress) {
        player.mediaPlayer.seekTo(progress);
    }

    public void endChangeProgress() {
        player.setPressed(false);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

}
