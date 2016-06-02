/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.home.Home_FragmentActivity;
import com.study.ranhuo.renewer.util.LoginHelper;

/**
 * Created by ranhuo on 15/11/24.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText username_edit;
    private EditText password_edit;
    private Button login_btn;
    private Button signup;
    private ProgressDialog progressDialog;
    //private AVUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initial();
        login_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    private void initial() {
        username_edit = (EditText) findViewById(R.id.login_edit_username);
        password_edit = (EditText) findViewById(R.id.login_edit_password);
        login_btn = (Button) findViewById(R.id.btn_Login);
        signup = (Button) findViewById(R.id.btn_start_signup);
        LoginHelper.user = new AVUser();
        AVUser.logOut();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Login:
                String username = username_edit.getText().toString();
                String password = password_edit.getText().toString();
                if(username.isEmpty()){
                    showUserNameERR();
                    return;
                }
                if(password.isEmpty()){
                    showPassWordERR();
                    return;
                }
                progressDialogShow();
                AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(avUser != null){
                            Log.v("hr", "Login successfully");
                            progressDialogDismiss();
                            //LoginHelper.user = AVUser.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Home_FragmentActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.v("hr", "Login failed");
                            progressDialogDismiss();
                            showLoginError();

                        }
                    }
                });
                break;
            case R.id.btn_start_signup:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void showUserNameERR(){
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("User Name should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
    public void showPassWordERR(){
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("Password should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void showLoginError() {
        new AlertDialog.Builder(this)
                .setTitle(
                        this.getResources().getString(
                                R.string.error_title))
                .setMessage(
                        this.getResources().getString(
                                R.string.error_hint))
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
