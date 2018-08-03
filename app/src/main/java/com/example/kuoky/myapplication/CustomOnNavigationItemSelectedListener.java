package com.example.kuoky.myapplication;

import android.app.FragmentManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.kinvey.android.Client;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

public class CustomOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private Activity activity;

    public CustomOnNavigationItemSelectedListener(Context context) {
        this.context = context;
        this.activity = (Activity) this.context;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_pos) {
            // Handle the camera action
        } else if (id == R.id.nav_inventory) {
            FragmentController fragmentController = new FragmentController();
            FragmentManager manager = activity.getFragmentManager();
            manager.beginTransaction().replace(R.id.drawer_layout, fragmentController).commit();
        } else if(id==R.id.nav_logout){
            Client client=MainActivity.getKinveyClient();
            UserStore.logout(client, new KinveyClientCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    activity.finishAffinity();
                    Intent intent=new Intent(context,MainActivity.class);
                    activity.startActivity(intent);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });

        }

        return true;
    }
}
