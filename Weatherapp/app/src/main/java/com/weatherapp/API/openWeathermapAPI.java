package com.weatherapp.API;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.weatherDetail;

import org.json.JSONArray;
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

public class openWeathermapAPI {
    public static String getCurrent_json(GPS_coordinate gps, int mode) {
        String url_str = "";

        if(mode == 1){ // cho viec lay thoi tiet hien tai
            url_str = "http://api.openweathermap.org/data/2.5/weather?lat=" + gps.getLat() + "&lon=" + gps.getLon() + "&lang=vi&units=metric&appid=66cf474352cf7671f24a41e50c3a9db7";
        }
        else if (mode == 2){ // lay weather cho 120h tiep theo
            url_str = "http://api.openweathermap.org/data/2.5/forecast?lat=" + gps.getLat() + "&lon=" + gps.getLon() + "&lang=vi&units=metric&appid=66cf474352cf7671f24a41e50c3a9db7";
        }
        else if (mode == 3){ // lay json thoi tiet theo tung gio
            url_str = "http://api.openweathermap.org/data/2.5/onecall?lat=" + gps.getLat() + "&lon=" + gps.getLon() + "&lang=vi&units=metric&exclude=minutely,daily&appid=66cf474352cf7671f24a41e50c3a9db7";
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
                Log.v("openWeathermapAPI", "GET REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String result = "";
                String line = null;
                while ((line = reader.readLine()) != null)
                    result += line;
                //stringBuilder.append(line);

                reader.close();
                //Log.v("donate", "JSON GET REQUEST : " + stringBuilder.toString());
                Log.v("openWeathermapAPI", result); // result day la json
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static weatherDetail getCurrentWeatherDetail(GPS_coordinate gps){
        String json_str = getCurrent_json(gps,1);
        weatherDetail result = new weatherDetail();
        try {
            JSONObject main = new JSONObject(json_str);

            result.setCity_name(main.getString("name"));
            result.setIcon(main.getJSONArray("weather").getJSONObject(0).getString("icon"));
            result.setDescription(main.getJSONArray("weather").getJSONObject(0).getString("description"));
            result.setReal_temp(main.getJSONObject("main").getDouble("temp"));
            result.setTemp_max(main.getJSONObject("main").getDouble("temp_max"));
            result.setTemp_min(main.getJSONObject("main").getDouble("temp_min"));
            result.setFeelLike_temp(main.getJSONObject("main").getDouble("feels_like"));
            result.setPressure(main.getJSONObject("main").getDouble("pressure"));
            result.setHumidity(main.getJSONObject("main").getDouble("humidity"));
            result.setVisibility(main.getDouble("visibility"));
            result.setWind_speed(main.getJSONObject("wind").getDouble("speed"));
            result.setWind_direction_deg(main.getJSONObject("wind").getDouble("deg"));
            result.setWind_direction_description(openWeathermapAPI.Wind_direction_check(result.getWind_direction_deg()));
            result.setClouds(main.getJSONObject("clouds").getDouble("all"));

            Long sunrise = main.getJSONObject("sys").getLong("sunrise");
            result.setSunrise(timestamp_Converter.convert(sunrise,2));

            Long sunset = main.getJSONObject("sys").getLong("sunset");
            result.setSunset(timestamp_Converter.convert(sunset,2));

            Long time = main.getLong("dt");
            result.setTime(String.valueOf(time));

            if(main.has("pop")){
                result.setPop(main.getDouble("pop"));
            }

            if( main.has("rain")){
                if(main.getJSONObject("rain").has("3h")) {
                    result.setRain(main.getJSONObject("rain").getDouble("3h"));
                    result.setRain_time("3h");
                }
                else{
                    result.setRain(main.getJSONObject("rain").getDouble("1h"));
                    result.setRain_time("1h");
                }

                if (result.getPop() == 0) // troi dang mua nen ty le co mua la 100%
                    result.setPop(100.0);
            }
            if(main.has("snow")){
                if(main.getJSONObject("snow").has("3h")) {
                    result.setSnow(main.getJSONObject("snow").getDouble("3h"));
                    result.setSnow_time("3h");
                }
                else {
                    result.setSnow(main.getJSONObject("snow").getDouble("1h"));
                    result.setSnow_time("1h");
                }
            }

            Log.v("current weather ", result.toString());
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<weatherDetail> get_5days_3hour_weather(GPS_coordinate gps){
        ArrayList<weatherDetail> result_arr = new ArrayList<>();

        String main_json = getCurrent_json(gps,2);

        try {
            JSONArray main_list = new JSONObject(main_json).getJSONArray("list");

            Log.v("list", "has "+main_list.length() +" items");

            for (int i = 0; i< main_list.length(); i++){
                weatherDetail result = new weatherDetail();
                JSONObject main = main_list.getJSONObject(i);

                result.setCity_name(new JSONObject(main_json).getJSONObject("city").getString("name"));
                result.setIcon(main.getJSONArray("weather").getJSONObject(0).getString("icon"));
                result.setDescription(main.getJSONArray("weather").getJSONObject(0).getString("description"));
                result.setReal_temp(main.getJSONObject("main").getDouble("temp"));
                result.setTemp_max(main.getJSONObject("main").getDouble("temp_max"));
                result.setTemp_min(main.getJSONObject("main").getDouble("temp_min"));
                result.setFeelLike_temp(main.getJSONObject("main").getDouble("feels_like"));
                result.setPressure(main.getJSONObject("main").getDouble("pressure"));
                result.setHumidity(main.getJSONObject("main").getDouble("humidity"));
                result.setVisibility(main.getDouble("visibility"));
                result.setWind_speed(main.getJSONObject("wind").getDouble("speed"));
                result.setWind_direction_deg(main.getJSONObject("wind").getDouble("deg"));
                result.setWind_direction_description(openWeathermapAPI.Wind_direction_check(result.getWind_direction_deg()));
                result.setClouds(main.getJSONObject("clouds").getDouble("all"));

//                Long sunrise = main.getJSONObject("sys").getLong("sunrise");
//                result.setSunrise(timestamp_Converter.convert(sunrise,2));
//
//                Long sunset = main.getJSONObject("sys").getLong("sunset");
//                result.setSunset(timestamp_Converter.convert(sunset,2));

                Long time = main.getLong("dt");
                //result.setTime(timestamp_Converter.convert(time,3));
                result.setTime(String.valueOf(time));

                if(main.has("pop")){
                    result.setPop(main.getDouble("pop"));
                }

                if( main.has("rain")){
                    if(main.getJSONObject("rain").has("3h")) {
                        result.setRain(main.getJSONObject("rain").getDouble("3h"));
                        result.setRain_time("3h");
                    }
                    else{
                        result.setRain(main.getJSONObject("rain").getDouble("1h"));
                        result.setRain_time("1h");
                    }

                    if (result.getPop() == 0) // troi dang mua nen ty le co mua la 100%
                        result.setPop(100.0);
                }
                if(main.has("snow")){
                    if(main.getJSONObject("snow").has("3h")) {
                        result.setSnow(main.getJSONObject("snow").getDouble("3h"));
                        result.setSnow_time("3h");
                    }
                    else {
                        result.setSnow(main.getJSONObject("snow").getDouble("1h"));
                        result.setSnow_time("1h");
                    }
                }
                Log.v("5 days weather ", result.toString());
                result_arr.add(result);
            }

            return result_arr;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // lay du lieu thoi tiet theo gio
    public static List<weatherDetail> get_hourly_weather(GPS_coordinate gps, Context context) {
        List<weatherDetail> result_arr = new ArrayList<>();

        String main_json = getCurrent_json(gps, 3);

        try {
            JSONObject main_object = new JSONObject(main_json);
            JSONArray list = main_object.getJSONArray("hourly");

            for (int i = 0; i< list.length(); i++){
                JSONObject item = list.getJSONObject(i);
                Log.v("time stamp", timestamp_Converter.convert(item.getLong("dt"),2));



                String city = "";
                // lay ten thanh pho dua theo gps
                Geocoder gcd = new Geocoder(context);
                try {
                    List<Address> add =  gcd.getFromLocation(gps.getLat(),gps.getLon(), 2);
                    city = add.get(0).getAdminArea();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                weatherDetail result = new weatherDetail();
                result.setCity_name(city);
                result.setReal_temp(item.getDouble("temp"));
                result.setFeelLike_temp(item.getDouble("feels_like"));
                result.setPressure(item.getDouble("pressure"));
                result.setHumidity(item.getDouble("humidity"));
                result.setClouds(item.getDouble("clouds"));
                result.setVisibility(item.getDouble("visibility"));
                result.setWind_speed(item.getDouble("wind_speed"));
                result.setWind_direction_deg(item.getDouble("wind_deg"));
                result.setWind_direction_description(openWeathermapAPI.Wind_direction_check(result.getWind_direction_deg()));

                Long time_long = item.getLong("dt");
                result.setTime(String.valueOf(time_long));

                JSONObject weather_object = (JSONObject) item.getJSONArray("weather").get(0);
                result.setIcon(weather_object.getString("icon"));
                result.setDescription(weather_object.getString("description"));



                if(item.has("pop")){
                    result.setPop(item.getDouble("pop"));
                }

                if( item.has("rain")){
                    if(item.getJSONObject("rain").has("3h")) {
                        result.setRain(item.getJSONObject("rain").getDouble("3h"));
                        result.setRain_time("3h");
                    }
                    else if (item.getJSONObject("rain").has("1h")){
                        result.setRain(item.getJSONObject("rain").getDouble("1h"));
                        result.setRain_time("1h");
                    }
                    else {
                        result.setRain(item.getDouble("rain"));
                        result.setRain_time("1h");
                    }

                    if (result.getPop() == 0) // troi dang mua nen ty le co mua la 100%
                        result.setPop(100.0);
                }
                if(item.has("snow")){
                    if(item.getJSONObject("snow").has("3h")) {
                        result.setSnow(item.getJSONObject("snow").getDouble("3h"));
                        result.setSnow_time("3h");
                    }
                    else if (item.getJSONObject("snow").has("1h")){
                        result.setSnow(item.getJSONObject("snow").getDouble("1h"));
                        result.setSnow_time("1h");
                    }
                    else {
                        result.setSnow(item.getDouble("snow"));
                        result.setSnow_time("1h");
                    }
                }

                Log.v("hourly", result.toString());
                result_arr.add(result);
            }

            return result_arr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return result_arr;
    }


    public static String Wind_direction_check(double wind_degree){
        // 0 do la huong bac
        // 90 do la huong dong
        // 180 do la huong nam
        // 270 do la huong tay
        if( (wind_degree > 348.75 && wind_degree <=360) || wind_degree <= 11.25) return "Bắc";
        else if ( wind_degree > 11.25 && wind_degree <= 33.75 ) return  "Bắc Đông Bắc";
        else if ( wind_degree > 33.75 && wind_degree <= 56.25 ) return "Đông Bắc";
        else if ( wind_degree > 56.25 && wind_degree <= 78.75 ) return "Đông Đông Bắc";
        else if ( wind_degree > 78.75 && wind_degree <= 101.25) return "Đông";
        else if ( wind_degree >101.25 && wind_degree <= 123.25) return "Đông Đông Nam";
        else if ( wind_degree >123.25 && wind_degree <= 146.25) return "Đông Nam";
        else if ( wind_degree >146.25 && wind_degree <= 168.75) return "Nam Đông Nam";
        else if ( wind_degree >168.75 && wind_degree <= 191.25) return "Nam";
        else if ( wind_degree >191.25 && wind_degree <= 213.75) return "Nam Tây Nam";
        else if ( wind_degree >213.75 && wind_degree <= 236.25) return "Tây Nam";
        else if ( wind_degree >236.25 && wind_degree <= 258.75) return "Tây Tây Nam";
        else if ( wind_degree >258.75 && wind_degree <= 281.25) return "Tây";
        else if ( wind_degree >281.25 && wind_degree <= 303.75) return "Tây Tây Bắc";
        else if ( wind_degree >303.75 && wind_degree <= 326.25) return "Tây Bắc";
        else if ( wind_degree >326.25 && wind_degree <= 348.75) return "Bắc Tây Bắc";
        else return "Không xác định";
    }
}
