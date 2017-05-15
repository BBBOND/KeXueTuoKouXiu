package com.bbbond.kexuetuokouxiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 缓存的节目
 * Created by bbbond on 2017/5/15.
 */

public class ProgrammeCache extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id;
    private String path;
    private String title;
    private String creator;
    private String category;

    public ProgrammeCache() {
    }

    protected ProgrammeCache(Parcel in) {
        id = in.readString();
        path = in.readString();
        title = in.readString();
        creator = in.readString();
        category = in.readString();
    }

    public static final Creator<ProgrammeCache> CREATOR = new Creator<ProgrammeCache>() {
        @Override
        public ProgrammeCache createFromParcel(Parcel in) {
            return new ProgrammeCache(in);
        }

        @Override
        public ProgrammeCache[] newArray(int size) {
            return new ProgrammeCache[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgrammeCache that = (ProgrammeCache) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(creator);
        dest.writeString(category);
    }
}
