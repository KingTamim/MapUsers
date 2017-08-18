package com.mangolab.mapusers;

/**
 * Created by Tamim on 8/6/2017.
 */

public class UserData {

    private String Name;
    private String Business;
    private String Address;
    private String UserId;
    private String Latitude;
    private String Longitude;


    public UserData() {
    }

    public UserData(String name, String business, String address, String userId){  //} String latitude, String longitude) {
        Name = name;
        Business = business;
        Address = address;
        UserId = userId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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
