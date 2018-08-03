package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kuoky.myapplication.model.Stock;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.model.User;
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

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private Client client;
    private Stock book = new Stock();
    private DataStore<Stock> stockDataStore;
    private Button btn;
    private Button pushBtn;
    private ProgressDialog progressDialog;
    private Button signBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client=MainActivity.getKinveyClient();
        stockDataStore=DataStore.collection("Stock",Stock.class, StoreType.SYNC,client);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String id=getIntent().getStringExtra("id");
        findStock(id);
    }

    private void findStock(String id) {
        if(id!=null){
            stockDataStore.find(id, new KinveyClientCallback<Stock>() {
                @Override
                public void onSuccess(Stock stock) {
                    UpdateActivity.this.book=stock;


                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }
    }
    private void save(){
        book.setName("Lee Foo Keong");
        showProgress("Loading");

        stockDataStore.save(book, new KinveyClientCallback<Stock>() {

            @Override
            public void onSuccess(Stock stock) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Saved Completed",Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });


    }
    public void btnClick(View v){
        save();
    }
    public void btnClick2(View v){
        sync();

    }
    public void pushClick(View v){
        push();
        showProgress("Getting data");

    }
    public void login(View v){
        showProgress("Signing");
        try{

            UserStore.login("kuokyong", "123", client, new KinveyClientCallback<com.kinvey.android.model.User>() {

                @Override
                public void onSuccess(User user) {
                    dismissProgress();


                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();

                }
            });
        }catch (IOException ex){

        }
    }
    private void pull(){

        stockDataStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void push(){
        showProgress("Pushing");
        stockDataStore.push(new KinveyPushCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse) {
                dismissProgress();

            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onProgress(long l, long l1) {

            }
        });
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
    private void sync(){
        String name=client.getActiveUser().getUsername();

        showProgress("Syncing");
        stockDataStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {
                dismissProgress();
                String a="KOng";
                Toast.makeText(getApplicationContext(),"Sync Completed",Toast.LENGTH_SHORT);
            }

            @Override
            public void onPullStarted() {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Pull Started",Toast.LENGTH_SHORT);
            }

            @Override
            public void onPushStarted() {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Push Started",Toast.LENGTH_SHORT);

            }

            @Override
            public void onPullSuccess(KinveyPullResponse kinveyPullResponse) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Pull Success",Toast.LENGTH_SHORT);
            }

            @Override
            public void onPushSuccess(KinveyPushResponse kinveyPushResponse) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"on push success",Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT);

            }
        });
    }
    private void getData() {
        stockDataStore.find(new KinveyReadCallback<Stock>() {
            @Override
            public void onSuccess(KinveyReadResponse<Stock> kinveyReadResponse) {
                dismissProgress();
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }
}
