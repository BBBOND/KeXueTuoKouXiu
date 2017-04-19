package com.bbbond.kexuetuokouxiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Weya on 2016/12/4.
 */

public class Comment implements Parcelable {

    private String title;
    private String link;
    private String pubDate;
    private String creator;
    private String description;
    private String content;

    public Comment() {
    }

    protected Comment(Parcel in) {
        title = in.readString();
        link = in.readString();
        pubDate = in.readString();
        creator = in.readString();
        description = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(creator);
        dest.writeString(description);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return creator + "\n" +
                pubDate + "\n" +
                description;
    }
}
