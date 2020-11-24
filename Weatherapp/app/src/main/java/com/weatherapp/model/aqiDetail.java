package com.weatherapp.model;

import java.util.ArrayList;

public class aqiDetail {
    String city_name;
    String time; // thoi gian cap nhat
    double aqi;  // chi so chat luong khong khi   air quality index

    String dominentpol = "không xác định"; // chat chinh gay o nhiem khong khi
    String dew = "không xác định";
    String CO = "không xác định";
    String NO2 = "không xác định";
    String SO2 = "không xác định";
    String O3 = "không xác định";
    String PM10 = "không xác định"; // chi so bui min  2.5 -> 10 micromet
    String PM25 = "không xác định"; // chi so bui min < 2.5 micromet
    String impact = "";
    String description = "";

    public aqiDetail() {
        this.aqi = 0;
    }
    // toString() de test
    public String toString(){
        return "city " + this.city_name
                +"\naqi " + this.aqi
                + "\ndomi " + this.dominentpol
                + "\ndew " + this.dew
                +"\nco " + this.CO
                +"\nno2 " + this.NO2
                + "\nSO2 " + this.SO2
                + "\nO3 " + this.O3
                + "\nPM10 " + this.PM10
                + "\nPM25 " + this.PM25
                + "\ntime " + this.time
                + "\nimpact " + this.impact
                + "\ndescription " + this.description;
    }

    public static ArrayList<String> check_impact(double aqi, String PM25){
        // item 0 la danh gia chung ve chat luong khong khi: tot, trung binh hay xau....
        // item 1 la muc do anh huong chung den suc khoe

        ArrayList<String> result = new ArrayList<>();
        if(aqi <= 50){  // chat luong khong khi tot
            String x0 = "Tốt";
            String x1 = "Chất lượng không khí đạt yêu cầu và ô nhiễm không khí gây ra rất ít hoặc không có rủi ro";
            result.add(x0); result.add(x1);
        }
        else if (aqi <= 100){
            String x0 = "Trung bình";
            String x1 = "Chất lượng không khí chấp nhận được. Người nhạy cảm với không khí xấu nên hạn chế ra ngoài";

            result.add(x0); result.add(x1);
        }
        else if (aqi <= 150){
            String x0 = "Tương đối xấu";
            String x1 = "Ảnh hưởng xấu đến nhóm người nhạy cảm. Người bình thường ít nhiều có tác động";

            result.add(x0); result.add(x1);

        }
        else if (aqi <= 200){
            String x0 = "Xấu";
            String x1 = "Mọi người bắt đầu có dấu hiệu ảnh hưởng đến sức khỏe. Người nhạy cảm bị tác động rất mạnh";

            result.add(x0); result.add(x1);

        }
        else if (aqi <= 300){
            String x0 = "Rất xấu";
            String x1 = "Cảnh báo ở mức khẩn cấp. Mọi người đều bị tác động. Cần hạn chế ra ngoài";
            result.add(x0); result.add(x1);
        }
        else {  // aqi 300+
            String x0 = "Nguy hại";
            String x1 = "Tất cả mọi người đều bị ảnh hưởng ở mức độ nguy hại. Tránh mọi hoạt động ngoài trời";
            result.add(x0); result.add(x1);
        }

        return result;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDew() {
        return dew;
    }

    public void setDew(String dew) {
        this.dew = dew;
    }

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getDominentpol() {
        return dominentpol;
    }

    public void setDominentpol(String dominentpol) {
        this.dominentpol = dominentpol;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getNO2() {
        return NO2;
    }

    public void setNO2(String NO2) {
        this.NO2 = NO2;
    }

    public String getSO2() {
        return SO2;
    }

    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String o3) {
        O3 = o3;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }
}
