package com.kim.kexuetuokouxiu.app.model;

import android.os.AsyncTask;
import android.support.v4.media.MediaMetadataCompat;

import com.kim.kexuetuokouxiu.bean.ProgrammeMetadata;
import com.kim.kexuetuokouxiu.utils.LogUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Weya on 2017/1/8.
 */

public class ProgrammeProvider {

    ProgrammeProviderSource mSource;

    // Categorized caches for programme track data:
    private final ConcurrentMap<String, ProgrammeMetadata> mProgrammeListById;

    private final Set<String> mFavoriteTracks;

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    private volatile State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }

    public ProgrammeProvider() {
        this(new ProgrammeSource());
    }

    public ProgrammeProvider(ProgrammeProviderSource source) {
        this.mSource = source;
        mProgrammeListById = new ConcurrentHashMap<>();
        mFavoriteTracks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }

    public MediaMetadataCompat getProgramme(String programmeId) {
        return mProgrammeListById.containsKey(programmeId) ? mProgrammeListById.get(programmeId).metadata : null;
    }

    /**
     * 初始化节目列表
     *
     * @param callback
     */
    public void retrieveMediaAsync(final Callback callback) {
        LogUtil.d(ProgrammeProvider.class, "retrieveMediaAsync", "called");
        if (mCurrentState == State.INITIALIZED) {
            if (callback != null) {
                // Nothing to do, execute callback immediately
                callback.onMusicCatalogReady(true);
            }
            return;
        }

        // Asynchronously load the music catalog in a separate thread
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... params) {
                retrieveMedia();
                return mCurrentState;
            }

            @Override
            protected void onPostExecute(State current) {
                if (callback != null) {
                    callback.onMusicCatalogReady(current == State.INITIALIZED);
                }
            }
        }.execute();
    }

    private synchronized void retrieveMedia() {
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;

                Iterator<MediaMetadataCompat> tracks = mSource.iterator();
                while (tracks.hasNext()) {
                    MediaMetadataCompat item = tracks.next();
                    String programmeId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                    mProgrammeListById.put(programmeId, new ProgrammeMetadata(programmeId, item));
                }
                mCurrentState = State.INITIALIZED;
            }
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                // Something bad happened, so we reset state to NON_INITIALIZED to allow
                // retries (eg if the network connection is temporary unavailable)
                mCurrentState = State.NON_INITIALIZED;
            }
        }
    }

}
