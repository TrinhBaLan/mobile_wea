package com.weatherapp.API;

import android.util.Log;

import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.weatherDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class accuWeatherAPI {
    // xu ly de lay duoc json
    public static String getLocationKey(GPS_coordinate gps) {
        HttpURLConnection httpCon = null;
        URL url = null;
        String key = "";
        String url_string = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=GjgLoASBfGdG7bW9SCbbmL4SeNQiA9aH&q=" +gps.getLat()+ "%2C"+ gps.getLon();
        try {
            Log.v("url", url_string);
            url = new URL(url_string);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setUseCaches(false);
            httpCon.setReadTimeout(15 * 1000); // 15 seconds to timeout
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
        } catch (Exception e){
            e.printStackTrace();
        }

        if (httpCon != null){
            try {
                httpCon.setRequestMethod("GET");
                httpCon.setDoInput(true);
                httpCon.connect();
                Log.v("getLocationKey", "GET REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String result = "";
                String line = null;
                while ((line = reader.readLine()) != null)
                    result += line;
                //stringBuilder.append(line);

                reader.close();
                //Log.v("donate", "JSON GET REQUEST : " + stringBuilder.toString());
                Log.v("getLocationKey", result); // result day la json
                key = json_to_locationKey(result);
                Log.v("getLocationKey", key);
                return key;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
    private static String json_to_locationKey(String s){
        String result = null;
        try {
            JSONObject main_json = new JSONObject(s);
            result = main_json.getString("Key");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static weatherDetail[] get_5days_weather(String location_key){
        // code
        return null;
    }

    public static weatherDetail get_1hour_weather(String location_key){
        //code
        return null;
    }

    public static weatherDetail get_12hours_weather(String location_key){
        //code
        return null;
    }

}
