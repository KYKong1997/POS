package com.example.kuoky.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kuoky.myapplication.*;

public class Cancellation extends AppCompatActivity {
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);
    }
    public void confirmBtnOnClick(View v){
        Intent intent = new Intent(this, CancellationActivity.class);
        text = (EditText)findViewById(R.id.editText);
        String invoiceId= text.getText().toString();
        intent.putExtra("editText",invoiceId);
        startActivity(intent);
    }
    public void exitBtnOnClick(View v){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
