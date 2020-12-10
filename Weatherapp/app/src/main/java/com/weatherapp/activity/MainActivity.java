package com.weatherapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.weatherapp.API.converter;
import com.weatherapp.API.openWeathermapAPI;
import com.weatherapp.R;
import com.weatherapp.model.GPS_coordinate;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.weatherapp.model.main_adapter_onClick;
import com.weatherapp.model.weatherDetail;
import com.weatherapp.View_adapter.*;
import com.weatherapp.widgetprovider;

import static com.weatherapp.API.accuWeatherAPI.getGPS_by_cityname;
import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final Long noti_interval = Long.valueOf(3600000);
    public static LocationManager locationManager;
    public static GPS_coordinate gps;
    horizontal_view_adapter adapter;
    RecyclerView recyclerView;
    public static weatherDetail weather;
    FloatingActionButton fab;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("on dit troi", "dit troi");
        Intent intent = new Intent(MainActivity.this,notifi.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Log.v("on dit troi", "dit troi 2");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), noti_interval, pendingIntent );
        Log.v("on dit troi", "dit troi 3");

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("on create main", "on create main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_and_hourly);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        Log.v("start","start");
        TextView temp = findViewById(R.id.temp_detail);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, detail_weather.class);
                i.putExtra("weatherDetail", weather);
                startActivity(i);
            }
        });
        createNotificationChannel();

        Intent i = getIntent();
        GPS_coordinate gps_citylist = null;
        gps_citylist = (GPS_coordinate) i.getSerializableExtra("GPS_coordinate");

        if(gps_citylist == null) {
            new get_current_weather(getLocation(), this).execute();
            new get_hourly_weather(getLocation(), this).execute();
        }
        else {
            Log.v("gps ser", gps_citylist.toString());
            new get_current_weather(gps_citylist, this).execute();
            new get_hourly_weather(gps_citylist, this).execute();
        }

        Intent intent = new Intent(MainActivity.this,notifi.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), noti_interval, pendingIntent );


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_120h:{
                startActivity(new Intent(this, next_120h_weather.class));
                break;
            }
            case R.id.main_ui:{
                Toast.makeText(this, "main ui", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.aqi_view:{
                startActivity(new Intent(this, aqi.class));
                break;
            }
            case R.id.current_detail:{
                Intent i = new Intent(MainActivity.this, detail_weather.class);
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
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    // lay kinh do va vi do theo GPS
    // de truyen vao API
    public GPS_coordinate getLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
            gps = new GPS_coordinate(lat, lon);
            return new GPS_coordinate(lat, lon);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }

    // tra ve thoi gian he thong theo unix timestamp

    public Long get_current_time() {
        Date currentTime = Calendar.getInstance().getTime();

        return currentTime.getTime() / 1000;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class get_hourly_weather extends AsyncTask<String, String, List<weatherDetail>> implements main_adapter_onClick {
        GPS_coordinate gps;
        Context context;
        List<weatherDetail> list;

        public get_hourly_weather(GPS_coordinate gps, Context context) {
            this.gps = gps;
            this.context = context;
        }

        @Override
        protected List<weatherDetail> doInBackground(String... strings) {
            Log.v("do in backg", "do");
            //Log.v("cityname ", getGPS_by_cityname("Vũng tàu").toString());
            list = openWeathermapAPI.get_hourly_weather(gps, MainActivity.this);
            return list;
        }

        @Override
        protected void onPostExecute(List<weatherDetail> result) {
            super.onPostExecute(result);
            adapter = new horizontal_view_adapter(context, result, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }

        @Override
        public void onItemClick(int position) {
            Log.v("on click", list.get(position).toString() );

            Intent i = new Intent(MainActivity.this, detail_weather.class);
            i.putExtra("weatherDetail", list.get(position));
            startActivity(i);
        }

        @Override
        public void onLongItemClick(int position) {
            // đây là xử lý khi giữ
        }
    }

    private class get_current_weather extends AsyncTask<String, String, weatherDetail>{
        GPS_coordinate gps;
        Context context;
        protected ProgressDialog dialog;

        public get_current_weather(GPS_coordinate gps, Context context) {
            this.gps = gps;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Đang tải");
            this.dialog.show();
        }


        @Override
        protected weatherDetail doInBackground(String... strings) {
            weather = openWeathermapAPI.getCurrentWeatherDetail(gps);
            return weather;
        }
        @Override
        protected void onPostExecute(weatherDetail result) {
            super.onPostExecute(result);
            TextView location = findViewById(R.id.location_aqi);
            TextView temp = findViewById(R.id.temp_detail);
            TextView des = findViewById(R.id.description_cur);
            ImageView icon = findViewById(R.id.icon_detail);

            location.setText(result.getCity_name());
            temp.setText(String.valueOf(result.getReal_temp()));
            des.setText(converter.UppingCaseFirstCharacter(result.getDescription()));

            String icon_uri = "@drawable/icon"+ result.getIcon();
            int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            icon.setImageDrawable(res);

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }



    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String title = "test title create noti";
            String content = "test content create noti";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifi", title, importance);
            channel.setDescription(content);

             NotificationManager manager = getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel);
        }
    }
}