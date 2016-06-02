package com.study.ranhuo.renewer.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ranhuo on 16/6/1.
 */
public class DoubleCache implements ImageLoader.ImageCache {

    private static final String TAG = "DoubleCache";
    private static LruCache<String, Bitmap> mLruCache;
    private static DiskLruCache mDiskLruCache;
    private static final int MAXDISKSIZE = 10 * 1024 * 1024;

    public DoubleCache(Context context){
        int maxsize = (int) Runtime.getRuntime().maxMemory() / 8;
        mLruCache = new LruCache<String, Bitmap>(maxsize){
          protected int sizeOf(String key, Bitmap bitmap){
              return bitmap.getWidth() * bitmap.getHeight();
          }
        };

        mDiskLruCache = null;

        try{
            File cacheDir = getDiskCacheDir(context.getApplicationContext(), "bitmap");
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
                Log.i(TAG, "Dir not exits");
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, MAXDISKSIZE);
            Log.i("m123", "mDiskLruCache" + " " + cacheDir.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        if(mLruCache.get(url) != null){
            Log.i(TAG, "从LruCahce获取");
            return mLruCache.get(url);
        }else {
            String key = MD5Util.Md5(url);
            try{
                if(mDiskLruCache.get(key) != null){
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if(snapshot != null){
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        mLruCache.put(url, bitmap);
                        Log.i(TAG, "从DiskLruCahce获取");
                    }

                    return bitmap;
                }
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);

        String key = MD5Util.Md5(url);
        try{
            if(mDiskLruCache.get(key) == null){
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if(editor != null){
                    OutputStream outputStream = editor.newOutputStream(0);
                    if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)){
                        editor.commit();
                    }else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
            Log.i("m123", "1" + " " + cachePath);
        }else {
            cachePath = context.getCacheDir().getPath();
            Log.i("m123", "2" + " " + cachePath);
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static int getAppVersion(Context context){
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
