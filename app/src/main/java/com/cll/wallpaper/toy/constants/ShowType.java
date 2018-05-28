package com.cll.wallpaper.toy.constants;

/**
 * Created by cll on 2018/5/3.
 */

public enum ShowType {
    IMAGE(0, "图片"),VIDEO(1, "视频"),GIF(2, "动态图");


    private String mValue;
    private int mPosition;
    ShowType(final int position, final String value){
        mPosition = position;
        mValue = value;
    }

    public String getValue(){
        return mValue;
    }

    public int getPosition(){
        return mPosition;
    }
}
