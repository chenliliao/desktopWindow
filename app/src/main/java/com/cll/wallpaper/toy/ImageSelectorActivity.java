package com.cll.wallpaper.toy;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
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
    private ArrayList<Folder> mResultFolder = new ArrayList<>();
    private ImageGridViewAdapter mAdapter;
    private Toolbar mSettingTypeToolbar;
    private Button mCategoryButton;
    private ListView mFolderListView;
    private ImageFolderAdapter folderAdapter;

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
                Image image = (Image) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.putExtra("seletor_result", image.path);
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
        getLoaderManager().restartLoader(0, null,mLoaderCallback);
    }


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

            return LoaderManagerUtils.createCursorLoader(mContext,id);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            LoaderManagerUtils.loaderFinish(mContext,data,2,images,mResultFolder);
            mAdapter = new ImageGridViewAdapter(ImageSelectorActivity.this,images);
            mImageGridView.setAdapter(mAdapter);
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
}
