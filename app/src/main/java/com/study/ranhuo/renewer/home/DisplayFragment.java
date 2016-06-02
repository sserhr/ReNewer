/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.study.ranhuo.renewer.adapter.DisplayListviewAdapter;
import com.study.ranhuo.renewer.entity.GoodObject;

import java.util.ArrayList;
import java.util.List;


public class DisplayFragment extends Fragment {

    private static ListView displayLV;
    private static DisplayListviewAdapter myAdapter;
    private List<GoodObject> goods = new ArrayList<GoodObject>();


//    public DisplayFragment() {
//        AVQuery<AVObject> myquery = AVQuery.getQuery("Goods").include("Publisher");
//        myquery.include("Discription");
//        myquery.include("Price");
//        myquery.include("Images");
//        myquery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (null == e) {
//                    goods.clear();
//                    for (AVObject avele : list) {
//                        GoodObject tempObj = new GoodObject();
//
//                        List<AVUser> test = avele.getList("Publisher");
//                        Log.d("hr", "Publisher: " + test.get(0).getUsername());
//                        tempObj.setObjectid(avele.getObjectId());
//                        tempObj.setUsername(test.get(0).getUsername());
//                        tempObj.setDiscription(avele.getList("Discription").get(0).toString());
//                        Log.d("hr", "Discription: " + avele.getList("Discription").get(0));
//
//                        tempObj.setPrice(avele.getList("Price").get(0).toString());
//                        Log.d("hr", "Price: " + avele.getList("Price").get(0));
//                        List<AVFile> templist = avele.getList("Images");
//                        List<String> urlList = new ArrayList();
//                        for (AVFile avfile : templist) {
//                            urlList.add(avfile.getThumbnailUrl(true, 50, 50));
//                        }
//                        tempObj.setURLList(urlList);
//                        goods.add(tempObj);
//
//                    }
//                    Log.d("hr2", "goodSizeInitial: " + goods.size());
//                    myAdapter = new DisplayListviewAdapter(goods, getContext());
//                    displayLV.setAdapter(myAdapter);
////                        displayLV.deferNotifyDataSetChanged();
//                    myAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tempView = inflater.inflate(R.layout.fragment_display, container, false);
        Log.d("hr", "goods Size: " + goods.size());

        displayLV = (ListView) tempView.findViewById(R.id.display_lv);
        displayLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("hrLearn", "position: " + i + " " + "id: " + l + " " + "goodSize: " + goods.size() + " " + "objectid" + goods.get(i).getObjectid());
                Intent intent = new Intent(getContext(), ShowDetaiActivity.class);
                intent.putExtra("objectid", goods.get(i).getObjectid());
                intent.putExtra("username", goods.get(i).getUsername());
                intent.putExtra("price", goods.get(i).getPrice());
                intent.putExtra("description", goods.get(i).getDiscription());
                startActivity(intent);
            }
        });
//        displayLV.setAdapter(myAdapter);
        return tempView;

    }

    @Override
    public void onResume() {
        super.onResume();
        ShowDetaiActivity ShowDetaiActivity = new ShowDetaiActivity();
        //Log.v("hrtest", "display_fragment onResume");

        AVQuery<AVObject> myquery = AVQuery.getQuery("Goods").include("Publisher");
        myquery.include("Discription");
        myquery.include("Price");
        myquery.include("Images");
        myquery.include("Buyer");
        myquery.whereEqualTo("Buyer", null);
        myquery.orderByDescending("createdAt");
        myquery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e) {
                    goods.clear();
                    for (AVObject avele : list) {
                        GoodObject tempObj = new GoodObject();

                        List<AVUser> test = avele.getList("Publisher");
                        Log.d("hr", "Publisher: " + test.get(0).getUsername());
                        tempObj.setObjectid(avele.getObjectId());
                        tempObj.setUsername(test.get(0).getUsername());
                        tempObj.setDiscription(avele.getList("Discription").get(0).toString());
                        Log.d("hr", "Discription: " + avele.getList("Discription").get(0));

                        tempObj.setPrice(avele.getList("Price").get(0).toString());
                        Log.d("hr", "Price: " + avele.getList("Price").get(0));
                        List<AVFile> templist = avele.getList("Images");
                        List<String> urlList = new ArrayList();
                        for (AVFile avfile : templist) {
                            urlList.add(avfile.getThumbnailUrl(true, 80, 100));
                        }
                        tempObj.setURLList(urlList);
                        goods.add(tempObj);

                    }
                    Log.d("hr2", "goodSizeResume: " + goods.size());
                    myAdapter = new DisplayListviewAdapter(goods, getContext());
                    displayLV.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public Bitmap array2bitmap(byte[] bytes){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmapTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.v("hr", "bitmapTemp" + bitmapTemp.getWidth());
        return bitmapTemp;
    }



}
