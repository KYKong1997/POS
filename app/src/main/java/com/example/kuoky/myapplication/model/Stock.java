package com.example.kuoky.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.Serializable;

public class Stock extends GenericJson implements Serializable{
    @Key
    private String SupplierName;

    @Key
    private String Name;

    @Key
    private String Category;

    @Key
    private Double Price;

    @Key
    private String Description;

    @Key
    private Double Qty;





    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Double getPrice() {
        return Price;
    }

    public Stock() {
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    public Stock(String name) {
        Name = name;
    }



}
