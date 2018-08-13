package com.example.kuoky.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuoky.myapplication.model.Invoice;
import com.example.kuoky.myapplication.model.Stock;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InvoiceReportAdapter extends BaseAdapter {
    private  List<Invoice> invoices;
    private  Context context;
    private static LayoutInflater inflater=null;
    private Activity activity;

    public InvoiceReportAdapter(List<Invoice> invoices, Context context) {

        this.invoices=invoices;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return invoices.size();

    }

    @Override
    public Invoice getItem(int i) {
        return invoices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view= inflater.inflate(R.layout.report_list_view,null);


        }

        TextView staffHandle=(TextView)view.findViewById(R.id.staffhandleTextView);
        TextView memberHandle=(TextView)view.findViewById(R.id.memberhandleTextView);
        TextView itemPur=(TextView)view.findViewById(R.id.itemhandleTextView);
        TextView dateView=(TextView)view.findViewById(R.id.datehandleTextView);

        staffHandle.setText(invoices.get(position).getStaff().getUsername());
        if(invoices.get(position).getMember()!=null){
            memberHandle.setText(invoices.get(position).getMember().getL_Name()+" "+invoices.get(position).getMember().getF_Name());
        }

        dateView.setText(invoices.get(position).getDate());
        ArrayList<Stock> stocks=invoices.get(position).getStocks();

        String item="";
        for(Stock s:stocks){
            item+=s.getName()+", ";
        }
        itemPur.setText(item);
        return view;
    }
}
