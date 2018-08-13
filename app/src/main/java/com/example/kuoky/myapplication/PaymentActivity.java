package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.Drawer.DrawerUtil;
import com.example.kuoky.myapplication.model.Invoice;
import com.example.kuoky.myapplication.model.Member;
import com.example.kuoky.myapplication.model.Staff;
import com.example.kuoky.myapplication.model.Stock;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.android.model.User;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private StockAdapter adapter;
    private ArrayList orderItem=new ArrayList<>();
    private ArrayList<Stock> orderListItem=new ArrayList<Stock>();
    private ListView orderListView;
    private EditText memberCardEditText;
    private TextView memberNameTextView;
    private TextView totalTextView;
    private TextView payTextView;
    private TextView changeTextView;

    private double totalAmt;
    private double payAmt;

    private Client client;
    private DataStore<Invoice> invoiceStore;
    private DataStore<Stock> stockStore;
    private DataStore<Staff> staffStore;
    private DataStore<Member> memberDataStore;
    private Toolbar toolbarPayment;

    private Member member;
    private Staff staffLogIn=new Staff();
    private ProgressDialog progressDialog;

    private User user;

    private List<Stock> listInDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent=getIntent();

        Bundle b=intent.getExtras();
        orderItem=(ArrayList<Stock>)b.getSerializable("orderItem");
        totalAmt=intent.getDoubleExtra("totalAmount",0);
        payAmt=intent.getDoubleExtra("payAmount",0);

        Gson gson=new Gson();

        for(int i=0;i<orderItem.size();i++){
            JsonElement jsonElement=gson.toJsonTree(orderItem.get(i));

            Stock e=gson.fromJson(jsonElement,Stock.class);
            orderListItem.add(e);

        }


        orderListView=(ListView)findViewById(R.id.orderedItemListView);
        memberCardEditText=(EditText)findViewById(R.id.memberCardEditText);
        memberNameTextView=(TextView)findViewById(R.id.memberNameTextView);
        totalTextView=(TextView)findViewById(R.id.totalAmtTextViewInPayment);
        payTextView=(TextView)findViewById(R.id.payAmtTextView);
        changeTextView=(TextView)findViewById(R.id.changeAmtTextView);
        toolbarPayment=(Toolbar)findViewById(R.id.toolbarPayment);
        toolbarPayment.setTitle("Payment");
        DrawerUtil.getDrawer(this,toolbarPayment);

        totalTextView.setText(totalAmt+"");
        payTextView.setText(payAmt+"");
        changeTextView.setText(String.format("%.2f",(payAmt-totalAmt)));
        updateOrderAdapter(orderListItem);

        client= Common.client;
        stockStore=DataStore.collection("Stock",Stock.class, StoreType.SYNC,client);
        invoiceStore=DataStore.collection("Invoice",Invoice.class,StoreType.SYNC,client);
        memberDataStore=DataStore.collection("Members",Member.class,StoreType.SYNC,client);
        staffStore=DataStore.collection("Staff",Staff.class,StoreType.SYNC,client);
        user=client.getActiveUser();


        staffLogIn.setPosition(Common.user.get("Position").toString());
        staffLogIn.setAddress(Common.user.get("Address").toString());
        staffLogIn.setEmail(Common.user.get("email").toString());
        staffLogIn.setUsername(Common.user.getUsername());
        staffLogIn.setSalary(Integer.valueOf(Common.user.get("Salary").toString()));
        pullMember();
    }
    private void updateOrderAdapter(ArrayList<Stock> orderStocks){
        if(orderStocks==null){
            orderStocks=new ArrayList<Stock>();
        }

        adapter=new StockAdapter(orderStocks,PaymentActivity.this);
        orderListView.setAdapter(adapter);

    }
    public void searchMember(View v){
        showProgress("Searching");
        String cardNo=memberCardEditText.getText().toString();
        if(cardNo!=""){
            Query query=client.query().in("M_Card_No",new String[]{cardNo});
            memberDataStore.find(query, new KinveyReadCallback<Member>() {
                @Override
                public void onSuccess(KinveyReadResponse<Member> kinveyReadResponse) {
                   member=kinveyReadResponse.getResult().get(0);
                    memberNameTextView.setText(member.getF_Name()+"");
                    dismissProgress();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    dismissProgress();
                }
            });
        }
        else {
            Toast.makeText(this,"Card Number cannot empty",Toast.LENGTH_SHORT).show();
        }



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
    private void pullMember(){
        staffStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {
                int a=kinveyPullResponse.getCount();
                String c=kinveyPullResponse.toString();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        memberDataStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {
                int a=kinveyPullResponse.getCount();
                String c=kinveyPullResponse.toString();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        stockStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private int calculateItem(String name){
        int i=0;
        for(Stock s:orderListItem){
            if(s.getName().equals(name)){
                i++;
            }
        }
        return i;

    }
    public void confirmPayment(View v){
        ArrayList<String> stockName=new ArrayList<String>();
        stockName.add(orderListItem.get(0).getName());
        for(int i=1;i<orderListItem.size();i++){
            if(!stockName.contains(orderListItem.get(i).getName())){
                stockName.add(orderListItem.get(i).getName());
            }
        }
       String[] a=new String[stockName.size()];
        a=stockName.toArray(a);

        Query query=client.query().in("Name",a);
        stockStore.find(query, new KinveyReadCallback<Stock>() {
            @Override
            public void onSuccess(KinveyReadResponse<Stock> kinveyReadResponse) {
                listInDatabase=kinveyReadResponse.getResult();
                for(int i=0;i<listInDatabase.size();i++){
                    int numQty=calculateItem(listInDatabase.get(i).getName());
                    listInDatabase.get(i).setQty(listInDatabase.get(i).getQty()-numQty);
                }
                updateKinvey();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateKinvey() {
        Invoice invoice=new Invoice();

        invoice.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        invoice.setTotal_Amount(totalAmt);
        invoice.setStaff(staffLogIn);
        invoice.setMember(member);
        invoice.setStocks(new ArrayList<Stock>(listInDatabase));
        invoiceStore.save(invoice, new KinveyClientCallback<Invoice>() {
            @Override
            public void onSuccess(Invoice invoice) {
                Toast.makeText(getApplicationContext(),"Invoice saved Complete",Toast.LENGTH_SHORT).show();
                for(Stock e:listInDatabase){
                    stockStore.save(e, new KinveyClientCallback<Stock>() {
                        @Override
                        public void onSuccess(Stock stock) {
                            String a=stock.getName();
                            double q=stock.getQty();
                            sync();


                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
                push();
                Intent intent=new Intent(getApplicationContext(),PosActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
    private void sync(){

        stockStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {


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
        invoiceStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {

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

}
