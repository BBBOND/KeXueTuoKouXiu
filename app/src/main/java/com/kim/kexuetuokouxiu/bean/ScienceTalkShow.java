package com.kim.kexuetuokouxiu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 科学脱口秀
 * Created by bbbond on 2017/4/16.
 */

public class ScienceTalkShow extends RealmObject implements Serializable {

    @PrimaryKey
    private String title; // 节目名
    private String subtitle; // 节目副标题
    private String link; // 节目主页
    private String image; // 节目图
    private String language; // 节目语言
    private String description; // 节目描述
    private String summary; // 节目说明
    private String authorName; // 作者名
    private String authorEmail; // 作者邮箱
    private String copyright; // 版权
    private String lastBuildDate; // 上次更新时间

    private RealmList<Programme> programmes = new RealmList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public RealmList<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(RealmList<Programme> programmes) {
        this.programmes = programmes;
    }

}