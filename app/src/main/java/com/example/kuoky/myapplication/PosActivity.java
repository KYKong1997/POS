package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuoky.myapplication.model.Stock;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
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

public class PosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        stockList=(ListView)findViewById(R.id.stockListView);
        orderListView=(ListView)findViewById(R.id.orderListView);
        btnPay=(Button)findViewById(R.id.payBtn);
        totalAmtTextView=(TextView)findViewById(R.id.totalAmtTextView);
        totalPay=(EditText)findViewById(R.id.payAmtEditText);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @Override
    protected void onResume() {
        super.onResume();
        pull();
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

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a=position;
        Stock stockfind=adapter.getItem(position);

        if(stockfind.getQty().intValue()>0){
            orderStocks.add(stockfind);
            stockfind.setQty(new Double(stockfind.getQty().intValue()-1));
            totalAmt+=stockfind.getPrice().doubleValue();
            totalAmtTextView.setText(""+totalAmt);
            updateOrderAdapter(orderStocks);
        }
        else {
            Toast.makeText(this,"Quantity not enough",Toast.LENGTH_SHORT).show();
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
            if(payAmount-totalAmt>=0){
                startActivity(intent);
            }
            else {
                Toast.makeText(this,"No enough Money",Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, Cancellation.class);
        startActivity(intent);
    }

}
