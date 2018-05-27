package com.cll.wallpaper.toy.bean;

/**
 * Created by cll on 2018/5/12.
 */

public class Video {
    private String thumbnailPath;
    private String path;

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getPath() {
        return path;
    }

    public Video(String path, String thumbnailPath){
        this.path = path;
        this.thumbnailPath = thumbnailPath;
    }
}
