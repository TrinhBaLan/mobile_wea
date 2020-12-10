package com.weatherapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.R;
import com.weatherapp.View_adapter.horizontal_view_adapter;
import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.main_adapter_onClick;
import com.weatherapp.model.weatherDetail;
import com.weatherapp.API.*;

import java.io.IOException;
import java.util.List;
import com.weatherapp.View_adapter.*;

import static com.weatherapp.activity.MainActivity.weather;

public class next_120h_weather extends AppCompatActivity {
    vertical_view_adapter adapter;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_120h_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        new get_120h_weather(getLocation(), this).execute();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_120h:{
                Toast.makeText(this, "main ui", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.main_ui:{
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.aqi_view:{
                startActivity(new Intent(this, aqi.class));
                break;
            }
            case R.id.current_detail:{
                //Toast.makeText(this, "current detail", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(next_120h_weather.this, detail_weather.class);
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
                startActivity(new Intent(this, next_120h_weather.class));
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

    private class get_120h_weather extends AsyncTask<String, String, List<weatherDetail>> implements main_adapter_onClick {

        GPS_coordinate gps;
        Context context;
        List<weatherDetail> list;


        public get_120h_weather(GPS_coordinate gps, Context context) {
            this.gps = gps;
            this.context = context;
        }


        @Override
        protected List<weatherDetail> doInBackground(String... strings) {
            list = openWeathermapAPI.get_5days_3hour_weather(gps);
            return list;
        }

        protected void onPostExecute(List<weatherDetail> result) {
            super.onPostExecute(result);
            adapter = new vertical_view_adapter(context, result, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = findViewById(R.id.next_120h_listview);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            String city = result.get(0).getCity_name();
            location = findViewById(R.id.location_120h);
            location.setText(city);

        }

        @Override
        public void onItemClick(int position) {
            Intent i = new Intent(next_120h_weather.this, detail_weather.class);
            i.putExtra("weatherDetail", list.get(position));
            startActivity(i);
        }

        @Override
        public void onLongItemClick(int position) {

        }
    }
}
