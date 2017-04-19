package com.bbbond.kexuetuokouxiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * 节目
 * Created by Weya on 2016/11/10.
 */
public class Programme extends RealmObject implements Parcelable {

    private String id; // 节目唯一标示，url的md5
    private String title; // 节目标题
    private String link; // 节目链接
    private String pubDate; // 节目发布时间
    private String creator; // 节目创建者
    private String category; // 节目类别
    private String description; // 节目描述
    private String summary; // 节目说明
    private String commentRss; // 评论RSS
    private String comments; // 评论数
    private String duration; // 节目长度
    private String mediaUrl; // 节目音频链接
    private boolean listened = false;

    public Programme() {
    }

    protected Programme(Parcel in) {
        id = in.readString();
        title = in.readString();
        link = in.readString();
        pubDate = in.readString();
        creator = in.readString();
        category = in.readString();
        description = in.readString();
        summary = in.readString();
        commentRss = in.readString();
        comments = in.readString();
        duration = in.readString();
        mediaUrl = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCommentRss() {
        return commentRss;
    }

    public void setCommentRss(String commentRss) {
        this.commentRss = commentRss;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean isListened() {
        return listened;
    }

    public void setListened(boolean listened) {
        this.listened = listened;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Programme programme = (Programme) o;

        return id != null ? id.equals(programme.id) : programme.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", creator='" + creator + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", commentRss='" + commentRss + '\'' +
                ", comments='" + comments + '\'' +
                ", duration='" + duration + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", listened=" + listened +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(link);
        parcel.writeString(pubDate);
        parcel.writeString(creator);
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeString(summary);
        parcel.writeString(commentRss);
        parcel.writeString(comments);
        parcel.writeString(duration);
        parcel.writeString(mediaUrl);
        parcel.writeByte((byte) (listened ? 1 : 0));
    }

}
