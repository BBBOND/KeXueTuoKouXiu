package com.bbbond.kexuetuokouxiu.bean;

/**
 * Created by bbbond on 2017/5/26.
 */

public class DownloadItem {

    private String title;
    private String titleNum;
    private long size;
    private long cachedSize;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleNum() {
        return titleNum;
    }

    public void setTitleNum(String titleNum) {
        this.titleNum = titleNum;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCachedSize() {
        return cachedSize;
    }

    public void setCachedSize(long cachedSize) {
        this.cachedSize = cachedSize;
    }
}
