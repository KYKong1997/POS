package com.example.kuoky.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.example.kuoky.myapplication.model.Stock;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;
import java.util.List;

public class PosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    public ListView stockList;
    private ListView orderListView;
    private TextView totalAmtTextView;
    private StockAdapter adapter;
    private StockAdapter orderAdpter;
    ArrayList<Stock> stocks;
    ArrayList<Stock> orderStocks=new ArrayList<Stock>();
    DataStore<Stock> stockStore;
    private Client client;
    ProgressDialog progressDialog;

    private double totalAmt=0;
    private EditText totalPay;

    private Stock returnStock=new Stock();
    private Button btnPay;
    private Toolbar posToolbar;

    Fragment fragment;
    DrawerLayout drawer;
    private TextView textView;
    private Button btn;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pos);
        posToolbar=(Toolbar)findViewById(R.id.toolbarPos);

        posToolbar.setTitle("POS");


        DrawerUtil.getDrawer(this,posToolbar);

        stockList=(ListView)findViewById(R.id.stockListView);
        orderListView=(ListView)findViewById(R.id.orderListView);
        btnPay=(Button)findViewById(R.id.payBtn);
        totalAmtTextView=(TextView)findViewById(R.id.totalAmtTextView);
        totalPay=(EditText)findViewById(R.id.payAmtEditText);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        client= Common.client;
        orderListView.setOnItemClickListener(PosActivity.this);
        stockStore=DataStore.collection("Stock",Stock.class, StoreType.CACHE,client);
        showProgress("Loading");
        getData();

        pullToRefresh=findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sync();

                pullToRefresh.setRefreshing(false);
            }
        });


        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btn=(Button)findViewById(R.id.button3);

        /*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void sync(){

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
                dismissProgress();

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateStockAdapter(List<Stock> stocks) {
        if(stocks==null){
            stocks=new ArrayList<Stock>();
        }

        stockList.setOnItemClickListener(PosActivity.this);
        adapter=new StockAdapter(stocks,PosActivity.this);
        stockList.setAdapter(adapter);
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
    private void pull(){
        showProgress("Loading");
        stockStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {
                dismissProgress();
                getData();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissProgress();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a=position;


        if(parent.getAdapter()==adapter){
            Stock stockfind=adapter.getItem(position);
            if(stockfind.getQty().intValue()>0){

                orderStocks.add(stockfind);
                stockfind.setQty(new Double(stockfind.getQty().intValue()-1));
                totalAmt+=stockfind.getPrice().doubleValue();
                totalAmtTextView.setText(String.format("%.2f", totalAmt));
                updateOrderAdapter(orderStocks);
            }
            else {
                Toast.makeText(this,"Quantity not enough",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Stock stockfind=orderAdpter.getItem(position);
            orderStocks.remove(a);
            totalAmt-=stockfind.getPrice().doubleValue();
            totalAmtTextView.setText(String.format("%.2f", totalAmt));
            orderAdpter.notifyDataSetChanged();
        }



/*        Intent i=new Intent(this,UpdateActivity.class);
        i.putExtra("id",stockfind.get("_id").toString());
        startActivity(i);*/
        /*findStock(stockfind.get("_id").toString());
        returnStock.setName("Testing");
        stockfind.setName("Testing");
        stockStore.save(stockfind, new KinveyClientCallback<Stock>() {
            @Override
            public void onSuccess(Stock stock) {
                String a =stock.getName();
                Toast.makeText(getApplication(), "Save Completed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        push();*/
    }
    private void findStock(String id){

        stockStore.find(id, new KinveyClientCallback<Stock>() {
            @Override
            public void onSuccess(Stock stock) {
                returnStock=stock;
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void push(){
        stockStore.push(new KinveyPushCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onProgress(long l, long l1) {

            }
        });
    }
    public void payBtn(View V){
        try{
            double payAmount=Double.parseDouble(totalPay.getText().toString());
            Intent intent=new Intent(this,PaymentActivity.class);

            intent.putExtra("orderItem",orderStocks);
            intent.putExtra("totalAmount",totalAmt);
            intent.putExtra("payAmount",payAmount);
            if(payAmount-totalAmt>=0||orderAdpter.getCount()!=0){
                startActivity(intent);
            }
            else {
                if(payAmount-totalAmt<=0){
                    Toast.makeText(this,"No enough Money",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(this,"Please select an item",Toast.LENGTH_SHORT).show();
                }
            }

        }catch (NumberFormatException ex){
            Toast.makeText(this,"Invalid Amount",Toast.LENGTH_SHORT).show();
        }


    }
    private void updateOrderAdapter(ArrayList<Stock> orderStocks){
        if(orderStocks==null){
            orderStocks=new ArrayList<Stock>();
        }

        orderAdpter=new StockAdapter(orderStocks,PosActivity.this);
        orderListView.setAdapter(orderAdpter);
        orderAdpter.notifyDataSetChanged();
    }

    public void CancelBtnOnClick(View v){
        onBackPressed();
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

}
