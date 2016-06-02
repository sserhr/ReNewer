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
import android.widget.TextView;

import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.entity.Message;

import java.util.List;


public class MessageListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Message> messageList;
    private LayoutInflater mInflater;

    public MessageListviewAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return messageList.size();
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
        Viewhoder holder = null;
        if(convertView == null){
            holder = new Viewhoder();
            convertView = mInflater.inflate(R.layout.message_fragment_listview_item, null);
            holder.username = (TextView) convertView.findViewById(R.id.message_username);
            holder.content = (TextView) convertView.findViewById(R.id.message_content);
            convertView.setTag(holder);
        }else {
            holder = (Viewhoder) convertView.getTag();

        }

        holder.username.setText(messageList.get(position).getSender() + ": " + "At " + messageList.get(position).getTime() + " said: ");
        holder.content.setText(messageList.get(position).getGoodsTitle());
        return convertView;
    }

    private class Viewhoder{
        private TextView username;
        private TextView content;

    }
}
