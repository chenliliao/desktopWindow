package com.cll.wallpaper.toy;

import android.app.Application;
import android.content.Context;


/**
 * Created by cll on 2018/5/3.
 */

public class App extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static Context getContext(){
        return mContext;
    }
}
