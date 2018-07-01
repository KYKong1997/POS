package com.example.kuoky.myapplication;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class User extends com.kinvey.android.model.User{
    @Key
    public String Email;
    @Key
    public int Salary;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
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

    public String getIC_NO() {
        return IC_NO;
    }

    public void setIC_NO(String IC_NO) {
        this.IC_NO = IC_NO;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    @Key("Position")
    public String Position;
    @Key("Address")
    public String Address;
    @Key("IC_NO")
    public String IC_NO;
    @Key("username")
    public String Username;
}
