package com.cll.wallpaper.toy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.cll.wallpaper.toy.utils.WindowUtils;

/**
 * Created by cll on 2018/3/19.
 */

public class FloatWindowService extends Service {

    private static final String TAG = "FloatWindowService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private WindowUtils.Builders mBuilder;
    private NotificationManager mManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBuilder = SmallWindowManager.SINGLETON.open(getApplication());
        showNotifyication(App.getContext(), intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotifyication(Context context, Intent service){
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent activity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activity, 0);
        Notification notification1 = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("yi")
                .setContentTitle("er")
                .setContentText("san")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();
        mManager.notify(1, notification1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBuilder != null){
            SmallWindowManager.SINGLETON.shut(mBuilder);
        }
        if (mManager != null){
            mManager.cancel(1);
        }
    }
}
