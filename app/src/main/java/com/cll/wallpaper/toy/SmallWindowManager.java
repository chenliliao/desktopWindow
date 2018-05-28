package com.cll.wallpaper.toy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cll.wallpaper.toy.constants.Constants;
import com.cll.wallpaper.toy.constants.ShowType;
import com.cll.wallpaper.toy.utils.WindowUtils;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by cll on 2018/3/16.
 */

public enum SmallWindowManager {

    SINGLETON;

    public void startService(Context context){
        Intent service = new Intent(context, FloatWindowService.class);
        context.startService(service);
    }

    public void stopService(Context context){
        context.stopService(new Intent(context, FloatWindowService.class));
    }

    public WindowUtils.Builders open(Context context){
        if (!isOpen){
            return showSmallWindow(context);
        }
        return null;
    }

    public void shut(WindowUtils.Builders mBuilder){
        if (isOpen && mBuilder != null){
            remoreSmallWindow(mBuilder);
        }
    }

    private boolean isOpen;
    private SharedPreferences mShare;
    private  WindowUtils.Builders showSmallWindow(Context context){
        mShare = context.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
        final View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_layout, null);
        final ImageView imageView = view.findViewById(R.id.icon_image_view);
        final TextureView mTextureView = view.findViewById(R.id.surface_view);
        final GifImageView mGifView = view.findViewById(R.id.gifview);
        if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.IMAGE.ordinal()){
            mTextureView.setVisibility(View.GONE);
            mGifView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            final String path = MainActivity.getPath();
            if (!TextUtils.isEmpty(path)){
//                new BitmapDrawable(BitmapFactory.decodeFile(path));
                imageView.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(path)));
//                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }else if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.VIDEO.ordinal()){
            mTextureView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            mGifView.setVisibility(View.GONE);
        }else if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.GIF.ordinal()){
            mTextureView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            mGifView.setVisibility(View.VISIBLE);
//            mGifView.setGifResource(R.drawable.a);
//            mGifView.pause();
        }
        WindowUtils.Builders builders = new WindowUtils.Builders(context)
                .setFloatView(view)
                .create();
        builders.addView();
        if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.VIDEO.ordinal()){
            PlayingManager.SINGLETON.play(mTextureView);
        }else if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.GIF.ordinal()){
            mGifView.setImageResource(R.drawable.a);
        }
        isOpen = true;
        return builders;
    }

    private void remoreSmallWindow(WindowUtils.Builders mBuilder ){
        if (mBuilder != null){
            mBuilder.remoreWindow();
        }
        isOpen = false;
    }




    public boolean isOpen(){
        return isOpen;
    }
}
