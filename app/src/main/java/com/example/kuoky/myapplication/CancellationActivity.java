package com.example.kuoky.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;
import com.example.kuoky.myapplication.model.*;

import java.util.Collections;

public class CancellationActivity extends AppCompatActivity {

    private Client client;
    DataStore<Invoice> invoiceStore;
    private String invoiceId;
    private EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_activity);
        Intent intent=getIntent();
        //text =(EditText) findViewById(R.id.editText2);
        invoiceId = intent.getStringExtra("editText");
        client=MainActivity.getKinveyClient();
        invoiceStore=DataStore.collection("Invoice",Invoice.class,StoreType.SYNC,client);
        getData();
        //pull();
        //push();

        //sync();
        //delete();

    }
    public void delete(){
        invoiceStore.delete(invoiceId, new KinveyDeleteCallback() {
            @Override
            public void onSuccess(Integer integer) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void sync(){
        invoiceStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, final KinveyPullResponse kinveyPullResponse) {

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
    private void pull(){

        invoiceStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {



            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }
    private void push(){
        invoiceStore.push(new KinveyPushCallback() {
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
    private void getData(){

        invoiceStore.find( new KinveyReadCallback<Invoice>() {
            @Override
            public void onSuccess(KinveyReadResponse<Invoice> kinveyReadResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}
