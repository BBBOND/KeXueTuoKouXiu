package com.kim.kexuetuokouxiu.app.model;

import android.support.v4.media.MediaMetadataCompat;

import com.kim.kexuetuokouxiu.bean.Programme;
import com.kim.kexuetuokouxiu.db.ScienceTalkShowDao;
import com.kim.kexuetuokouxiu.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.RealmList;

/**
 * Created by Weya on 2017/1/8.
 */

public class ProgrammeSource implements ProgrammeProviderSource {

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        RealmList<Programme> programmes = ScienceTalkShowDao.getScienceTalkShow().getProgrammes();
        ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
        if (programmes != null) {
            for (Programme programme : programmes) {
                tracks.add(buildMediaMetadata(programme));
            }
        }
        return tracks.iterator();
    }

    private MediaMetadataCompat buildMediaMetadata(Programme programme) {
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, programme.getId())
                .putString(CUSTOM_METADATA_TRACK_SOURCE_URL, programme.getEnclosureUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, programme.getCategory())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, TimeUtil.time2Long(programme.getDuration()))
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "幽默科学")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, programme.getTitle())
                .build();
    }
}
