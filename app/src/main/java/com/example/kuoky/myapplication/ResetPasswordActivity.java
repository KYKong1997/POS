package com.example.kuoky.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserManagementCallback;
import com.kinvey.android.store.UserStore;

public class ResetPasswordActivity extends AppCompatActivity {

    private Client client;
    private EditText usernameText;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        client= Common.client;
        usernameText=(EditText)findViewById(R.id.userNameText2);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void ResetPassword(View v){
        progressBar.setVisibility(View.VISIBLE);
        UserStore.resetPassword("kuokyong@live.com.my", client, new KinveyUserManagementCallback() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Reset password email is sent",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Reset password Failed",Toast.LENGTH_LONG).show();
            }
        });
    }
}
