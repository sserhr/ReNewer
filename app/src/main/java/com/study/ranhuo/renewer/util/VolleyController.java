package com.study.ranhuo.renewer.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ranhuo on 16/6/1.
 */
public class VolleyController {

    private static final String TAG = "VolleyController";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static VolleyController mInstance;

    private Context mContext;

}
