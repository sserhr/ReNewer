/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVOSCloud;
import com.study.ranhuo.renewer.util.DoubleCache;

/**
 * Created by ranhuo on 15/11/24.
 */
public class MyApplication extends Application {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "Ru9C3UaMU0K4BITVXpm3BdQX", "dExY5dE9tPJjant700Teh3mz");
        AVOSCloud.useAVCloudCN();
        mInstance = this;

    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if(mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new DoubleCache(mInstance));
        }
        return this.mImageLoader;
    }
}
