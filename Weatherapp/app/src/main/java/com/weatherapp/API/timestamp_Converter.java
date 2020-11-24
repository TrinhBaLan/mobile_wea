package com.weatherapp.API;

import android.location.Geocoder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class timestamp_Converter {
    public static String convert(Long unix_timestamp, int mode){

        Date date = new java.util.Date(unix_timestamp*1000L);
        SimpleDateFormat sdf = null;
        if (mode == 1)  sdf = new java.text.SimpleDateFormat("dd-MM-yyyy"); // convert thoi gian de hien thi binh thuong
        else if(mode == 2)  sdf = new java.text.SimpleDateFormat("hh:mm a"); // convert thoi gian de hien thi thoi gian mat troi moc, lan
        else if (mode == 3) sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        else if (mode == 4) sdf = new java.text.SimpleDateFormat("dd-MM");




        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss z");
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm:ss z")

        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


}
