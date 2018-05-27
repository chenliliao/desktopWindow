package com.cll.wallpaper.toy.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by cll on 2018/5/4.
 */

public class Utils {

    public static boolean isServiceRunning(Context context, String serviceName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> lists = am.getRunningServices(30);
        for (ActivityManager.RunningServiceInfo info : lists) {
            if(info.service.getClassName().equals(serviceName)){
                isRunning = true;
            }
        }


        return isRunning;
    }

    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : lists){
            Log.w("TAG","TEST info = " + info.processName);
            if(info.processName.equals(proessName)){
                isRunning = true;
            }
        }
        return isRunning;
    }
}
