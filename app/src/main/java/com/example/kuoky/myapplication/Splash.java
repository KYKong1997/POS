package com.example.kuoky.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 50;
    private Client client;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        client=new Client.Builder("kid_BJIst7Fbm","a50a0c9766714a6e9a123bc3f2c68e74",this).build();
        Common.client=client;

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
               mainIntent = new Intent(Splash.this,MainActivity.class);
                UserStore.retrieve(client, new KinveyClientCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Common.user=user;

                        DrawerUtil.userEmail=user.get("email").toString();
                        DrawerUtil.usernname=user.getUsername();
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                    }
                });

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
