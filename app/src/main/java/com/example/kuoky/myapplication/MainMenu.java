package com.example.kuoky.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Client client;
    Fragment fragment;
    DrawerLayout drawer;
    private TextView textView;
    private Button btn;
    private Toolbar toolbarMainMenu;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbarMainMenu=(Toolbar)findViewById(R.id.toolbarMainMenu);
        toolbarMainMenu.setTitle("Main Menu");
       client= Common.client;


        DrawerUtil.getDrawer(this,toolbarMainMenu);

       //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btn=(Button)findViewById(R.id.memberMgmtBtn);

        if(!Common.user.get("Position").equals("Manager")){
            btn.setVisibility(View.INVISIBLE);
        }


    }
    public void startDrawer(){
        DrawerUtil.getDrawer(this ,toolbarMainMenu);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

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

    public void btn3Onclick(View v){
        Intent intent=new Intent(this,PosActivity.class);
        startActivity(intent);
    }

    public void staffBtnOnClick(View v) {
        Intent intent = new Intent(this, StaffMenu.class);
        startActivity(intent);
    }
    public void memBtnOnClick(View v) {
        Intent intent = new Intent(this, memMgmt.class);
        startActivity(intent);
    }
    public void inventoryBtn(View v){
        Intent intent=new Intent(this,Inventory.class);
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
