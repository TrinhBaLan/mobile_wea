package com.weatherapp.model;

import java.io.Serializable;

public class weatherDetail implements Serializable {
    // thong tin thoi tiet
    // bao gom nhiet do, do am, gio, ....

    String city_name;

    String description;
    String icon;
    // temp tinh bang do C
    double real_temp;
    double temp_min;
    double temp_max;
    double feelLike_temp;

    double pressure; // ap suat khi quyen hPa
    double humidity; // tinh = %

    double visibility; // tam nhin

    double wind_speed ;
    double wind_direction_deg;
    String wind_direction_description;

    double clouds; // ti le co may. tinh bang %

    String sunrise;
    String sunset;

    String rain_time;
    double rain = 0 ; // luong mua, tinh = mm

    String snow_time;
    double snow = 0 ; // luong tuyet trong 3h

    double pop = 0 ; // ty le co mua

    String time; // thoi gian cap nhat

    // for testing
    public String toString(){
        return    "city " + this.city_name
                + "\ndescription " + this.description
                + "\nicon " + this.icon
                + "\nreal temp " + this.real_temp
                + "\ntemp min " + this.temp_min
                + "\ntemp max " + this.temp_max
                + "\nfeel like temp " + this.feelLike_temp
                + "\npressure " + this.pressure
                + "\nhumidity " + this.humidity
                + "\nvisibility " + this.visibility
                + "\nwind speed " + this.wind_speed
                + "\nwind direction " + this.wind_direction_deg
                + "\nwind direction description " + this.wind_direction_description
                + "\nclound " + this.clouds
                + "\nsunrise " + this.sunrise
                + "\nsunset " + this.sunset
                + "\npop " + this.pop
                + "\nrain time " + this.rain_time
                + "\nrain " + this.rain
                + "\nsnow time " + this.snow_time
                + "\nsnow " + this.snow
                + "\ntime " + this.time;
    }

    public String getWind_direction_description() {
        return wind_direction_description;
    }

    public void setWind_direction_description(String wind_direction_description) {
        this.wind_direction_description = wind_direction_description;
    }

    public String getSnow_time() {
        return snow_time;
    }

    public void setSnow_time(String snow_time) {
        this.snow_time = snow_time;
    }

    public String getRain_time() {
        return rain_time;
    }

    public void setRain_time(String rain_time) {
        this.rain_time = rain_time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public double getPop() {
        return pop;
    }

    public void setRain(double rain_3h) {
        this.rain = rain_3h;
    }

    public void setSnow(double snow_3h) {
        this.snow = snow_3h;
    }

    public double getRain() {
        return rain;
    }

    public double getSnow() {
        return snow;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
// con thoi gian mat troi moc, mat troi lan
    // con convert unix timestamp

    public weatherDetail(){}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setReal_temp(double real_temp) {
        this.real_temp = (double)Math.round(real_temp * 10d) / 10d;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = (double)Math.round(temp_min * 10d) / 10d;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = (double)Math.round(temp_max * 10d) / 10d;
    }

    public void setFeelLike_temp(double feelLike_temp) {
        this.feelLike_temp = (double)Math.round(feelLike_temp * 10d) / 10d;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWind_direction_deg(double wind_direction_deg) {
        this.wind_direction_deg = wind_direction_deg;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public double getReal_temp() {
        return real_temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getFeelLike_temp() {
        return feelLike_temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public double getWind_direction_deg() {
        return wind_direction_deg;
    }

    public double getClouds() {
        return clouds;
    }
}
