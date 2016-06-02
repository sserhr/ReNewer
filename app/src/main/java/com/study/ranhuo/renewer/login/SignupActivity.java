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
import com.avos.avoscloud.SignUpCallback;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.home.Home_FragmentActivity;

public class SignupActivity extends Activity implements View.OnClickListener {

    private EditText username_edit;
    private EditText password_edit;
    private EditText emailAddress_edit;
    private Button signUp_btn;
    private AVUser user;
    private ProgressDialog progressDialog;
    private Home_FragmentActivity home_fragmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initial();
        signUp_btn.setOnClickListener(this);


    }

    public void initial() {

        username_edit = (EditText) findViewById(R.id.signup_edit_username);
        password_edit = (EditText) findViewById(R.id.signup_edit_password);
        emailAddress_edit = (EditText) findViewById(R.id.signup_edit_emailAddress);
        signUp_btn = (Button) findViewById(R.id.signup_btn_signUp);

        user = new AVUser();
        AVUser.logOut();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_btn_signUp:

                if (username_edit.getText().toString().isEmpty()) {
                    showUserNameERR();
                    return;
                }
                if (password_edit.getText().toString().isEmpty()) {
                    showPassWordERR();
                    return;
                }
                if (emailAddress_edit.getText().toString().isEmpty()) {
                    showEmailERR();
                    return;
                }
                user.setUsername(username_edit.getText().toString());
                //Log.v("hr", "username" + username_edit.getText().toString());
                user.setPassword(password_edit.getText().toString());
                user.setEmail(emailAddress_edit.getText().toString());
                progressDialogShow();
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.v("hr", "signUp success");
                            progressDialogDismiss();
                            Toast.makeText(SignupActivity.this, "Singup successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, Home_FragmentActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.v("hr", "signUp failed");
                            showSignupError();
                            progressDialogDismiss();
                        }
                    }
                });
                break;
        }
    }

    public void showUserNameERR() {
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("User Name should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public void showPassWordERR() {
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("Password should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public void showEmailERR() {
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage("E-mail should not be Empty!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void showSignupError() {
        new AlertDialog.Builder(this)
                .setTitle(
                        this.getResources().getString(
                                R.string.error_title))
                .setMessage("SignUp failed, please check username, password, email, or network connection")
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
                        this.getResources().getText(R.string.dialog_text_wait), true, false);

    }
}
