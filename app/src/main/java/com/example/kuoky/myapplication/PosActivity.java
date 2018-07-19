package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;
import java.util.List;

public class PosActivity extends AppCompatActivity {
    public ListView stockList;
    ArrayList<Stock> stocks;
    DataStore<Stock> stockStore;
    private Client client;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        stockList=(ListView)findViewById(R.id.stockListView);
        client=MainActivity.getKinveyClient();
        showProgress("Loading");
        stockStore=DataStore.collection("Stock",Stock.class, StoreType.SYNC,client);
        final SwipeRefreshLayout pullToRefresh=findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sync();
                pullToRefresh.setRefreshing(false);
            }
        });
        pull();

    }

    private void sync(){
        showProgress("Syncing");
        stockStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {
                getData();
                dismissProgress();
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
        ArrayList<String> stockName=new ArrayList<String>();
        for(Stock a:stocks){
            stockName.add(a.getName());
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_view,stockName);
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

            }
        });
    }
}
