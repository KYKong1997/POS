package com.example.kuoky.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuoky.myapplication.Drawer.Common;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class memMgmt extends AppCompatActivity  {


    private EditText titleEditText;
    private EditText contentEditText;
    private Button sendBtn;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainMenu.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_mgmt);
        Intent intent=getIntent();
        titleEditText=(EditText)findViewById(R.id.titleNotificationeditText);
        contentEditText=(EditText)findViewById(R.id.bodyEditText);
        sendBtn=(Button)findViewById(R.id.sendBtn);

        if(intent.getStringExtra("title")!=null){
            titleEditText.setText(intent.getStringExtra("title"));
            contentEditText.setText(intent.getStringExtra("message"));
        }
        if(!Common.user.get("Position").toString().equals("Manager")){
            titleEditText.setEnabled(false);
            contentEditText.setEnabled(false);
            sendBtn.setVisibility(View.INVISIBLE);
        }





    }
    public void send(View v){

        sendNotification();

    }

    private void sendNotification() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", contentEditText.getText().toString());
                    dataJson.put("title", titleEditText.getText().toString());
                    json.put("notification", dataJson);
                    json.put("to", "/topics/news");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AIzaSyA-tExSAhBZoMZ9qIXESz_qG6f-xSYnldM")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception ex) {

                }
                return null;
            }
        }.execute();
    }




}
