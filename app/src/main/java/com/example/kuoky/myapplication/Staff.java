package com.example.kuoky.myapplication;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Staff extends GenericJson{

    @Key("Salary")
    public int Salary;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    @Key("Username")
    public String Username;

    @Key("Position")
    public String Manager;

    @Key("Address")
    public String Address;

    @Key("Email")
    public String Email;

    @Key("IC_NO")
    public String IC_NO;

    @Key("Name")
    public String Name;

    public Staff() {
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String manager) {
        Manager = manager;
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
