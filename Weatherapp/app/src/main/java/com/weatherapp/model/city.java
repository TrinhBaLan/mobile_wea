package com.weatherapp.model;

public class city {
    String name;

    // Latitude and Longitude
    GPS_coordinate gps;
    String LocationKey;
    public city(String name, GPS_coordinate gps, String locationKey) {
        this.name = name;
        this.gps = gps;
        LocationKey = locationKey;
    }
    public city(GPS_coordinate gps){
        this.gps = gps;
    }

    public String getName() {
        return name;
    }

    public GPS_coordinate getGps() {
        return gps;
    }

    public String getLocationKey() {
        return LocationKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGps(GPS_coordinate gps) {
        this.gps = gps;
    }

    public void setLocationKey(String locationKey) {
        LocationKey = locationKey;
    }
}
