package com.example.kuoky.myapplication.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.util.ArrayList;

public class Invoice extends GenericJson {
    @Key
    private Staff Staff;
    @Key
    private Member Member;
    @Key
    private Double Total_Amount;
    @Key
    private String Date;

    public ArrayList<Stock> getStocks() {
        return Stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        Stocks = stocks;
    }

    @Key
    private ArrayList<Stock> Stocks;

    public Invoice() {
    }


    public Staff getStaff() {
        return Staff;
    }

    public void setStaff(Staff staff) {
        this.Staff = staff;
    }

    public Member getMember() {
        return Member;
    }

    public void setMember(Member member) {
        this.Member = member;
    }

    public double getTotal_Amount() {
        return Total_Amount.doubleValue();
    }

    public void setTotal_Amount(Double total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
