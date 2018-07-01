package com.example.kuoky.myapplication;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.kinvey.android.Client;

public class App extends MultiDexApplication{
    private Client sharedClient;
    @Override
    public void onCreate() {
        super.onCreate();

        sharedClient = new Client.Builder(this).build();
        sharedClient.enableDebugLogging();
    }
    public Client getSharedClient(){
        return sharedClient;
    }
}
