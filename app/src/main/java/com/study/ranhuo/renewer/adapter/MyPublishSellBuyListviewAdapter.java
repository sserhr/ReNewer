/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.adapter;

import android.content.Context;
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

import java.util.List;


public class MyPublishSellBuyListviewAdapter extends BaseAdapter {



    private List<GoodObject> goodList;
    private LayoutInflater mInflater;


    public MyPublishSellBuyListviewAdapter(List<GoodObject> goodObjectList, Context context) {

        this.goodList = goodObjectList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.my_listview_item, null);
            holder.thumbnail_iv = (ImageView) convertView.findViewById(R.id.my_psb_iv_thumbnail);
            holder.price_tv = (TextView) convertView.findViewById(R.id.my_psb_tv_price);
            holder.title_tv = (TextView) convertView.findViewById(R.id.my_psb_tv_title);
            holder.state_tv = (TextView) convertView.findViewById(R.id.my_psb_tv_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.ImageListener tempImageListener = ImageLoader.getImageListener(holder.thumbnail_iv, R.drawable.default_img, R.drawable.failed_img);
        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(goodList.get(position).getURLList().get(0), tempImageListener);
        //Log.d("hrPublish", "adapter: " + goodList.get(position).getTitle());
        holder.price_tv.setText("$" + goodList.get(position).getPrice());
        holder.title_tv.setText(goodList.get(position).getTitle());
        if(!goodList.get(position).getBuyFlag()){
            holder.state_tv.setText("Selling");
        }else {
            holder.state_tv.setText("Sold");
        }
        return convertView;
    }

    public class ViewHolder{
        private ImageView thumbnail_iv;
        private TextView title_tv;
        private TextView price_tv;
        private TextView state_tv;
    }
}
