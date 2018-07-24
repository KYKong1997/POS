package com.example.kuoky.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kuoky.myapplication.model.Staff;
import com.kinvey.android.Client;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.store.StoreType;

public class CuztomizeStaff extends AppCompatActivity {

    private Client client;
    private DataStore<Staff> staffDataStore;
    private Staff tempStaff=new Staff();

    private EditText nameEditText;
    private EditText icNoEditText;
    private EditText emailEditText;
    private EditText salaryEditText;
    private EditText addressEditText;
    private Spinner positionSpinner;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuztomize_staff);

        client=MainActivity.getKinveyClient();
        staffDataStore=DataStore.collection("Staff",Staff.class, StoreType.SYNC,client);

        nameEditText=(EditText)findViewById(R.id.nameEditTextStaff);
        icNoEditText=(EditText)findViewById(R.id.icNoEditTextStaff);
        emailEditText=(EditText)findViewById(R.id.emailEditTextStaff);
        salaryEditText=(EditText)findViewById(R.id.salaryEditTextStaff);
        addressEditText=(EditText)findViewById(R.id.addressEditTextStaff);
        positionSpinner=(Spinner)findViewById(R.id.positionSpinner);

        getUserData();


    }

    private void updateViewComponent() {
        nameEditText.setText(tempStaff.getUsername());
        icNoEditText.setText(tempStaff.getIC_NO());
        emailEditText.setText(tempStaff.getEmail());
        salaryEditText.setText(tempStaff.getSalary()+"");
        addressEditText.setText(tempStaff.getAddress());
        selectValue(positionSpinner,tempStaff.getPosition());
    }
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void getUserData(){
        showProgress("Loading");
        UserStore.retrieve(client, new KinveyClientCallback<com.kinvey.android.model.User>() {
            @Override
            public void onSuccess(com.kinvey.android.model.User user) {
                tempStaff.setUsername(user.getUsername());
                tempStaff.setIC_NO(user.get("IC_No").toString());
                tempStaff.setEmail(user.get("Email").toString());
                tempStaff.setSalary(Integer.valueOf(user.get("Salary").toString()));
                tempStaff.setAddress(user.get("Address").toString());
                tempStaff.setPosition(user.get("Position").toString());
                updateViewComponent();
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
    public void updateUser(View v){
        client.getActiveUser().put("Salary",Integer.valueOf(salaryEditText.getText().toString()));
        client.getActiveUser().put("Position",positionSpinner.getSelectedItem().toString());
        client.getActiveUser().put("Address",addressEditText.getText().toString());
        client.getActiveUser().put("Email",emailEditText.getText().toString());
        client.getActiveUser().put("IC_No",icNoEditText.getText().toString());
        client.getActiveUser().put("username",nameEditText.getText().toString());
        showProgress("Updating");
        client.getActiveUser().update(new KinveyClientCallback() {
            @Override
            public void onSuccess(Object o) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
