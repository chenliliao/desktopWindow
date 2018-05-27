package com.cll.wallpaper.toy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.cll.wallpaper.toy.R;
import com.cll.wallpaper.toy.bean.Video;

import java.util.List;

/**
 * Created by cll on 2018/5/12.
 */

public class VideoGridViewAdapter extends BaseAdapter{


    List<Video> list ;
    private Context mContext;
    public VideoGridViewAdapter(Context context, List<Video> list){
        this.list = list;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, null);
            holder.thumbImage = (ImageView)convertView.findViewById(R.id.image);
//            holder.titleText = convertView.findViewById(R.id.mask);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //显示信息
//        holder.titleText.setText(videoItems.get(position).title);
        if(list.get(position).getPath() != null){
//            Uri uri = Uri.parse(list.get(position).getPath());
//            Log.w("TAG", "test onLoadFinished list.get(position).getPath() " + list.get(position).getPath());
//            holder.thumbImage.setImageURI(uri);
//            holder.thumbImage.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(list.get(position).getPath())));
//            holder.thumbImage.setImageBitmap(uri);
//            holder.thumbImage.setBackgroundResource(R.drawable.wallpaper);
            Log.w("TAG", "test onLoadFinished uri 2 "+list.get(position).getThumbnailPath());

//            ThumbnailUtils.createVideoThumbnail(list.get(position).getThumbnailPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//            holder.thumbImage.setImageBitmap(getVideoThumbnail());
            holder.thumbImage.setImageBitmap(createVideoThumbnail(list.get(position).getPath()));
//            ThumbnailUtils.createVideoThumbnail()
        }

        return convertView;
    }

    public static Bitmap getVideoThumbnail(String videoPath,int width,int height,int kind) {
        Bitmap bitmap =null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
            Log.w("TAG", "test onLoadFinished uri 5");
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            Log.w("TAG", "test onLoadFinished uri 3" + ex.toString());
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            Log.w("TAG", "test onLoadFinished uri 4");
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }
    class ViewHolder{
        ImageView thumbImage;
        TextView titleText;
    }
}
