package com.kim.kexuetuokouxiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Weya on 2016/11/10.
 */

public class Programme implements Parcelable {
    private String title;
    private String link;
    private String commentsUrl;
    private String pubDate;
    private String dcCreator;
    private String category;
    private String description;
    private String contentEncoded;
    private String wfwCommentRss;
    private String slashComments;
    private String enclosureUrl;
    private String duration;
    private boolean listened = false;

    public Programme() {
    }

    protected Programme(Parcel in) {
        title = in.readString();
        link = in.readString();
        commentsUrl = in.readString();
        pubDate = in.readString();
        dcCreator = in.readString();
        category = in.readString();
        description = in.readString();
        contentEncoded = in.readString();
        wfwCommentRss = in.readString();
        slashComments = in.readString();
        enclosureUrl = in.readString();
        duration = in.readString();
        listened = in.readByte() != 0;
    }

    public static final Creator<Programme> CREATOR = new Creator<Programme>() {
        @Override
        public Programme createFromParcel(Parcel in) {
            return new Programme(in);
        }

        @Override
        public Programme[] newArray(int size) {
            return new Programme[size];
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

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDcCreator() {
        return dcCreator;
    }

    public void setDcCreator(String dcCreator) {
        this.dcCreator = dcCreator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentEncoded() {
        return contentEncoded;
    }

    public void setContentEncoded(String contentEncoded) {
        this.contentEncoded = contentEncoded;
    }

    public String getWfwCommentRss() {
        return wfwCommentRss;
    }

    public void setWfwCommentRss(String wfwCommentRss) {
        this.wfwCommentRss = wfwCommentRss;
    }

    public String getSlashComments() {
        return slashComments;
    }

    public void setSlashComments(String slashComments) {
        this.slashComments = slashComments;
    }

    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isListened() {
        return listened;
    }

    public void setListened(boolean listened) {
        this.listened = listened;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", commentsUrl='" + commentsUrl + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", dcCreator='" + dcCreator + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", contentEncoded='" + contentEncoded + '\'' +
                ", wfwCommentRss='" + wfwCommentRss + '\'' +
                ", slashComments='" + slashComments + '\'' +
                ", enclosureUrl='" + enclosureUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", listened=" + listened +
                "}\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(link);
        parcel.writeString(commentsUrl);
        parcel.writeString(pubDate);
        parcel.writeString(dcCreator);
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeString(contentEncoded);
        parcel.writeString(wfwCommentRss);
        parcel.writeString(slashComments);
        parcel.writeString(enclosureUrl);
        parcel.writeString(duration);
        parcel.writeByte((byte) (listened ? 1 : 0));
    }
}
