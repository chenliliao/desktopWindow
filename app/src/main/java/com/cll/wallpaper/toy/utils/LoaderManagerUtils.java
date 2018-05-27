package com.cll.wallpaper.toy.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.media.MediaFormat;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.cll.wallpaper.toy.bean.Folder;
import com.cll.wallpaper.toy.bean.Image;
import com.cll.wallpaper.toy.bean.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cll on 2017/8/18.
 */

public class LoaderManagerUtils {

    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID};
    private static final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID};

    static String[] thumbColumns = new String[]{
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID
    };
//    private static ArrayList<Folder> mResultFolder = new ArrayList<>();

    public static CursorLoader createCursorLoader(Context context, int id){
        CursorLoader mCursorLoader = null;
        if(id == 0){
            mCursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
//            mCursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    IMAGE_PROJECTION,null,
//                    null,null);
//        }
//
//        if (false){
//            mCursorLoader = new CursorLoader(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    VIDEO_PROJECTION, VIDEO_PROJECTION[4] + " > 0", null , null);
//
//            Log.w("TAG", "test onLoadFinished " + "1");


        }
        return  mCursorLoader;
    }

    private static boolean hasFolderGened = false;
    public static void loaderFinish(Context context , Cursor data, int mRequestType, List<Image> images, ArrayList<Folder> mResultFolder){
        if(data != null){
            if (data.getCount() > 0){
                data.moveToFirst();
                getImageData(data,images,mResultFolder);
            }

        }
    }

    private static void getImageData(Cursor data, List<Image> images, ArrayList<Folder> mResultFolder){
        do {
            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
            Image image = null;
            if (fileExist(path) && !TextUtils.isEmpty(name)){
                image = new Image(path, name, dateTime);
                images.add(image);
            }
            if (!hasFolderGened && fileExist(path)){
                File folderFile = new File(path).getParentFile();
                if (folderFile != null && folderFile.exists()) {
                    String fp = folderFile.getAbsolutePath();
                    Folder f = getFolderByPath(fp,mResultFolder);
                    if (f == null) {
                        Folder folder = new Folder();
                        folder.name = folderFile.getName();
                        folder.path = fp;
                        folder.cover = image;
                        List<Image> imageList = new ArrayList<>();
                        imageList.add(image);
                        folder.images = imageList;
                        mResultFolder.add(folder);
                    } else {
                        f.images.add(image);
                    }
                }
            }
        }while (data.moveToNext());
    }

    public static  List<Video> getVideoDate(final Context context, final Cursor data){
        if (data == null || data.getCount() == 0){
            return null;
        }

        List<Video> videos = new ArrayList();
        data.moveToFirst();


        do {

            int id = data.getInt(data.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
            String[] selectionArgs = new String[]{id+""};
            Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
            thumbCursor.moveToFirst();

            String thumbnailPath = thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
            Video video = new Video(path,thumbnailPath);
            videos.add(video);

        }while (data.moveToNext());
        Log.w("TAG", "test onLoadFinished " + "videos  = " + videos.size());
        return videos;
    }


    private static boolean fileExist(String path) {
        if (!TextUtils.isEmpty(path)) {
            return new File(path).exists();
        }
        return false;
    }

    private static Folder getFolderByPath(String path, ArrayList<Folder> mResultFolder) {
        if (mResultFolder != null) {
            for (Folder folder : mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }
}
