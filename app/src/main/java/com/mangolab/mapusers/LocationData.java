package com.mangolab.mapusers;

/**
 * Created by Tamim on 8/18/2017.
 */

public class LocationData {

    private String latitude;
    private String longitude;
    private String uid;

    public LocationData() {
    }

    public LocationData(String latitude, String longitude, String uid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
