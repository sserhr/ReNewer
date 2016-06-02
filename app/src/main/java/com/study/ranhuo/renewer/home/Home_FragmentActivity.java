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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.study.ranhuo.renewer.R;
//import com.study.ranhuo.renewer.category.categoryFragment;
import com.study.ranhuo.renewer.login.LoginActivity;
import com.study.ranhuo.renewer.message.MessageFragment;
import com.study.ranhuo.renewer.my.MyFragment;
import com.study.ranhuo.renewer.publish.publishActivity;


public class Home_FragmentActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView[] tabhost_menu= new ImageView[4];
    private int[] tabhost_menu_id = { R.id.iv_menu_0, R.id.iv_menu_2, R.id.iv_menu_3, R.id.iv_menu_4 };
    private int[] select_on = { R.drawable.guide_home_on, R.drawable.guide_publish_on, R.drawable.guide_message_on, R.drawable.guide_my_on};
    private int[] select_off = { R.drawable.tabhost_menu_0_select, R.drawable.tabhost_menu_2_select, R.drawable.tabhost_menu_3_select, R.drawable.tabhost_menu_4_select };
    private DisplayFragment display_Fragment;
    //private categoryFragment category_Fragment;
    private MyFragment my_Fragment;
    private MessageFragment message_Fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentactivity_home);
        //AVUser.logOut();
        //LoginHelper.user = new AVUser();
        //LoginHelper.user = AVUser.getCurrentUser();
        Log.d("hr1", "userOncreat: " + AVUser.getCurrentUser());
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        initial();
    }


    private void initial() {
       for(int i =0; i < tabhost_menu.length; i++){
           tabhost_menu[i] = (ImageView) findViewById(tabhost_menu_id[i]);
           tabhost_menu[i].setOnClickListener(this);
       }
        if(display_Fragment == null){
            display_Fragment = new DisplayFragment();
            addFragment(display_Fragment);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_menu_0:
                if(display_Fragment == null){
                    addFragment(display_Fragment);
                    showFragment(display_Fragment);
                }else {
                    if(display_Fragment.isHidden())
                        showFragment(display_Fragment);
                }
                break;
//            case R.id.iv_menu_1:
//                if(category_Fragment == null){
//                    category_Fragment = new categoryFragment();
//                    addFragment(category_Fragment);
//                    showFragment(category_Fragment);
//                }else if(category_Fragment.isHidden()){
//                    showFragment(category_Fragment);
//                }
//                break;
            case R.id.iv_menu_2:
                if(AVUser.getCurrentUser() == null){
                    //Log.d("hr1", "userButton2: " + AVUser.getCurrentUser());
                    Intent intent = new Intent(Home_FragmentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Home_FragmentActivity.this, publishActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_menu_3:
                if(AVUser.getCurrentUser() == null){
                    //Log.d("hr1", "userButton3: " + AVUser.getCurrentUser());
                    Intent intentLogin = new Intent(Home_FragmentActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }else {
                    if(message_Fragment == null){
                        message_Fragment = new MessageFragment();
                        addFragment(message_Fragment);
                        showFragment(message_Fragment);
                    }else if(message_Fragment.isHidden()){
                        showFragment(message_Fragment);
                    }
                }
                break;
            case R.id.iv_menu_4:
                if(my_Fragment == null){
                    my_Fragment = new MyFragment();
                    addFragment(my_Fragment);
                    showFragment(my_Fragment);
                }else {
                    if(my_Fragment.isHidden())
                        showFragment(my_Fragment);
                }
                break;

        }
        changeImageOnclick(view);
    }
    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment);
        ft.commit();
    }
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

        //ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);


        if (display_Fragment != null) {
            ft.hide(display_Fragment);
        }
//        if (category_Fragment != null) {
//            ft.hide(category_Fragment);
//        }
        if(my_Fragment != null){
            ft.hide(my_Fragment);
        }
        if(message_Fragment != null){
            ft.hide(message_Fragment);
        }



        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    public void changeImageOnclick(View view){
        for(int i = 0; i < tabhost_menu.length; i++){
            tabhost_menu[i].setImageResource(select_off[i]);
            if(view.getId() == tabhost_menu[i].getId()){
                tabhost_menu[i].setImageResource(select_on[i]);
            }
        }
    }
    public Bitmap array2bitmap(byte[] bytes){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmapTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.v("hr", "bitmapTemp" + bitmapTemp.getWidth());
        return bitmapTemp;
    }

}
