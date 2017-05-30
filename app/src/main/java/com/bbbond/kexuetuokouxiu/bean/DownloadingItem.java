package com.bbbond.kexuetuokouxiu.bean;

/**
 * Created by bbbond on 2017/5/30.
 */

public class DownloadingItem {

    private String id;
    private String title;
    private String url;
    private String path;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "DownloadingItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
