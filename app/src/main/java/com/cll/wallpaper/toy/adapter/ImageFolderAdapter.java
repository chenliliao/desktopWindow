package com.cll.wallpaper.toy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.cll.wallpaper.toy.R;
import com.cll.wallpaper.toy.bean.Folder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cll on 2017/9/11.
 */

public class ImageFolderAdapter extends BaseAdapter {

    private ArrayList<Folder> mFolderList = new ArrayList<>();
    private Context context;
    public ImageFolderAdapter(ArrayList<Folder> mFolderList, Context context){
        this.mFolderList = mFolderList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return mFolderList == null ? 0 : mFolderList.size() +1;
    }

    @Override
    public Folder getItem(int position) {
        if (position == 0){
            return  null;
        }else{
            return mFolderList.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_folder_select_item,null);
            mHolder = new ViewHolder();
            mHolder.initWidgets(convertView);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.bindData(position == 0 ? null : mFolderList.get(position -1), position);
        return convertView;
    }

    class ViewHolder{
        private View folders;
        private ImageView cover;
        private TextView folderName;
        private TextView path;
        private TextView count;
        public ImageView indicator;
        private void initWidgets(View view){
            folders = (View) view.findViewById(R.id.folder_layout);
            cover = (ImageView)view.findViewById(R.id.folder_image);
            folderName = (TextView) view.findViewById(R.id.folder_name);
            path = (TextView) view.findViewById(R.id.folder_path);
            count = (TextView) view.findViewById(R.id.folder_count);
            indicator = (ImageView) view.findViewById(R.id.folder_selected);

        }

        private void bindData(Folder data,final int position){

            if (position == 0) {
                folderName.setText("所有图片");
                path.setText("/sdcard");
                count.setText(getTotalImageSize()+" 张");
                Folder f = mFolderList.get(1);
                Log.w("tag","test  asd  asd "+f.cover.path);
                    File coverFile = new File(f.cover.path);
                    if (f != null) {
                        Picasso.with(context)
                                .load(coverFile)
                                .placeholder(R.drawable.ic_launcher_background)
                                .resize(70,70)
                                .centerCrop()
                                .into(cover);
                    } else {
                        cover.setImageResource(R.drawable.ic_launcher_background);
                    }
                    return;
            }

            folderName.setText(data.name);
            path.setText(data.path);
            count.setText(data.images.size()+" 张");

            if (!data.path.contains("asset")) {
                File file = new File(data.cover.path);
                if (file != null) {
                    Picasso.with(context)
                            .load(file)
                            .placeholder(R.drawable.ic_launcher_background)
                            .resize(70,70)
                            .centerCrop()
                            .into(cover);
                }
            } else {
                Picasso.with(context)
                        .load("file:///android_asset/" + data.cover.path)
                        .placeholder(R.drawable.ic_launcher_background)
                        .tag("tag")
                        .resize(70,70)
                        .centerCrop()
                        .into(cover);
            }
        }

        public void getSelected(){
            indicator.setVisibility(View.VISIBLE);
        }
        private int getTotalImageSize() {
            int result = 0;
            if (mFolderList != null && mFolderList.size() > 0) {
                for (Folder f : mFolderList) {
                    result += f.images.size();
                }
            }
            return result;
        }

    }
}
