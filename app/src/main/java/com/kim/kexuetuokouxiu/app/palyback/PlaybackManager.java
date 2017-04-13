package com.kim.kexuetuokouxiu.app.palyback;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.kim.kexuetuokouxiu.service.PlayService;
import com.kim.kexuetuokouxiu.utils.LogUtil;

/**
 * Created by Weya on 2017/1/7.
 */

public class PlaybackManager implements Playback.Callback {

    private static final String CUSTOM_ACTION_THUMBS_UP = "com.kim.kexuetuokouxiu.THUMBS_UP";
    private Playback mPlayback;
    private PlaybackServiceCallback mServiceCallback;
    private MediaSessionCallback mMediaSessionCallback;
    private QueueManager mQueueManager;

    public PlaybackManager(Playback mPlayback,
                           PlaybackServiceCallback mServiceCallback,
                           QueueManager queueManager) {
        this.mPlayback = mPlayback;
        this.mServiceCallback = mServiceCallback;
        this.mQueueManager = queueManager;
        this.mMediaSessionCallback = new MediaSessionCallback();
        this.mPlayback.setCallback(this);
    }

    public Playback getPlayback() {
        return mPlayback;
    }

    public MediaSessionCompat.Callback getMediaSessionCallback() {
        return mMediaSessionCallback;
    }

    public void handlePlayRequest() {
        LogUtil.d(PlaybackManager.class, "handlePlayRequest", "mState=" + mPlayback.getState());
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentProgramme();
        if (currentMusic != null) {
            mServiceCallback.onPlaybackStart();
            mPlayback.play(currentMusic);
        }
    }

    public void handlePauseRequest() {
        LogUtil.d(PlaybackManager.class, "handlePauseRequest", "mState=" + mPlayback.getState());
        if (mPlayback.isPlaying()) {
            mPlayback.pause();
            mServiceCallback.onPlaybackStop();
        }
    }

    /**
     * Handle a request to stop music
     *
     * @param withError Error message in case the stop has an unexpected cause. The error
     *                  message will be set in the PlaybackState and will be visible to
     *                  MediaController clients.
     */
    public void handleStopRequest(String withError) {
        LogUtil.d(PlaybackManager.class, "handleStopRequest", "mState=" + mPlayback.getState() + " error=" + withError);
        mPlayback.stop(true);
        mServiceCallback.onPlaybackStop();
        updatePlaybackState(withError);
    }

    /**
     * Update the current media player state, optionally showing an error message.
     *
     * @param error if not null, error message to present to the user.
     */
    public void updatePlaybackState(String error) {
        LogUtil.d(PlaybackManager.class, "updatePlaybackState", "playback state=" + mPlayback.getState());
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mPlayback != null && mPlayback.isConnected()) {
            position = mPlayback.getCurrentStreamPosition();
        }

        //noinspection ResourceType
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());

        int state = mPlayback.getState();

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error);
            state = PlaybackStateCompat.STATE_ERROR;
        }
        //noinspection ResourceType
        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());

        // Set the activeQueueItemId if the current index is valid.
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentProgramme();
        if (currentMusic != null) {
            stateBuilder.setActiveQueueItemId(currentMusic.getQueueId());
        }

        mServiceCallback.onPlaybackStateUpdated(stateBuilder.build());

        if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED) {
            mServiceCallback.onNotificationRequired();
        }
    }

    private long getAvailableActions() {
        long actions =
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (mPlayback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }

    @Override
    public void onCompletion() {
        // The media player finished playing the current song, so we go ahead
        // and start the next.
        if (mQueueManager.skipQueuePosition(1)) {
            handlePlayRequest();
            mQueueManager.updateMetadata();
        } else {
            // If skipping was not possible, we stop and release the resources:
            handleStopRequest(null);
        }
    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        updatePlaybackState(null);
    }

    @Override
    public void onError(String error) {
        updatePlaybackState(error);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onPlay", "play");
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
            LogUtil.d(PlayService.class, "MediaSessionCallback.OnSkipToQueueItem", queueId);
        }

        @Override
        public void onSeekTo(long position) {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onSeekTo", position);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onPlayFromMediaId", mediaId);
        }

        @Override
        public void onPause() {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onPause", "onPause");
        }

        @Override
        public void onStop() {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onStop", "onStop");
        }

        @Override
        public void onSkipToNext() {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onSkipToNext", "onSkipToNext");
        }

        @Override
        public void onSkipToPrevious() {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onSkipToPrevious", "onSkipToPrevious");
        }

        @Override
        public void onCustomAction(@NonNull String action, Bundle extras) {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onCustomAction", action);
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            LogUtil.d(PlayService.class, "MediaSessionCallback.onPlayFromSearch", query);
        }
    }

    public interface PlaybackServiceCallback {
        void onPlaybackStart();

        void onNotificationRequired();

        void onPlaybackStop();

        void onPlaybackStateUpdated(PlaybackStateCompat newState);
    }

}
