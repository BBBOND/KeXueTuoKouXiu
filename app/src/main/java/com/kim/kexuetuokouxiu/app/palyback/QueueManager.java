package com.kim.kexuetuokouxiu.app.palyback;

import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.kim.kexuetuokouxiu.app.model.ProgrammeProvider;
import com.kim.kexuetuokouxiu.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Weya on 2017/1/8.
 */

public class QueueManager {

    private ProgrammeProvider mProgrammeProvider;
    private MetadataUpdateListener mListener;

    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private int mCurrentIndex;

    public QueueManager(@NonNull MetadataUpdateListener mListener,
                        @NonNull ProgrammeProvider mProgrammeProvider) {
        this.mListener = mListener;
        this.mProgrammeProvider = mProgrammeProvider;

        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mCurrentIndex = 0;
    }

    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public MediaSessionCompat.QueueItem getCurrentProgramme() {
        if (!isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    private boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }

    public void updateMetadata() {
        MediaSessionCompat.QueueItem currentMusic = getCurrentProgramme();
        if (currentMusic == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        final String programmeId = currentMusic.getDescription().getMediaId();
        MediaMetadataCompat metadata = mProgrammeProvider.getProgramme(programmeId);
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid programmeId " + programmeId);
        }

        mListener.onMetadataChanged(metadata);
    }

    public boolean skipQueuePosition(int amount) {
        int index = mCurrentIndex + amount;
        if (index < 0) {
            // skip backwards before the first song will keep you on the first song
            index = 0;
        } else {
            // skip forwards when in last song will cycle back to start of the queue
            index %= mPlayingQueue.size();
        }
        if (!isIndexPlayable(index, mPlayingQueue)) {
            LogUtil.e(QueueManager.class, "skipQueuePosition", "Cannot increment queue index by " + amount +
                    ". Current=" + mCurrentIndex + " queue length=" + mPlayingQueue.size());
            return false;
        }
        mCurrentIndex = index;
        return true;
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);

        void onMetadataRetrieveError();

        void onCurrentQueueIndexUpdated(int queueIndex);

        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
