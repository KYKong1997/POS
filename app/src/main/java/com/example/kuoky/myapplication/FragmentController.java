package com.example.kuoky.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kuoky.myapplication.model.Staff;
import com.kinvey.android.Client;

import com.kinvey.android.model.User;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.Query;

import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.store.StoreType;

public class FragmentController extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    View myView;

    private Client client;
    public User userClass;

    private TextView nameEditText;

    private TextView emailText;
    private TextView salaryEditText;
    private TextView addressEditText;
    private Spinner positionSpinner;



    private ImageButton button;

    private String[] positionChoice={"Staff","Cashier","Manager"};

    ProgressDialog progressDialog;

    DataStore<Staff> staffDataStore;
    @Override
    public void onStart() {
        super.onStart();
        //saveBtn=(Button)getView().findViewById(R.id.saveBtn);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,positionChoice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapter);
        positionSpinner.setOnItemSelectedListener(this);
        button.setOnClickListener(this);

        staffDataStore.sync(new KinveySyncCallback() {
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

        Query quuery=client.query().in("Username",new String[]{"LiangWei"});












    }

    @Nullable

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client=((App)getActivity().getApplication()).getSharedClient();
        userClass=client.getActiveUser();

        staffDataStore=DataStore.collection("Staff",Staff.class,StoreType.SYNC,client);



    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {


    }

    private void showProgress(String message){

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void getData() {

        showProgress("Finding");





    }
    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
