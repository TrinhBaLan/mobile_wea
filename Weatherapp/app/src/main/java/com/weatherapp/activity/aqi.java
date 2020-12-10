package com.weatherapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weatherapp.API.aqi_api;
import com.weatherapp.API.converter;
import com.weatherapp.R;
import com.weatherapp.View_adapter.horizontal_view_adapter;
import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.aqiDetail;
import com.weatherapp.model.weatherDetail;

import org.w3c.dom.Text;

import java.util.List;

import static com.weatherapp.activity.MainActivity.weather;

public class aqi extends AppCompatActivity {

    aqiDetail aqi;
    GPS_coordinate gps;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem cur = menu.findItem(R.id.current_detail);
        //cur.setVisible(false);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aqi_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        gps = getLocation();
        new get_aqi_info(this, gps).execute();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_120h: {
                startActivity(new Intent(this, next_120h_weather.class));
                break;
            }
            case R.id.main_ui: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.aqi_view: {
                Toast.makeText(this, "aqi", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.current_detail: {
                //Toast.makeText(this, "current detail", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(aqi.this, detail_weather.class);
                i.putExtra("weatherDetail", weather);
                startActivity(i);
                break;
            }
            case R.id.citylist_menu:{
                startActivity(new Intent(this, cityList.class));
                break;
            }
            case R.id.update:{
                finish();
                startActivity(new Intent(this, aqi.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public GPS_coordinate getLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            return new GPS_coordinate(lat, lon);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }

    private class get_aqi_info extends AsyncTask<String, String, aqiDetail>{
        Context context;
        GPS_coordinate gps;

        public get_aqi_info(Context context, GPS_coordinate gps) {
            this.context = context;
            this.gps = gps;
        }

        @Override
        protected aqiDetail doInBackground(String... strings) {
            aqi = aqi_api.getAqiDetail(gps,"gps", context);
            return aqi;
        }

        protected void onPostExecute(aqiDetail result) {
            super.onPostExecute(result);
            TextView aqi = findViewById(R.id.aqi_number);
            TextView main_po = findViewById(R.id.main_pollutant);
            TextView impact = findViewById(R.id.impact_aqi);
            TextView time = findViewById(R.id.time_aqi);
            TextView des = findViewById(R.id.description_aqi);
            TextView co = findViewById(R.id.co_aqi);
            TextView no2 = findViewById(R.id.no2_aqi);
            TextView so2 = findViewById(R.id.so2_aqi);
            TextView o3 = findViewById(R.id.o3_aqi);
            TextView pm10 = findViewById(R.id.pm10_aqi);
            TextView pm25 = findViewById(R.id.pm25_aqi);
            TextView location = findViewById(R.id.location_aqi);

            aqi.setText(String.valueOf((int)result.getAqi()));
            main_po.setText("Chất độc chính " + converter.UppingCase(result.getDominentpol()));
            //impact.setText(result.getImpact());
            time.setText(result.getTime().split(" ")[0] +" " + result.getTime().split(" ")[1]);
            des.setText(converter.UppingCaseFirstCharacter(result.getDescription()));
            co.setText("CO\n" + result.getCO() + " ppm");
            no2.setText("NO2\n"+ result.getNO2() + " ppm");
            so2.setText("SO2\n" + result.getSO2() + " ppm");
            o3.setText("O3\n"+ result.getO3() +" ppm");
            pm10.setText("PM10\n"+ result.getPM10()+" ppm");
            pm25.setText("PM25\n"+ result.getPM25()+" ppm");
            no2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng NO2 trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
            co.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng CO trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
            so2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng SO2 trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
            o3.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng O3 trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
            pm10.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng bụi mịn có đường kính từ 2.5 đến 10 micromet trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
            pm25.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    Toast t = new Toast(getApplicationContext());
                    TextView textView = new TextView(com.weatherapp.activity.aqi.this);
                    textView.setText("Lượng bụi mịn có đường kính bé hơn 2.5 micromet trong không khí\nĐơn vị PPM (Một phần triệu)");
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
                    textView.setAlpha((float)0.5);
                    textView.setTextSize((float)18);
                    textView.setPadding(10,10,10,10);
                    t.setView(textView);
                    t.setGravity(Gravity.CENTER, 0,300);
                    t.show();
                }
            });
        }

    }
}
