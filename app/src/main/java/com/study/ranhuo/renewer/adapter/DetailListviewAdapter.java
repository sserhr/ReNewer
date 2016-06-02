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
import android.widget.TextView;

import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.entity.Comments;

import java.util.List;


public class DetailListviewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Comments> mcommentsList;
    public DetailListviewAdapter(List<Comments> commentsList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mcommentsList = commentsList;
    }

    @Override
    public int getCount() {
        return mcommentsList.size();
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
            convertView = mInflater.inflate(R.layout.detail_activity_listview_item, null);
            holder.sendername = (TextView) convertView.findViewById(R.id.detail_comment_username);
            holder.content = (TextView) convertView.findViewById(R.id.detail_comment_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d("hr12345", "mcommentsListSize: " + mcommentsList.size() +" " + position + " " + "mcommentsListName: " + mcommentsList.get(position).getUsername());
        holder.sendername.setText(mcommentsList.get(position).getUsername() + ": " + "At " + mcommentsList.get(position).getTime() + " said: ");
        holder.content.setText(mcommentsList.get(position).getContent());
        return convertView;
    }

    private class ViewHolder{
        private TextView sendername;
        private TextView content;
    }
}
