package com.kim.kexuetuokouxiu.app.model;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

public interface ProgrammeProviderSource {
    String CUSTOM_METADATA_TRACK_SOURCE_URL = "PROGRAMME_URL";

    Iterator<MediaMetadataCompat> iterator();
}