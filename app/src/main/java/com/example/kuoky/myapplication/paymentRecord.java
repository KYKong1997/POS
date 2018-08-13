package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.model.Invoice;
import com.example.kuoky.myapplication.model.Stock;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;

public class paymentRecord extends AppCompatActivity {

    private Client client;
    private ArrayList<Invoice> invoices;
    private InvoiceReportAdapter invoiceReportAdapter;
    private DataStore<Invoice> invoiceDataStore;

    private ListView paymentListView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_record);
        client= Common.client;
        paymentListView=(ListView)findViewById(R.id.paymentListView);
        invoiceDataStore=DataStore.collection("Invoice",Invoice.class, StoreType.CACHE,client);
        showProgress("Loading");
        sync();
        if(invoices==null){
            invoices=new ArrayList<Invoice>();
        }




    }
    private void sync(){
        invoiceDataStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {
               getReport();


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
    public void getReport(){
        invoiceDataStore.find(new KinveyReadCallback<Invoice>() {
            @Override
            public void onSuccess(KinveyReadResponse<Invoice> kinveyReadResponse) {
                invoices=new ArrayList<Invoice>(kinveyReadResponse.getResult());
                invoiceReportAdapter=new InvoiceReportAdapter(invoices,paymentRecord.this);
                paymentListView.setAdapter(invoiceReportAdapter);
                dismissProgress();

            }

            @Override
            public void onFailure(Throwable throwable) {

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
}
