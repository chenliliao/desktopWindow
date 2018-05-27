package com.cll.wallpaper.toy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cll.wallpaper.toy.constants.Constants;

/**
 * Created by cll on 2018/5/12.
 */

public class WindowUtils {

    public static class Builders {

        private Context mContext;
        private View mFloatView;
        private static WindowManager mWindowManager;
        private WindowManager.LayoutParams mParams;
        private SharedPreferences mShare;

        public Builders(Context context) {
            mContext = context;
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mShare = context.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
        }

        public Builders setFloatView(final View view) {
            mFloatView = view;
            return this;
        }

        public Builders create() {
            if (mParams != null) {
                mParams.width = mWindowManager.getDefaultDisplay().getWidth() ;
                mParams.height = mWindowManager.getDefaultDisplay().getHeight() + getNavigationBarHeight(mContext);
                mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                mParams.gravity = Gravity.LEFT | Gravity.TOP ;
                mParams.format = PixelFormat.TRANSLUCENT;
                mParams.alpha = Float.valueOf(mShare.getString(Constants.SHARE_ALPHA, "0.4"));
                Toast.makeText(mContext, "alpha = " + mParams.alpha, Toast.LENGTH_SHORT).show();
            }
            return this;
        }

        public void addView() {
            if (mFloatView != null && mParams != null) {
                mWindowManager.addView(mFloatView, mParams);
            }
        }

        public void remoreWindow() {
            if (mFloatView != null && mWindowManager != null) {
                mWindowManager.removeView(mFloatView);
            }
        }
    }


    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        Toast.makeText(context, "result = "+result, Toast.LENGTH_SHORT).show();
        return result;
    }
    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
