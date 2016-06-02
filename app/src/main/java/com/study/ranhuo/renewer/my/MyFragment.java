/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.login.LoginActivity;

public class MyFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout settinglayout;
    private RelativeLayout myPublishlayout;
    private RelativeLayout mySelllayout;
    private RelativeLayout myBuylayout;
    private RelativeLayout userRl;
    private ImageView profile_img;
    private TextView username_tv;
    private TextView log_tv;
    private LoginActivity loginActivity;
    private MyPublishActivity myPublishActivity;
    private MyBuyedActivity myBuyedActivity;
    private MySoldActivity mySoldActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = new LoginActivity();
        myPublishActivity = new MyPublishActivity();
        myBuyedActivity = new MyBuyedActivity();
        mySoldActivity = new MySoldActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View temp = inflater.inflate(R.layout.fragment_my, container, false);
        settinglayout = (RelativeLayout) temp.findViewById(R.id.my_rl_setting);
        myPublishlayout = (RelativeLayout) temp.findViewById(R.id.my_rl_my_publish);
        mySelllayout = (RelativeLayout) temp.findViewById(R.id.my_rl_my_sell);
        myBuylayout = (RelativeLayout) temp.findViewById(R.id.my_rl_my_buy);
        userRl = (RelativeLayout) temp.findViewById(R.id.my_rl_usershow);
        username_tv = (TextView) temp.findViewById(R.id.my_tv_username);
        log_tv = (TextView) temp.findViewById(R.id.my_tv_login);
        if(AVUser.getCurrentUser() == null){
            username_tv.setText("Please Login");

            log_tv.setText("Login");
            userRl.setOnClickListener(this);
        }else {
            username_tv.setText(AVUser.getCurrentUser().getUsername());
            log_tv.setText("Logoff");

        }
        log_tv.setOnClickListener(this);
        userRl.setOnClickListener(this);
        settinglayout.setOnClickListener(this);
        myPublishlayout.setOnClickListener(this);
        mySelllayout.setOnClickListener(this);
        myBuylayout.setOnClickListener(this);
        return temp;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_rl_setting:

                break;
            case R.id.my_rl_usershow:
                if(AVUser.getCurrentUser() == null){
                    Intent intent = new Intent(this.getContext(), loginActivity.getClass());
                    startActivity(intent);
                }
                break;
            case R.id.my_tv_login:
                if(AVUser.getCurrentUser() != null){
                    AVUser.logOut();
                    log_tv.setText("Login");
                    username_tv.setText("Please Login");
                }else{
                    Intent intent = new Intent(this.getContext(), loginActivity.getClass());
                    startActivity(intent);
                }
                break;
            case R.id.my_rl_my_publish:
                if(AVUser.getCurrentUser() != null){
                    Intent intent = new Intent(this.getContext(), myPublishActivity.getClass());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this.getContext(), loginActivity.getClass());
                    startActivity(intent);
                }
                break;
            case R.id.my_rl_my_sell:
                if(AVUser.getCurrentUser() != null){
                    Intent intent = new Intent(this.getContext(), mySoldActivity.getClass());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this.getContext(), loginActivity.getClass());
                    startActivity(intent);
                }
                break;
            case R.id.my_rl_my_buy:
                if(AVUser.getCurrentUser() != null){
                    Intent intent = new Intent(this.getContext(), myBuyedActivity.getClass());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this.getContext(), loginActivity.getClass());
                    startActivity(intent);
                }
                break;

        }
    }
}
