package com.mangolab.mapusers;

/**
 * Created by Tamim on 8/6/2017.
 */

public class UserData {

    private String Name;
    private String Business;
    private String Address;


    public UserData() {
    }

    public UserData(String name, String business, String address) {
        Name = name;
        Business = business;
        Address = address;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
