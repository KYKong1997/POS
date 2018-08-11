package com.example.kuoky.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.example.kuoky.myapplication.R;
import com.example.kuoky.myapplication.model.Stock;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private Client client;
    Fragment fragment;
    DrawerLayout drawer;
    private TextView textView;
    private Button btn;
    private ListView inventoryListView;
    private ProgressDialog progressDialog;
    private Toolbar inventoryToolbar;

    private StockAdapter inventoryAdapter;
    ArrayList<Stock> inventory;
    DataStore<Stock> stockStore;

    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        client= Common.client;
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btn=(Button)findViewById(R.id.button3);

       /* drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
        inventoryToolbar=(Toolbar)findViewById(R.id.toolbarInventory);
        inventoryToolbar.setTitle("Inventory");


        DrawerUtil.getDrawer(this,inventoryToolbar);

       inventoryListView=(ListView)findViewById(R.id.inventoryListView);

        stockStore=DataStore.collection("Stock",Stock.class, StoreType.CACHE,client);
        getData();
        pullToRefresh=findViewById(R.id.pullToRefreshInventory);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sync();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void pull() {
        showProgress("Loading");
        stockStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {
                dismissProgress();
                getData();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void sync() {
        stockStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {
                getData();

            }

            @Override
            public void onPullStarted() {

            }

            @Override
            public void onPushStarted() {

            }

            @Override
            public void onPullSuccess(KinveyPullResponse kinveyPullResponse) {

            }

            @Override
            public void onPushSuccess(KinveyPushResponse kinveyPushResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getData() {
        stockStore.find(new KinveyReadCallback<Stock>() {
            @Override
            public void onSuccess(KinveyReadResponse<Stock> kinveyReadResponse) {

                updateStockAdapter(kinveyReadResponse.getResult());

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void updateStockAdapter(List<Stock> stocks) {
        if(stocks==null){
            stocks=new ArrayList<Stock>();
        }

        inventoryListView.setOnItemClickListener(Inventory.this);
        inventoryAdapter=new StockAdapter(stocks,Inventory.this);
        inventoryListView.setAdapter(inventoryAdapter);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Stock selectedStock=inventoryAdapter.getItem(i);
        Intent intent=new Intent(this,InventoryDetails.class);
        intent.putExtra("btnClick","edit");
        intent.putExtra("editStock",selectedStock);
        startActivity(intent);

    }
    public void addStockBtn(View v){
        Intent intent=new Intent(this,InventoryDetails.class);
        intent.putExtra("btnClick","addStock");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }
}
