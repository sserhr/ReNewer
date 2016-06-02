/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.ShowDetaiActivity;
import com.study.ranhuo.renewer.adapter.MessageListviewAdapter;
import com.study.ranhuo.renewer.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {


    private ListView message_lv;
    private List<Message> messageList = new ArrayList<>();
    private MessageListviewAdapter myAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tmp = inflater.inflate(R.layout.fragment_message, container, false);
        message_lv = (ListView) tmp.findViewById(R.id.message_lv);

        return tmp;
    }

    public void onResume() {
        super.onResume();
        //ShowDetaiActivity ShowDetaiActivity = new ShowDetaiActivity();
        //Log.v("hrtest", "display_fragment onResume");
        messageList.clear();
        AVQuery mypublish = AVQuery.getQuery("Goods").include("Title");
        mypublish.whereEqualTo("Publisher", AVUser.getCurrentUser());
        mypublish.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject ele : list) {

                        //tempMessage.setGoodsTitle(ele.getList("Title").get(0).toString());
                        AVQuery<AVObject> queryMyComments = AVQuery.getQuery("Comments");
                        queryMyComments.include("Sender");
                        queryMyComments.include("createdAt");
                        queryMyComments.include("Content");
                        queryMyComments.include("Goods");
                        queryMyComments.whereEqualTo("Goods", ele);
                        queryMyComments.whereNotEqualTo("Sender", AVUser.getCurrentUser());
                        queryMyComments.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                for (AVObject comment : list) {
                                    Log.d("hr12345", " queryTest: " + list.size());
                                    Message tempMessage = new Message();
                                    AVUser sender = (AVUser) comment.get("Sender");
                                    tempMessage.setSender(sender.getUsername());
                                    tempMessage.setTime(comment.get("createdAt").toString());
                                    tempMessage.setGoodsTitle(comment.get("Content").toString());
                                    AVObject good = (AVObject) comment.get("Goods");
                                    Log.d("hr12345789", " messageTestObjectId: " + good.getObjectId());
                                    tempMessage.setGoodsId(good.getObjectId());
                                    List<AVUser> publisherName = good.getList("Publisher");
                                    Log.d("hr12345789", " username: " + publisherName.get(0).getObjectId());
                                    tempMessage.setGoodsPublisher(AVUser.getCurrentUser().getUsername());
//                                    tempMessage.setGoodsTitle(good.getList("Title").get(0).toString());
                                    tempMessage.setGoodsPrice(good.getList("Price").get(0).toString());
                                    tempMessage.setGoodsDescription(good.getList("Discription").get(0).toString());
                                    messageList.add(tempMessage);

                                }
//                                Log.d("hr12345", " messageTest: " + messageList.size());
                                myAdapter = new MessageListviewAdapter(messageList, getContext());
                                message_lv.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }


            }


        });

        message_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowDetaiActivity.class);
                intent.putExtra("objectid", messageList.get(i).getGoodsId());
                intent.putExtra("username", messageList.get(i).getGoodsPublisher());
                intent.putExtra("price", messageList.get(i).getGoodsPrice());
                intent.putExtra("description", messageList.get(i).getGoodsDescription());
                startActivity(intent);
            }
        });
    }

}
