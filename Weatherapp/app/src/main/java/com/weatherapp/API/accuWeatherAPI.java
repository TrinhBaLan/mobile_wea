package com.weatherapp.API;

import android.util.Log;

import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.weatherDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class accuWeatherAPI {

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    // xu ly de lay duoc json
    public static String getJson(String cityname) throws UnsupportedEncodingException {
        HttpURLConnection httpCon = null;
        URL url = null;
        String key = "";
        String url_string = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=GjgLoASBfGdG7bW9SCbbmL4SeNQiA9aH&q="
                + removeAccent(cityname.replace(" ", "%20")) ;
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
                int status = httpCon.getResponseCode();
                if( status == 503) return null;
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
                return  result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public static GPS_coordinate getGPS_by_cityname(String cityname)  {
        GPS_coordinate gps_coordinate = null;
        String json = null;
        try {
            json = getJson(cityname);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if( json == null ) return new GPS_coordinate(999.99,999.99);
        if( json.equals("[]") ) return null;

        try {
            Log.v("api accu", json);
            JSONArray main = new JSONArray(json);
            JSONObject geo = main.getJSONObject(0).getJSONObject("GeoPosition");
            double lat = geo.getDouble("Latitude");
            double lon = geo.getDouble("Longitude");
            gps_coordinate = new GPS_coordinate(lat,lon);
            return gps_coordinate;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gps_coordinate;
    }


}
