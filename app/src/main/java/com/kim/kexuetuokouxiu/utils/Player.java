package com.kim.kexuetuokouxiu.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Weya on 2016/11/15.
 */

public class Player implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public MediaPlayer mediaPlayer;
    //    private SeekBar seekBar;
    private Timer timer = new Timer();

    private boolean isPressed = false;
    private OnProgressChangeListener listener;

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public void setListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }

    public Player() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        timer.schedule(timerTask, 0, 1000);
    }

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && !isPressed) {
                handler.sendEmptyMessage(0); // 发送消息
            }
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if (duration > 0) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                long pos = 100 * position / duration;
                if (listener != null) {
                    listener.progressChanged((int) pos);
                }
            }
        }
    };

    public void play() {
        mediaPlayer.start();
    }

    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        handler = null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        if (listener != null) {
            listener.secondaryProgressChanged(percent);
        }
//        int currentProgress = seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        handler = null;
        if (listener != null) {
            listener.completion();
        }
        listener = null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public interface OnProgressChangeListener {
        void progressChanged(int progress);

        void secondaryProgressChanged(int progress);

        void completion();
    }
}
