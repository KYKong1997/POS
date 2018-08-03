package com.example.kuoky.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuoky.myapplication.R;
import com.kinvey.android.Client;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

public class Inventory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Client client;
    Fragment fragment;
    DrawerLayout drawer;
    private TextView textView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_menu);

        client=MainActivity.getKinveyClient();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btn=(Button)findViewById(R.id.button3);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home) {
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_pos) {
            Intent intent = new Intent(this, PosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_inventory) {
            Intent intent = new Intent(this, Inventory.class);
            startActivity(intent);
        } else if(id==R.id.nav_logout){
            UserStore.logout(client, new KinveyClientCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openDrawer(View v){
        drawer.openDrawer(Gravity.LEFT);
    }
}
