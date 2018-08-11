package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;
import com.example.kuoky.myapplication.model.Stock;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;

public class InventoryDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] category={"Bread","Canned Food","Snacks","Drinks","Cleaning Essentials","Others"};
    private Client client;
    private Stock stock;
    private DataStore<Stock> stockDataStore;

    private EditText nameEditText;
    private EditText qtyEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText supplierEditText;
    private Spinner categorySpinner;
    private ProgressDialog progressDialog;
    private Button cancelBtn;
    private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);

        client= Common.client;

        stockDataStore=DataStore.collection("Stock",Stock.class, StoreType.SYNC,client);
        showProgress("Loading");
        sync();
        nameEditText=(EditText)findViewById(R.id.stockNameInventoryDetails);
        qtyEditText=(EditText)findViewById(R.id.stockQtyInventoryDetails);
        descriptionEditText=(EditText)findViewById(R.id.stockDescriptionInventoryDetails);
        priceEditText=(EditText)findViewById(R.id.stockPriceInventoryDetails);
        supplierEditText=(EditText)findViewById(R.id.stockSupplierInventoryDetails);
        categorySpinner=(Spinner)findViewById(R.id.categoryInventorySpinner);
        categorySpinner.setOnItemSelectedListener(this);
        saveBtn=(Button)findViewById(R.id.confirmBtnInventory);
        cancelBtn=(Button)findViewById(R.id.deleteBtnInventory);

        ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(aa);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();

        if(b.getString("btnClick").equals("addStock")){
            stock=new Stock();
            dismissProgress();
        }
        else
        {
            Gson gson=new Gson();
            JsonElement jsonElement=gson.toJsonTree(b.getSerializable("editStock"));

            stock=gson.fromJson(jsonElement,Stock.class);

            nameEditText.setText(stock.getName());
            qtyEditText.setText(Integer.valueOf(stock.getQty().intValue())+"");
            descriptionEditText.setText(stock.getDescription());
            priceEditText.setText(stock.getPrice()+"");
            supplierEditText.setText(stock.getSupplierName());
            int spinnerPosition=aa.getPosition(stock.getCategory());
            categorySpinner.setSelection(spinnerPosition);
        }


    }

    private boolean addStockValidation() {
        boolean enter=true;
        if(nameEditText.getText()==null){
            enter=false;
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        if(qtyEditText.getText()==null){
            enter=false;
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        if(descriptionEditText.getText()==null){
            enter=false;
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        if(priceEditText.getText()==null){
            enter=false;
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        if(supplierEditText.getText()==null){
            enter=false;
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        return enter;

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
        stockDataStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {
                dismissProgress();

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void sync(){

        stockDataStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, KinveyPullResponse kinveyPullResponse) {
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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void updateStock(View v){
        if(addStockValidation()==false){
            Toast.makeText(this,"All entry must be fill in",Toast.LENGTH_SHORT).show();
        }
        else{
            showProgress("Saving");
            stock.setQty(Double.valueOf(qtyEditText.getText().toString()));
            stock.setName(nameEditText.getText().toString());
            stock.setDescription(descriptionEditText.getText().toString());
            stock.setPrice(Double.valueOf(priceEditText.getText().toString()));
            stock.setSupplierName(supplierEditText.getText().toString());
            stock.setCategory(categorySpinner.getSelectedItem().toString());

            stockDataStore.save(stock, new KinveyClientCallback<Stock>() {
                @Override
                public void onSuccess(Stock stock) {
                    sync();
                    dismissProgress();
                    Toast.makeText(getApplicationContext(),"Inventory Changes Saved",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }

    }
    public void deleteStock(View v){
        stockDataStore.delete(stock.get("_id").toString(), new KinveyDeleteCallback() {
            @Override
            public void onSuccess(Integer integer) {
                onBackPressed();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}
