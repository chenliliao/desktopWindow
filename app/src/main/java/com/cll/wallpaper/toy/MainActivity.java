package com.cll.wallpaper.toy;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cll.wallpaper.toy.constants.Constants;
import com.cll.wallpaper.toy.constants.ShowType;
import com.cll.wallpaper.toy.utils.Utils;

import java.text.DecimalFormat;

import static android.provider.Settings.canDrawOverlays;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "[WallPaper]MainActivity";
    private boolean isOpen = false;
    private Button mButtonSetting ,mButtonImage, mButtonVideo, mButtonGif;
    private TextView mCurrentMode;
    private int REQUEST_CODE_IMAGE = 1;
    private int REQUEST_CODE_VIDEO = 2;
    private static String imagePath = null;
    private static String videoPath = null;
    private  SharedPreferences mShare;
    private SeekBar mSeekBar;
    private RadioButton mRadioButton;
    private RadioGroup mRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShare = this.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
//        mShare.edit().putInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()).commit();
//        mShare.edit().putString(Constants.SHARE_ALPHA, "0.4").commit();
//        mShare.edit().putBoolean(Constants.SHARE_AUDIO, true).commit();
//        mShare.edit().putBoolean(Constants.SHARE_IS_OPEN, false).commit();
//        mShare.edit().putInt(Constants.SHARE_PROGRESS, 90).commit();
        initWidgets();

    }

    private void initWidgets(){
        mButtonSetting = findViewById(R.id.button_setting);
        mButtonImage = findViewById(R.id.button_image);
        mButtonVideo = findViewById(R.id.button_video);
        mButtonGif = findViewById(R.id.button_gif);
        mSeekBar = findViewById(R.id.seek_bar);
        mRadioButton = findViewById(R.id.radio_button2);
        mRadioGroup = findViewById(R.id.radio_group);
        mCurrentMode = findViewById(R.id.current_text_view);

        refreshMode();
        Toast.makeText(this, "pro"+mShare.getInt(Constants.SHARE_PROGRESS, 90), Toast.LENGTH_SHORT).show();
        mSeekBar.setProgress(mShare.getInt(Constants.SHARE_PROGRESS, 90));
        mSeekBar.setKeyProgressIncrement(10);
        mSeekBar.setMax(90);

        initListener();
    }

    private void initListener(){
        mRadioButton.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button1){
                    mShare.edit().putBoolean(Constants.SHARE_AUDIO, false).commit();
                }else if (checkedId == R.id.radio_button2){
                    mShare.edit().putBoolean(Constants.SHARE_AUDIO, true).commit();
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShare.edit().putString(Constants.SHARE_ALPHA, (new DecimalFormat("#.0").format((float)progress / 100)) + "").commit();
                mShare.edit().putInt(Constants.SHARE_PROGRESS, progress).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShare.getBoolean(Constants.SHARE_IS_OPEN, true)){
                    Log.w(TAG,"app stop service");
                    mShare.edit().putBoolean(Constants.SHARE_IS_OPEN, false).commit();
                    SmallWindowManager.SINGLETON.stopService(MainActivity.this);
                }else{
                    mShare.edit().putBoolean(Constants.SHARE_IS_OPEN, true).commit();
                    Log.w(TAG,"app start service");
                    refreshMode();
                    SmallWindowManager.SINGLETON.startService(MainActivity.this);
                }
            }
        });
        mButtonSetting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Utils.isServiceRunning(MainActivity.this, "com.cll.wallpaper.toy.FloatWindowService")){
                    SmallWindowManager.SINGLETON.stopService(MainActivity.this);
                }
                return true;
            }
        });

        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.edit().putInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()).commit();
                startActivityForResult(new Intent(MainActivity.this, ImageSelectorActivity.class), REQUEST_CODE_IMAGE);
            }
        });

        mButtonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.edit().putInt(Constants.SHARE_MODE, ShowType.VIDEO.ordinal()).commit();
                startActivityForResult(new Intent(MainActivity.this, ImageSelectorActivity.class), REQUEST_CODE_VIDEO);
            }
        });

        mButtonGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.edit().putInt(Constants.SHARE_MODE, ShowType.GIF.ordinal()).commit();
            }
        });
    }


    private void refreshMode(){
        if (mCurrentMode != null && mShare != null){
            int mode = mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal());
            if (mode == ShowType.IMAGE.ordinal()){
                mCurrentMode.setText(ShowType.IMAGE.getValue());
            }else if (mode == ShowType.VIDEO.ordinal()){
                mCurrentMode.setText(ShowType.VIDEO.getValue());
            }else if (mode == ShowType.GIF.ordinal()){
                mCurrentMode.setText(ShowType.GIF.getValue());
            }
        }
    }

    private boolean checkPemission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mButtonSetting.setClickable(true);
            return true;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (!Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
            mButtonSetting.setClickable(false);
            return false;
        }else{
            mButtonSetting.setClickable(true);
            return true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refreshMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPemission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                imagePath = data.getStringExtra(Constants.CONSTANT_SELETOR_RESULT);
                if (!TextUtils.isEmpty(imagePath)){
                    if (mShare.getBoolean(Constants.SHARE_IS_OPEN, false)){
                        SmallWindowManager.SINGLETON.stopService(MainActivity.this);
                        SmallWindowManager.SINGLETON.startService(MainActivity.this);
                    }
                }
                refreshMode();
                Toast.makeText(this, "就决定是你了", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_CODE_VIDEO){
            if (resultCode == Activity.RESULT_OK){
                videoPath = data.getStringExtra(Constants.CONSTANT_SELETOR_RESULT);
                if (!TextUtils.isEmpty(videoPath)){
                    if (mShare.getBoolean(Constants.SHARE_IS_OPEN, false)){
                        SmallWindowManager.SINGLETON.stopService(MainActivity.this);
                        SmallWindowManager.SINGLETON.startService(MainActivity.this);
                    }
                }
                refreshMode();
                Toast.makeText(this, "就决定是你了"+videoPath, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static String getPath(){
        return imagePath;
    }

    public static String getVideoPath(){
        return videoPath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
