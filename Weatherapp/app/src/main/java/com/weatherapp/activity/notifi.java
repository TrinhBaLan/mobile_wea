package com.weatherapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.weatherapp.API.aqi_api;
import com.weatherapp.API.converter;
import com.weatherapp.API.openWeathermapAPI;
import com.weatherapp.R;
import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.aqiDetail;
import com.weatherapp.model.weatherDetail;

import static android.content.Context.LOCATION_SERVICE;
import static com.weatherapp.activity.MainActivity.gps;
import static com.weatherapp.activity.MainActivity.locationManager;
import static com.weatherapp.activity.MainActivity.weather;

public class notifi extends BroadcastReceiver  {
    GPS_coordinate gps;
    public static weatherDetail weatherDetail;
    public static com.weatherapp.model.aqiDetail aqiDetail ;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        gps = getLocation(context);
        new get_current_weather(gps, context).execute();
        //Log.v("gps noti", gps.toString());

    }


    private class get_current_weather extends AsyncTask<String, String, weatherDetail>{
        GPS_coordinate gps;
        Context context;
        public get_current_weather(GPS_coordinate gps, Context context) {
            this.gps = gps;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected weatherDetail doInBackground(String... strings) {
            weather = openWeathermapAPI.getCurrentWeatherDetail(gps);
            aqiDetail = aqi_api.getAqiDetail(gps, "gps", null);
            return weather;
        }
        @Override
        protected void onPostExecute(weatherDetail result) {
            super.onPostExecute(result);
            weatherDetail = result;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifi");
            String icon_uri = "@drawable/icon"+ weatherDetail.getIcon();
            int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());


            builder.setSmallIcon(imageResource);

            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), imageResource));

//            builder.setContentTitle(weatherDetail.getReal_temp() + "°C tại " +weatherDetail.getCity_name());
//            builder.setContentText(converter.UppingCaseFirstCharacter(weatherDetail.getDescription()));

            builder.setContentTitle(weatherDetail.getReal_temp() + "°C | " + weatherDetail.getFeelLike_temp()+ " °C tại " +weatherDetail.getCity_name());
            builder.setContentText("Chất lượng không khí " + aqiDetail.getImpact());

            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(200, builder.build());
        }
    }


    @SuppressLint("MissingPermission")
    public GPS_coordinate getLocation(Context context) {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if( location != null) {
            Double lat, lon;
            try {
                lat = location.getLatitude();
                lon = location.getLongitude();
                gps = new GPS_coordinate(lat, lon);
                Log.v("notifi", gps.toString());
                return new GPS_coordinate(lat, lon);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{

        }
        return gps;
    }

}
