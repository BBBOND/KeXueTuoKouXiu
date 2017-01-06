package com.kim.kexuetuokouxiu.bean;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Weya on 2016/11/10.
 */

public class ScienceTalkShow extends RealmObject implements Serializable {

    @PrimaryKey
    private String title;
    private String link;
    private String description;
    private String lastBuildDate;
    private String language;
    private String itunes_summary;
    private String itunes_author;
    private String image;
    private String copyright;
    private String itunes_subtitle;

    private RealmList<Programme> programmes = new RealmList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getItunes_summary() {
        return itunes_summary;
    }

    public void setItunes_summary(String itunes_summary) {
        this.itunes_summary = itunes_summary;
    }

    public String getItunes_author() {
        return itunes_author;
    }

    public void setItunes_author(String itunes_author) {
        this.itunes_author = itunes_author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getItunes_subtitle() {
        return itunes_subtitle;
    }

    public void setItunes_subtitle(String itunes_subtitle) {
        this.itunes_subtitle = itunes_subtitle;
    }

    public RealmList<Programme> getProgrammes() {
        return programmes;
    }

    public void addProgramme(Programme programme) {
        if (programmes == null)
            programmes = new RealmList<>();
        programmes.add(programme);
    }

    @Override
    public String toString() {
        return "ScienceTalkShow{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", lastBuildDate='" + lastBuildDate + '\'' +
                ", language='" + language + '\'' +
                ", itunes_summary='" + itunes_summary + '\'' +
                ", itunes_author='" + itunes_author + '\'' +
                ", image='" + image + '\'' +
                ", copyright='" + copyright + '\'' +
                ", itunes_subtitle='" + itunes_subtitle + '\'' +
                ", programmes=" + programmes +
                '}';
    }
}
