package com.example.kuoky.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.example.kuoky.myapplication.model.Member;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.android.sync.KinveyPullCallback;
import com.kinvey.android.sync.KinveyPushResponse;
import com.kinvey.android.sync.KinveySyncCallback;
import com.kinvey.java.model.KinveyPullResponse;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;
import java.util.List;

public class memMgmt extends AppCompatActivity  {

    private ListView memberList;
    private Client client;
    DataStore<Member> memStore;
    private MemberAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_mgmt);
        memberList=(ListView)findViewById(R.id.listView);
        client=MainActivity.getKinveyClient();
        memStore=DataStore.collection("Members",Member.class, StoreType.SYNC,client);
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
    private void sync(){
        memStore.sync(new KinveySyncCallback() {
            @Override
            public void onSuccess(KinveyPushResponse kinveyPushResponse, final KinveyPullResponse kinveyPullResponse) {
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
    private void printTable(List<Member> m){
        if(m==null){
            m=new ArrayList<Member>();
        }
        adapter=new MemberAdapter(m,memMgmt.this);

      memberList.setAdapter(adapter);

    }
    private void getData(){
        memStore.find(new KinveyReadCallback<Member>() {
            @Override
            public void onSuccess(KinveyReadResponse<Member> kinveyReadResponse) {
                printTable(kinveyReadResponse.getResult());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void pull(){

        memStore.pull(new KinveyPullCallback() {
            @Override
            public void onSuccess(KinveyPullResponse kinveyPullResponse) {

                getData();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


}
