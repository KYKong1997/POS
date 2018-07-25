package com.example.kuoky.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.store.StoreType;
import com.example.kuoky.myapplication.model.*;
import java.util.List;

public class CancellationReceipt extends AppCompatActivity {

    private Client mKinveyClient;
    private EditText text;
    DataStore<Invoice> dataStore;
    List<Invoice> invoices;
    private static final String TAG = "MyActivity:";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_receipt);
        text = (EditText)findViewById(R.id.editText);

        mKinveyClient = new Client.Builder("kid_SyOu6-If7", "bedda858e6604d3389b3c88fc496e8cc", this).build();

       /* UserStore.logout(mKinveyClient, new KinveyClientCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {  }
            @Override
            public void onSuccess(Void aVoid) { text.setText("Sucsess"); }
        });*/

        /*try {
            UserStore.login("foo","123",mKinveyClient, new KinveyClientCallback<User>() {
                @Override
                public void onSuccess(User user) {
                text.setText("Success");
                }

                @Override
                public void onFailure(Throwable throwable) {
                    text.setText("Fail");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mKinveyClient.ping(new KinveyPingCallback() {
            public void onFailure(Throwable t) {
                Log.e(TAG, "Kinvey Ping Failed", t);
            }
            public void onSuccess(Boolean b) {
                Log.d(TAG, "Kinvey Ping Success");
            }
        });


        dataStore = DataStore.collection("invoice", Invoice.class, StoreType.SYNC, mKinveyClient);



        Log.i(TAG, String.valueOf(mKinveyClient.getActiveUser()));
        Log.i(TAG, String.valueOf(mKinveyClient.isUserLoggedIn()));



    }
    public Client getKinveyClient(){
        return mKinveyClient;
    }
    public void clickText(View v){


        dataStore.find(new KinveyReadCallback<Invoice>() {

            @Override
            public void onSuccess(com.kinvey.java.model.KinveyReadResponse<Invoice> kinveyReadResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        Invoice book = new Invoice();
        dataStore.save(book, new KinveyClientCallback<Invoice>(){


            @Override
            public void onSuccess(Invoice invoice) {

            }

            @Override
            public void onFailure(Throwable error) {
                // Place your error handler here
            }
        });
        text.setText(book.getDate().toString());


    }
}
