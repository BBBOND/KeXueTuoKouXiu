package com.kim.kexuetuokouxiu.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.kim.kexuetuokouxiu.app.model.ProgrammeProvider;
import com.kim.kexuetuokouxiu.app.palyback.PlaybackManager;
import com.kim.kexuetuokouxiu.helper.LogHelper;
import com.kim.kexuetuokouxiu.utils.LogUtil;

import java.util.List;

/**
 * Created by Weya on 2016/11/13.
 */

public class PlayService extends MediaBrowserServiceCompat implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener {

    private ProgrammeProvider mpProgrammeProvider;
    private PlaybackManager mPlaybackManager;
    private MediaSessionCompat mSession;


    // 连接的设备名
    public static final String EXTRA_CONNECTED_CAST = "com.kim.kexuetuokouxiu.CAST_NAME";
    // 接收的Intent的命令
    public static final String ACTION_CMD = "com.kim.kexuetuokouxiu.ACTION_CMD";
    // 传来的命令的KEY
    public static final String CMD_NAME = "CMD_NAME";
    // 接收到此值音乐将会被暂停
    public static final String CMD_PAUSE = "CMD_PAUSE";
    // 音乐播放应该切换到铸造播放本地播放
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";
    // stopSelf的延迟
    private static final int STOP_DELAY = 30000;

    public static final String PLAYSERVICE_TAG = "com.kim.kexuetuokouxiu.PlayService";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mSession = new MediaSessionCompat(this, PLAYSERVICE_TAG);
        setSessionToken(mSession.getSessionToken());
        mSession.setCallback();
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
//            mediaPlayer.setDataSource(programme.getEnclosureUrl());
//            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.release();
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

}
