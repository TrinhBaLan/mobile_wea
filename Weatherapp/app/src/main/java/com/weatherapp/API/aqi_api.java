package com.weatherapp.API;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.aqiDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class aqi_api {
    String key = "622b13abb1c3cf035037a346c0c5778e674c24c7";

    public static String get_jsonString(GPS_coordinate gps, String value){
        String url_str = "";
        if( value.equals("gps")){ // lấy theo gps
            url_str = "https://api.waqi.info/feed/geo:"+gps.getLat() +";"+gps.getLon()+"/?token=622b13abb1c3cf035037a346c0c5778e674c24c7";
        }
        else{
            // value la ten thanh pho muon tim kiem
            url_str = "https://api.waqi.info/feed/"+ value +"/?token=622b13abb1c3cf035037a346c0c5778e674c24c7";
        }


        URL url = null;
        HttpURLConnection httpCon = null;

        try {
            Log.v("url", url_str);
            url = new URL(url_str);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setUseCaches(false);
            httpCon.setReadTimeout(15 * 1000); // 15 seconds to timeout
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (httpCon != null) {
            try {
                httpCon.setRequestMethod("GET");
                httpCon.setDoInput(true);
                httpCon.connect();
                Log.v("aqi", "GET REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String result = "";
                String line = null;
                while ((line = reader.readLine()) != null)
                    result += line;
                //stringBuilder.append(line);

                reader.close();
                //Log.v("donate", "JSON GET REQUEST : " + stringBuilder.toString());
                //Log.v("aqi", result); // result day la json
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static aqiDetail getAqiDetail(GPS_coordinate gps, String value, Context context){
        String city = "";
        // lay ten thanh pho dua theo gps
        if( context != null) {
            Geocoder gcd = new Geocoder(context);
            try {
                List<Address> list = gcd.getFromLocation(gps.getLat(), gps.getLon(), 2);
                //List<Address> list = gcd.getFromLocation(21.028511, 105.804817, 2);
                city = list.get(0).getAdminArea();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        aqiDetail result = new aqiDetail();
        String json = get_jsonString(gps, value);

        try {
            JSONObject main = new JSONObject(json);
            if( main.getString("status").equals("ok")){
                result.setCity_name(city);

                JSONObject data = main.getJSONObject("data");
                result.setAqi(data.getDouble("aqi"));
                result.setDominentpol(data.getString("dominentpol"));

                String time = data.getJSONObject("time").getString("s");
                result.setTime(time);

                JSONObject iaqi = data.getJSONObject("iaqi");

                if( iaqi.has("co"))
                    result.setCO(String.valueOf(iaqi.getJSONObject("co").getDouble("v")) );
                if( iaqi.has("dew"))
                    result.setDew(String.valueOf(iaqi.getJSONObject("dew").getDouble("v")));
                if( iaqi.has("no2"))
                    result.setNO2(String.valueOf(iaqi.getJSONObject("no2").getDouble("v")));
                if( iaqi.has("so2"))
                    result.setSO2(String.valueOf(iaqi.getJSONObject("so2").getDouble("v")));
                if( iaqi.has("o3"))
                    result.setO3(String.valueOf(iaqi.getJSONObject("o3").getDouble("v")));
                if( iaqi.has("pm10"))
                    result.setPM10(String.valueOf(iaqi.getJSONObject("pm10").getDouble("v")));
                if( iaqi.has("pm25"))
                    result.setPM25(String.valueOf(iaqi.getJSONObject("pm25").getDouble("v")));

                ArrayList<String> check = aqiDetail.check_impact(result.getAqi(),result.getPM25());

                result.setImpact(check.get(0));
                result.setDescription(check.get(1));

                //Log.v("aqi result", result.toString());
            }
            else return result; // trả về tất cả là không xác định


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}
