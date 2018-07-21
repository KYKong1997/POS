package com.example.kuoky.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuoky.myapplication.model.Stock;

import java.util.List;

public class StockAdapter extends BaseAdapter {
    private final List<Stock> stocks;
    private final Context context;

    public StockAdapter(List<Stock> stocks, Context context) {
        this.stocks = stocks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stocks.size();

    }

    @Override
    public Stock getItem(int i) {
        return stocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list_view,viewGroup,false);

        }
        TextView tv=(TextView)view;
        Stock stock=stocks.get(position);
        tv.setText(stock.getName());
        return tv;
    }
}
