/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.study.ranhuo.renewer.MyApplication;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.entity.GoodObject;

import java.util.ArrayList;
import java.util.List;


public class DisplayListviewAdapter extends BaseAdapter {

    private List<GoodObject> goodsList;
    private LayoutInflater myInflater;
    //private RequestQueue mRequestQueue;
    //private static ImageLoader imageLoader;

    public DisplayListviewAdapter(List<GoodObject> list, Context context) {
        this.goodsList = list;
        this.myInflater = LayoutInflater.from(context);
//        this.mRequestQueue = Volley.newRequestQueue(context);
//        imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View covertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(covertView == null){
            holder = new ViewHolder();
            covertView = myInflater.inflate(R.layout.display_fragment_listview_item, null);
            holder.username_tv = (TextView) covertView.findViewById(R.id.listview_tv_username);
            holder.privce_tv = (TextView) covertView.findViewById(R.id.listview_tv_price);
            holder.discription_tv = (TextView) covertView.findViewById(R.id.listview_tv_discription);
            holder.img_1 = (ImageView) covertView.findViewById(R.id.listviewe_img_1);
            holder.img_2 = (ImageView) covertView.findViewById(R.id.listview_img_2);
            holder.img_3 = (ImageView) covertView.findViewById(R.id.listview_img_3);
            covertView.setTag(holder);
        }else {
            holder = (ViewHolder) covertView.getTag();
        }

        //covertView = myInflater.inflate(R.layout.display_fragment_listview_item, null);
        //TextView username = (TextView) covertView.findViewById(R.id.listview_tv_username);
        holder.username_tv.setText(goodsList.get(position).getUsername());
        //TextView price = (TextView) covertView.findViewById(R.id.listview_tv_price);
        holder.privce_tv.setText("$" + goodsList.get(position).getPrice());
        //TextView discription = (TextView) covertView.findViewById(R.id.listview_tv_discription);
        holder.discription_tv.setText(goodsList.get(position).getDiscription());
        //ImageView img1 = (ImageView) covertView.findViewById(R.id.listviewe_img_1);
        //ImageView img2 = (ImageView) covertView.findViewById(R.id.listview_img_2);
        //ImageView img3 = (ImageView) covertView.findViewById(R.id.listview_img_3);
        List<ImageView> imgList = new ArrayList<>();
        imgList.add(holder.img_1);
        imgList.add(holder.img_2);
        imgList.add(holder.img_3);
        for(int i = 0; i < goodsList.get(position).getURLList().size(); i++){
            ImageLoader.ImageListener tempImageListener = ImageLoader.getImageListener(imgList.get(i), 0, R.drawable.failed_img);
            //Log.d("hr", "thumDebug: " + goodsList.get(position).getURLList().get(i));
            ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
            imageLoader.get(goodsList.get(position).getURLList().get(i), tempImageListener);
        }
        Log.d("hr", "getview(): " + position);
        return covertView;
    }
    private class ViewHolder{
        private TextView username_tv;
        private TextView privce_tv;
        private TextView discription_tv;
        private ImageView img_1;
        private ImageView img_2;
        private ImageView img_3;
    }
//    public static class BitmapCache implements ImageLoader.ImageCache {
//
//        private LruCache<String, Bitmap> mCache;
//
//        public BitmapCache() {
//            int maxSize = 10 * 1024 * 1024;
//            mCache = new LruCache<String, Bitmap>(maxSize) {
//                @Override
//                protected int sizeOf(String key, Bitmap bitmap) {
//                    return bitmap.getRowBytes() * bitmap.getHeight();
//                }
//            };
//        }
//
//        @Override
//        public Bitmap getBitmap(String url) {
//
//            Log.d("hr", "get image from cache");
//            return mCache.get(url);
//        }
//
//        @Override
//        public void putBitmap(String url, Bitmap bitmap) {
//            Log.d("hr", "put image to cache");
//            mCache.put(url, bitmap);
//        }
//
//    }
}
