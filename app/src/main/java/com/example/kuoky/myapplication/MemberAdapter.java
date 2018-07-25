package com.example.kuoky.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuoky.myapplication.model.Member;
import com.example.kuoky.myapplication.model.Stock;

import java.util.List;

public class MemberAdapter extends BaseAdapter {
    private final List<Member> memberList;
    private final Context context;

    public MemberAdapter(List<Member> member, Context context) {
        this.memberList = member;
        this.context = context;
    }


    @Override
    public int getCount() {
        return memberList.size();
    }


    @Override
    public Member getItem(int i) {
        return memberList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.listview_member,viewGroup,false);

        }
        TextView tv=(TextView)view;
        Member member=memberList.get(i);
        tv.setText(member.getF_Name());
        return tv;
    }
}
