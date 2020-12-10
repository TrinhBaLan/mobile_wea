package com.weatherapp.model;

import java.io.Serializable;

public class GPS_coordinate implements Serializable {
    Double lat; // kinh do
    Double lon; // vi do

    public GPS_coordinate() {
    }

    public GPS_coordinate(double lat, double lon)  {
        this.lat = lat;
        this.lon = lon;
    }
    public String toString(){
        return "lat " + this.lat
                + "lon " + this.lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
