package com.bbbond.kexuetuokouxiu.bean;

import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;

public class ProgrammeMetadata {

    public MediaMetadataCompat metadata;
    public final String trackId;

    public ProgrammeMetadata(String trackId, MediaMetadataCompat metadata) {
        this.metadata = metadata;
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != ProgrammeMetadata.class) {
            return false;
        }

        ProgrammeMetadata that = (ProgrammeMetadata) o;

        return TextUtils.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return trackId.hashCode();
    }
}