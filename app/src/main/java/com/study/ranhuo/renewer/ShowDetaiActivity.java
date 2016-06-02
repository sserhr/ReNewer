package com.study.ranhuo.renewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.study.ranhuo.renewer.adapter.DetailListviewAdapter;
import com.study.ranhuo.renewer.entity.Comments;
import com.study.ranhuo.renewer.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranhuo on 15/11/24.
 */

public class ShowDetaiActivity extends Activity {

    private TextView username_tv;
    private TextView price_tv;
    private TextView description_tv;
    private ImageView detail_img1;
    private ImageView detail_img2;
    private ImageView detail_img3;
    private ImageView sendComents_iv;
    private EditText comments_et;
    private ImageView buy_iv;
    private LinearLayout comment_ll;
    private ProgressDialog progressDialog;
    private RelativeLayout detail_rl_footer;

    private String objectid;
    private String username;
    private String price;
    private String description;
    private String mComments;
    private ListViewForScrollView comments_lv;
    private List<String> urlList = new ArrayList<>();
    private List<Comments> commentsList = new ArrayList<>();
    private DetailListviewAdapter myAdapter;
    private Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetail);

        initial();
        if(AVUser.getCurrentUser() == null){
            comments_et.setVisibility(View.INVISIBLE);
            sendComents_iv.setVisibility(View.INVISIBLE);
            buy_iv.setVisibility(View.INVISIBLE);
            comment_ll.setVisibility(View.INVISIBLE);
        }
        detail_rl_footer.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        Log.d("hrLearn", "intentObjectId: " + intent.getStringExtra("objectid"));
        objectid = intent.getStringExtra("objectid");
        username = intent.getStringExtra("username");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        username_tv.setText(username);
        price_tv.setText("$" + price);
        description_tv.setText(description);


        AVQuery<AVObject> query = new AVQuery<AVObject>("Goods").include("Images");
        query.include("Commnents");
        query.include("Publisher");
        //AVObject detail;

        query.getInBackground(objectid, new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {

                List<AVUser> publisher = avObject.getList("Publisher");
                //Log.d("hr1234", "publishername: " + publisher.get(0).getUsername());
                if(AVUser.getCurrentUser()!=null && !publisher.get(0).equals(AVUser.getCurrentUser())){
                    detail_rl_footer.setVisibility(View.VISIBLE);
                }
                buy_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(ShowDetaiActivity.this).setTitle("Hint").setMessage("Are you sure to buy?").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                avObject.put("Buyer", AVUser.getCurrentUser());
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            Toast.makeText(mcontext, "BuySuccessfully",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                    }
                });
                List<AVFile> detail_img = avObject.getList("Images");
                for (AVFile ele : detail_img) {
                    urlList.add(ele.getUrl());
                    Log.d("hr2", "urlListSize: " + urlList.size());
                }

                List<ImageView> imgList = new ArrayList<ImageView>();
                imgList.add(detail_img1);
                imgList.add(detail_img2);
                imgList.add(detail_img3);
                for (int i = 0; i < urlList.size(); i++) {
                    ImageLoader.ImageListener tempImageListener = ImageLoader.getImageListener(imgList.get(i), R.drawable.default_img, R.drawable.failed_img);
                    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
                    imageLoader.get(urlList.get(i), tempImageListener);
                }

                AVQuery<AVObject> queryComments = AVQuery.getQuery("Comments");
                queryComments.include("Sender");
                queryComments.include("createdAt");
                queryComments.whereEqualTo("Goods", avObject);
                queryComments.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {


                        for(AVObject ele : list){
                            Comments tempComment = new Comments();
                            AVUser sender = (AVUser) ele.get("Sender");
                            tempComment.setUsername(sender.getUsername());
                            tempComment.setTime(ele.get("createdAt").toString());
                            tempComment.setContent(ele.get("Content").toString());
                            commentsList.add(tempComment);
                        }
//                        for(int i = 0; i < commentsList.size(); i++){
//                            Log.d("hr12345", i + " " + commentsList.get(i).getContent());
//                        }
                        myAdapter = new DetailListviewAdapter(commentsList, mcontext);
                        comments_lv.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }
                });

            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        if(AVUser.getCurrentUser() != null){
            sendComents_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mComments = comments_et.getText().toString();
                    if (mComments.isEmpty()) {
                        showCommentsEditERR();
                    }else {
                        AVObject myComments = new AVObject("Comments");
                        myComments.put("Content", mComments);
                        myComments.put("Sender", AVUser.getCurrentUser());
                        myComments.put("Goods", AVObject.createWithoutData("Goods", objectid));
                        progressDialogShow();
                        myComments.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e == null){
                                    progressDialogDismiss();
                                    Toast.makeText(ShowDetaiActivity.this, "Comment Successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    progressDialogDismiss();
                                    showSendCommentsError();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    private void initial() {
        mcontext = this;
        detail_img1 = (ImageView) findViewById(R.id.detail_iv_1);
        detail_img2 = (ImageView) findViewById(R.id.detail_iv_2);
        detail_img3 = (ImageView) findViewById(R.id.detail_iv_3);
        username_tv = (TextView) findViewById(R.id.detail_tv_username);
        price_tv = (TextView) findViewById(R.id.detail_tv_price);
        description_tv = (TextView) findViewById(R.id.detail_tv_description);
        comments_lv = (ListViewForScrollView) findViewById(R.id.detail_lv_comments);
        comments_et = (EditText) findViewById(R.id.detail_et_comment);
        sendComents_iv = (ImageView) findViewById(R.id.detail_iv_sendcomments);
        buy_iv = (ImageView) findViewById(R.id.detail_iv_buy);
        comment_ll = (LinearLayout) findViewById(R.id.detaill_ll_comment);
        detail_rl_footer = (RelativeLayout) findViewById(R.id.detail_rl_footer);
    }

    public void showBuyHint(){



    }

    public void showCommentsEditERR(){
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("Comments should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void showSendCommentsError() {
        new AlertDialog.Builder(this)
                .setTitle(
                        this.getResources().getString(
                                R.string.error_title))
                .setMessage("Comment failed, please check the network connection")
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }


    private void progressDialogDismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void progressDialogShow() {
        progressDialog = ProgressDialog
                .show(this,
                        this.getResources().getText(
                                R.string.dialog_message_title),
                        this.getResources().getText(
                                R.string.dialog_text_wait), true, false);
    }


}
