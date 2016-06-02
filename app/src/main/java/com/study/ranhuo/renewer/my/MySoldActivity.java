/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.ShowDetaiActivity;
import com.study.ranhuo.renewer.adapter.MyPublishSellBuyListviewAdapter;
import com.study.ranhuo.renewer.entity.GoodObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranhuo on 15/12/12.
 */
public class MySoldActivity extends Activity{
    private List<GoodObject> goodList;
    private Context mContext;
    private ListView goodsShow_lv;
    private MyPublishSellBuyListviewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell);
        init();
        final AVQuery mypublish = AVQuery.getQuery("Goods").include("Title");
        mypublish.include("Price");
        mypublish.include("Images");
        mypublish.include("Discription");
        mypublish.include("Publisher");
        mypublish.include("Buyer");
        mypublish.whereEqualTo("Publisher", AVUser.getCurrentUser());
        mypublish.whereNotEqualTo("Buyer", null);
        mypublish.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e) {
                    //goods.clear();
                    for (AVObject avele : list) {
                        GoodObject tempObj = new GoodObject();
                        List<AVUser> test = avele.getList("Publisher");
                        tempObj.setUsername(test.get(0).getUsername());
                        tempObj.setObjectid(avele.getObjectId());
                        tempObj.setDiscription(avele.getList("Discription").get(0).toString());
                        tempObj.setTitle(avele.getList("Title").get(0).toString());
                        if(avele.get("Buyer") == null){
                            //Log.d("hrBuy", "Buyer: " + goodList.size() + " " + "null");
                            tempObj.setBuyFlag(false);
                        }else {
                            //Log.d("hrBuy", "Buyer: " + test.getUsername());
                            tempObj.setBuyFlag(true);
                        }
//                        Log.d("hrPublish", "Discription: " + avele.getList("Title").get(0).toString());

                        tempObj.setPrice(avele.getList("Price").get(0).toString());
//                        Log.d("hrPublish", "Price: " + avele.getList("Price").get(0));
                        List<AVFile> templist = avele.getList("Images");
                        List<String> urlList = new ArrayList();
                        for (AVFile avfile : templist) {
                            urlList.add(avfile.getThumbnailUrl(true, 80, 80));
                        }
                        tempObj.setURLList(urlList);
                        goodList.add(tempObj);

                    }
                    //Log.d("hrPublish", "goodSizeResume: " + goodList.get(0).getTitle());
                    mAdapter = new MyPublishSellBuyListviewAdapter(goodList, mContext);
                    goodsShow_lv.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        goodsShow_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goodsShow_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Log.d("hrLearn", "position: " + i + " " + "id: " + l + " " + "goodSize: " + goods.size() + " " + "objectid" + goods.get(i).getObjectid());
                        Intent intent = new Intent(mContext, ShowDetaiActivity.class);
                        intent.putExtra("objectid", goodList.get(i).getObjectid());
                        intent.putExtra("username", goodList.get(i).getUsername());
                        intent.putExtra("price", goodList.get(i).getPrice());
                        intent.putExtra("description", goodList.get(i).getDiscription());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void init(){
        goodList = new ArrayList<>();
        mContext = this;
        goodsShow_lv = (ListView) findViewById(R.id.my_sell_display_lv);
    }
}
