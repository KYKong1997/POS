package com.example.kuoky.myapplication.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Staff extends GenericJson{

    @Key
    private Integer Salary;
    @Key
    private String Username;

    @Key
    private String Position;

    @Key
    private String Address;

    @Key
    private String Email;

    @Key
    private String IC_NO;

    @Key
    private String Name;

    public Staff() {
    }
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Integer getSalary() {
        return Salary;
    }

    public void setSalary(Integer salary) {
        Salary = salary;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIC_NO() {
        return IC_NO;
    }

    public void setIC_NO(String IC_NO) {
        this.IC_NO = IC_NO;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
