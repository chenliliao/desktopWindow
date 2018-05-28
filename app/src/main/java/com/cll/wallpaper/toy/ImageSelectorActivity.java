package com.cll.wallpaper.toy;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.cll.wallpaper.toy.adapter.ImageFolderAdapter;
import com.cll.wallpaper.toy.adapter.ImageGridViewAdapter;
import com.cll.wallpaper.toy.adapter.VideoGridViewAdapter;
import com.cll.wallpaper.toy.bean.Folder;
import com.cll.wallpaper.toy.bean.Image;
import com.cll.wallpaper.toy.bean.Video;
import com.cll.wallpaper.toy.constants.Constants;
import com.cll.wallpaper.toy.constants.ShowType;
import com.cll.wallpaper.toy.utils.LoaderManagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cll on 2018/4/28.
 */

public class ImageSelectorActivity extends Activity {

    private Context mContext;
    private GridView mImageGridView;
    private List<Image> images = new ArrayList<>();
    private List<Video> videos = new ArrayList<>();
    private ArrayList<Folder> mResultFolder = new ArrayList<>();
    private ImageGridViewAdapter mAdapter;
    private Toolbar mSettingTypeToolbar;
    private Button mCategoryButton;
    private ListView mFolderListView;
    private ImageFolderAdapter folderAdapter;
    private SharedPreferences mShare;
    private int IDENTIFIER_IMAGE = 0;
    private int IDENTIFIER_VIDEO = 1;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_board_image_seletor_layout);
        mContext = this;
        initWidgets();
        initClickListener();
        initAllImageData();
    }

    private void initWidgets(){
        mImageGridView = (GridView) findViewById(R.id.grid);
        mSettingTypeToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSettingTypeToolbar.setTitle("选择图片");
        mCategoryButton = (Button) findViewById(R.id.category_btn);
        mCategoryButton.setText("all picture");
        mFolderListView = (ListView) findViewById(R.id.folder_listview) ;
    }

    private void initClickListener(){
        mImageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = null;
                if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.IMAGE.ordinal()){
                    Image image = (Image) parent.getAdapter().getItem(position);
                    path = image.path;
                }else if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.VIDEO.ordinal()){
                    Video video = (Video) parent.getAdapter().getItem(position);
                    path = video.getPath();
                }

                Log.w("TAG","test setOnItemClickListener"+path);
                Intent intent = new Intent();
                intent.putExtra(Constants.CONSTANT_SELETOR_RESULT, path);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderListView.setVisibility(View.VISIBLE);
                mImageGridView.setVisibility(View.GONE);
                folderAdapter = new ImageFolderAdapter(mResultFolder,ImageSelectorActivity.this);
                mFolderListView.setAdapter(folderAdapter);
            }
        });

        mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Image> newImage = folderAdapter.getItem(position).images;
                mAdapter = new ImageGridViewAdapter(ImageSelectorActivity.this,newImage);
                mImageGridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mCategoryButton.setText(folderAdapter.getItem(position).name);
                mFolderListView.setVisibility(View.GONE);
                mImageGridView.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initAllImageData(){
        mShare = this.getSharedPreferences(Constants.SHARE_NAME, Context.MODE_PRIVATE);
        if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.IMAGE.ordinal()){
            getLoaderManager().restartLoader(IDENTIFIER_IMAGE, null,mLoaderCallback);
        }else {
            getLoaderManager().restartLoader(IDENTIFIER_VIDEO, null,mLoaderCallback);
        }
    }


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            return LoaderManagerUtils.createCursorLoader(mContext,id);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.IMAGE.ordinal()){
                List<Image> images =  LoaderManagerUtils.loaderFinish(data, mResultFolder);
                mAdapter = new ImageGridViewAdapter(ImageSelectorActivity.this,images);
                mImageGridView.setAdapter(mAdapter);
            }else if (mShare.getInt(Constants.SHARE_MODE, ShowType.IMAGE.ordinal()) == ShowType.VIDEO.ordinal()){
                videos = LoaderManagerUtils.getVideoDate(data);
                VideoGridViewAdapter videoAdapter = new VideoGridViewAdapter(mContext, videos);
                mImageGridView.setAdapter(videoAdapter);
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };
}
