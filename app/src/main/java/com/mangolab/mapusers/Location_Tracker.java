package com.mangolab.mapusers;

/**
 * Created by Tamim on 8/8/2017.
 */

public class Location_Tracker {

    private Double latitude;
    private Double longitude;

    public Location_Tracker() {
    }

    public Location_Tracker(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
