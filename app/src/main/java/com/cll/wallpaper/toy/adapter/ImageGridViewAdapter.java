package com.cll.wallpaper.toy.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.cll.wallpaper.toy.R;
import com.cll.wallpaper.toy.bean.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cll on 2017/8/17.
 */

public class ImageGridViewAdapter extends BaseAdapter {

    List<Image> imagesList  = new ArrayList<>();
    private boolean showCamera = true;
    private LayoutInflater mInflater;
    private Context context;
    final int mGridWidth;
    public ImageGridViewAdapter(Context context, List<Image> imagesList){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.imagesList = imagesList;
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / 3;
    }
    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Image getItem(int i) {
            return imagesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (view == null){
            view = mInflater.inflate(R.layout.white_board_image_item,null);
            mHolder = new ViewHolder();
            mHolder.image = (ImageView) view.findViewById(R.id.images);
            mHolder.mask = view.findViewById(R.id.mask);
            view.setTag(mHolder);
        }
        mHolder = (ViewHolder) view.getTag();
        bindImageData(mHolder,  getItem(i));

        return view;
    }

    private void bindImageData(ViewHolder mHolder,final Image mImageData){
        if (!mImageData.name.contains("assets")){
            File imageFile = new File(mImageData.path);
            if (imageFile.exists()){
                Picasso.with(context)
                        .load(imageFile)
                        .placeholder(R.drawable.ic_launcher_background)
                        .tag("tag")
                        .resize(mGridWidth, mGridWidth)
                        .centerCrop()
                        .into(mHolder.image);
            }
        }else {
            Picasso.with(context)
                    .load("file:///android_asset/" + mImageData.path)
                    .placeholder(R.drawable.ic_launcher_background)
                    .tag("tag")
                    .resize(mGridWidth,mGridWidth)
                    .centerCrop()
                    .into(mHolder.image);
        }
    }

    private class ViewHolder{
        private ImageView image;
        private View mask;
    }

}
