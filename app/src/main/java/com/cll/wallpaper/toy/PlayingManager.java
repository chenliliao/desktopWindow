package com.cll.wallpaper.toy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.Toast;

import com.cll.wallpaper.toy.constants.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by cll on 2018/5/3.
 */

public enum PlayingManager{

    SINGLETON;

    private MediaPlayer mMediaPlayer;

    public void play(final TextureView mTextureView){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                mMediaPlayer.setLooping(true);
            }
        });
        mTextureView.setSurfaceTextureListener(mListener);
    }

    public void stop(){
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();;
            mMediaPlayer = null;
        }
    }

    private Surface mSurface;
    TextureView.SurfaceTextureListener mListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface1, int width, int height) {
            mSurface = new Surface(surface1);
            mMediaPlayer.reset();
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            SharedPreferences mShare = App.getContext().getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
            if (!mShare.getBoolean(Constants.SHARE_AUDIO, true)){
                mMediaPlayer.setVolume(0,0);
            }else{
//                AudioManager mAudioManager = (AudioManager) App.getContext().getSystemService(Context.AUDIO_SERVICE);
//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            }

            try {
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/i.mp4";
                if (new File(MainActivity.getVideoPath()).exists()){
                    Toast.makeText(App.getContext(), "MainActivity.getVideoPath() = "+MainActivity.getVideoPath(), Toast.LENGTH_SHORT).show();
                    mMediaPlayer.setDataSource(MainActivity.getVideoPath());
                }else{
                    mMediaPlayer.setDataSource(App.getContext().getResources().getAssets().openFd("i.mp4"));
                }
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mSurface = null;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
}
