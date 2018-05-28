package com.cll.wallpaper.toy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.cll.wallpaper.toy.GlideApp;
import com.cll.wallpaper.toy.R;
import com.cll.wallpaper.toy.bean.Image;
import com.cll.wallpaper.toy.bean.Video;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by cll on 2018/5/12.
 */

public class VideoGridViewAdapter extends BaseAdapter{


    List<Video> list ;
    private Context mContext;   final int mGridWidth;
    public VideoGridViewAdapter(Context context, List<Video> list){
        this.list = list;
        this.mContext = context;

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
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
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

        String path = list.get(position).getPath();
//        String thumbImage = Environment.getExternalStorageDirectory() + "/DCIM/Camera/qq.jpg";
        String thumbImage = list.get(position).getThumbnailPath();
        Log.w("TAG,","test thumbnailPath"+"exists 0"+new File(Environment.getExternalStorageDirectory() + "/DCIM/.thumbnails").exists());
        Log.w("TAG,","test thumbnailPath"+"exists "+thumbImage);
//        holder.thumbImage.setImageBitmap(BitmapFactory.decodeFile(thumbImage));
//        Uri uri = Uri.fromFile(new File(thumbImage));
//        holder.thumbImage.setImageURI(uri);

        bindImageData(holder, path);
//        if(path != null){
////            Uri uri = Uri.parse(list.get(position).getPath());
////            Log.w("TAG", "test onLoadFinished list.get(position).getPath() " + list.get(position).getPath());
////            holder.thumbImage.setImageURI(uri);
//            Log.w("TAG","test getView path" + path);
//            Log.w("TAG","test getView thumbImage" + thumbImage);
//            if (!TextUtils.isEmpty(thumbImage)){
//        holder.thumbImage.setImageBitmap(BitmapFactory.decodeFile(thumbImage));
//            }else {
//                holder.thumbImage.setBackgroundResource(R.drawable.wallpaper);
//            }
//
////            holder.thumbImage.setImageBitmap(uri);
////               holder.thumbImage.setBackgroundResource(R.drawable.wallpaper);
////            Log.w("TAG", "test onLoadFinished uri 2 "+list.get(position).getThumbnailPath());
////            holder.thumbImage.setImageResource(R.drawable.wallpaper);
////            ThumbnailUtils.createVideoThumbnail(list.get(position).getThumbnailPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
////            holder.thumbImage.setImageBitmap(getVideoThumbnail());
////            holder.thumbImage.setImageBitmap(createVideoThumbnail(list.get(position).getPath()));
////            ThumbnailUtils.createVideoThumbnail()
//        }else{
//            holder.thumbImage.setImageResource(R.drawable.wallpaper);
//        }


        return convertView;
    }

    private void bindImageData(ViewHolder mHolder, final String path){
//        if (!mImageData.name.contains("assets")){

            File imageFile = new File(path);
        Log.w("TAG,","test thumbnailPath"+"exists 1"+imageFile.exists());
            if (imageFile.exists()){
                GlideApp.with(mContext)
                        .load(path)
                        .placeholder(R.drawable.ic_launcher_background)
                        .thumbnail(0.1f)
                        .into(mHolder.thumbImage);
                Log.w("TAG,","test thumbnailPath"+"exists 2");
        }
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
