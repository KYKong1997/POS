package com.example.kuoky.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kuoky.myapplication.model.Staff;
import com.kinvey.android.Client;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.store.StoreType;

public class StaffMenu extends AppCompatActivity {

    private Client client;
    private DrawerLayout drawer;
    DataStore<Staff> dataStore = DataStore.collection("staffs", Staff.class, StoreType.SYNC, client);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu);
        client=MainActivity.getKinveyClient();
        final ImageButton imgBtn = (ImageButton) findViewById(R.id.imageButton3);
        final TextView textView = (TextView) findViewById(R.id.textView3);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    imgBtn.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }else {
                    searchView.onActionViewCollapsed();
                    imgBtn.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                imgBtn.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        CustomOnNavigationItemSelectedListener NavListener = new CustomOnNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(NavListener);

    }

    public void openDrawer(View v) {
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
