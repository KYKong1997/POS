package com.example.kuoky.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.AsyncUserDiscovery;
import com.kinvey.android.AsyncUserGroup;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;

import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.android.callback.KinveyUserListCallback;
import com.kinvey.android.model.User;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.android.sync.KinveyPushCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;

import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.UserLookup;
import com.kinvey.java.store.StoreType;

import java.io.IOException;
import java.util.List;

public class FragmentController extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    View myView;

    private Client client;
    public User userClass;

    private TextView nameEditText;

    private TextView emailText;
    private TextView salaryEditText;
    private TextView addressEditText;
    private Spinner positionSpinner;
    DataStore<Book> dataStore;


    private Button button;

    private String[] positionChoice={"Staff","Cashier","Manager"};

    ProgressDialog progressDialog;

    com.example.kuoky.myapplication.User userCreated=new com.example.kuoky.myapplication.User();
    @Override
    public void onStart() {
        super.onStart();
        emailText=(TextView)getView().findViewById(R.id.emailTextView);
        nameEditText=(TextView) getView().findViewById(R.id.usernameTextView);


        salaryEditText=(TextView)getView().findViewById(R.id.salaryTextView);
        addressEditText=(TextView) getView().findViewById(R.id.addressTextView);
        positionSpinner=(Spinner)getView().findViewById(R.id.positionSpinner);
        //saveBtn=(Button)getView().findViewById(R.id.saveBtn);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,positionChoice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapter);
        positionSpinner.setOnItemSelectedListener(this);
        button=(Button)getView().findViewById(R.id.button);
        button.setOnClickListener(this);









    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.first_layout,container,false);


        return myView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client=((App)getActivity().getApplication()).getSharedClient();
        userClass=client.getActiveUser();
        dataStore=DataStore.collection("Book",Book.class,StoreType.SYNC,client);




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
        showProgress("Finding");
        dataStore.sync(new KinveySyncCallback() {
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

    private void showProgress(String message){

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void getData() {

        UserStore.convenience(client, new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {
                String a=user.get("Salary").toString();

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });


        UserStore.retrieve(new String[]{"Salary","Address","email","IC_NO","Position"}, client, new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(final User user) {
                userClass = user;

                userCreated.setPosition("Chasier");
                userCreated.setId(user.getId());
                userCreated.setSalary(5000);
                userCreated.setAddress(user.get("Address").toString());
                userCreated.setUsername(user.getUsername());
                userCreated.setEmail(user.get("email").toString());

                nameEditText.setText(user.getUsername());

                emailText.setText(user.get("email").toString());
                salaryEditText.setText(user.get("Salary").toString());
                addressEditText.setText(userCreated.getAddress());
                int pos = getIndex(positionSpinner, user.get("Position").toString());
                positionSpinner.setSelection(pos);




                AsyncUserGroup users = client.userGroup();

                userCreated.set("_kmd",user.get("_kmd"));
                userCreated.set("_acl",user.get("_acl"));

                userClass.setUsername("Fuck");
                userCreated.setIC_NO(user.get("IC_NO").toString());
                client.setActiveUser(userClass);
                userCreated.update(new KinveyClientCallback<com.example.kuoky.myapplication.User>() {
                    @Override
                    public void onSuccess(com.example.kuoky.myapplication.User o) {
                        String c=o.toString();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });



    }
    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
