package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.example.kuoky.myapplication.Drawer.MyFirebaseInstanceIdService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static Client mKinveyClient;

    private EditText userNameText;
    private EditText passwordText;
    public ProgressBar loadingBar;
    private ImageButton signInBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mKinveyClient=Common.client;


        userNameText=findViewById(R.id.userNameText);
        passwordText=findViewById(R.id.passwordText);
        loadingBar=findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.INVISIBLE);
        signInBtn=findViewById(R.id.imageButton);
        User user=mKinveyClient.getActiveUser();

        FirebaseMessaging.getInstance().subscribeToTopic("news").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });


        if(user!=null){

            Intent intent=new Intent(this,MainMenu.class);
            startActivity(intent);

        }

    }
    public static Client getKinveyClient(){
        return mKinveyClient;
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void signInClick(View v){
        try{
            loadingBar.setVisibility(View.VISIBLE);
            signInBtn.setVisibility(View.INVISIBLE);
            UserStore.login(userNameText.getText().toString(), passwordText.getText().toString(), mKinveyClient, new KinveyClientCallback<User>() {

                @Override
                public void onSuccess(User user) {
                    Common.user=user;
                    loadingBar.setVisibility(View.INVISIBLE);
                    DrawerUtil.usernname=user.getUsername();
                    DrawerUtil.userEmail=user.get("email").toString();
                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),MainMenu.class);
                    startActivity(intent);


                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            });



        }catch (IOException ex){

        }
    }
    public void signOut(View v){
        UserStore.logout(mKinveyClient, new KinveyClientCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void resetPassword(View v){
        Intent intent=new Intent(getApplicationContext(),ResetPasswordActivity.class);
        startActivity(intent);
    }
    private void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
