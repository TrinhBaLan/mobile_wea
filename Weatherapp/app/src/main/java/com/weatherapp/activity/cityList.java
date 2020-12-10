package com.weatherapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.weatherapp.API.converter;
import com.weatherapp.API.openWeathermapAPI;
import com.weatherapp.R;
import com.weatherapp.model.GPS_coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.weatherapp.model.main_adapter_onClick;
import com.weatherapp.model.weatherDetail;
import com.weatherapp.View_adapter.*;
import com.weatherapp.widgetprovider;
import static com.weatherapp.activity.MainActivity.gps;

import static com.weatherapp.API.accuWeatherAPI.getGPS_by_cityname;
import static com.weatherapp.activity.MainActivity.weather;

public class cityList extends AppCompatActivity {
    public static final String FILE_NAME = "city_info.txt";
    public static final String current_location = "current_gps.txt";
    ListView cityview;
    Button submitbutton;
    ArrayAdapter arrayAdapter;
    EditText city;
    List<String> city_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.v("location", String.valueOf(getFilesDir()));
        File cities_file = new File(String.valueOf(getFilesDir()) + "/" + FILE_NAME);
        if( !cities_file.exists()) {
            try {
                Log.v("newfile", "create a new file to store data");
                cities_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File current = new File(String.valueOf(getFilesDir()) + "/" + current_location);
        if( !current.exists()){
            try {
                Log.v("newfile", "create a new file to store data");
                current.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        city_list = getcityInfo();

        //Log.v("gps saved", SetupCurrent(this).toString());
        //saveCity("thanh hóa", new GPS_coordinate(19.797,105.779));

        SetupListView(city_list, this);

        submitbutton = findViewById(R.id.submit_button);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_info();
                city_list = getcityInfo();
                SetupListView(city_list, cityList.this);
                city = findViewById(R.id.city_name_list);
                city.setText("");
            }
        });


        cityview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int pos = position;
                if( position != 0) {
                    AlertDialog.Builder delete_alert = new AlertDialog.Builder(cityList.this);
                    delete_alert.setMessage("Xóa địa điểm này?").setCancelable(true)
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //String str = cityview.getItemAtPosition(position).toString();
                                        //Toast.makeText(cityList.this, city_list.get(pos - 1), Toast.LENGTH_SHORT).show();
                                        deleteCity_info(city_list, pos - 1);
                                        SetupListView(city_list, cityList.this);
                                    }
                                })
                                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                    AlertDialog alert = delete_alert.create();
                    alert.setTitle("Xóa");
                    alert.show();

                }
                return true;
            }
        });
        cityview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if( position == 0) startActivity(new Intent(cityList.this, MainActivity.class));
                else{
                    String str = city_list.get(position-1);
                    //Toast.makeText(cityList.this, str, Toast.LENGTH_SHORT).show();
                    String[] tmp = str.split("@");

                    Intent i = new Intent(cityList.this, MainActivity.class);
                    i.putExtra("GPS_coordinate",
                            new GPS_coordinate(Double.parseDouble(tmp[1]),Double.parseDouble(tmp[2])));
                    startActivity(i);
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_120h:{
                startActivity(new Intent(this, next_120h_weather.class));
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
                Intent i = new Intent(cityList.this, detail_weather.class);
                i.putExtra("weatherDetail", weather);
                startActivity(i);
                break;
            }

            case R.id.update:{
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem update = menu.findItem(R.id.citylist_menu);
        update.setVisible(false);
        return true;
    }

    public GPS_coordinate SetupCurrent(cityList cityList) {
        try {
            FileOutputStream fos = openFileOutput(current_location, MODE_PRIVATE);
            String tmp = String.valueOf(gps.getLat() + "@" + gps.getLon());
            fos.write(tmp.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = openFileInput(current_location);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String[] text = br.readLine().split("@");
            Log.v("file cur", text[0] + " " + text[1]);

            fis.close(); isr.close(); br.close();
            return new GPS_coordinate(Double.parseDouble(text[0]), Double.parseDouble(text[1]));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }
    public void saveCity(String cityname, GPS_coordinate gps){
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
            String tmp = cityname + "@" + String.valueOf(gps.getLat()) + "@" + String.valueOf(gps.getLon()+"\n");
            fos.write(tmp.getBytes());
            Log.v("write", String.valueOf(getFilesDir()));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getcityInfo(){
        List<String> result = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text ;
            while ((text=br.readLine()) != null){
                Log.v("test", text);
                result.add(text);
            }
            fis.close(); isr.close(); br.close();
            return  result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void SetupListView(List<String> city_list, Context context){
        cityview = findViewById(R.id.list_cityname);
        List<String> city__name = new ArrayList<>();

        for( int i = 0; i< city_list.size(); i++){
            city__name.add(city_list.get(i).split("@")[0]);
        }
        city__name.add(0, "Vị trí hiện tại");
        arrayAdapter = new ArrayAdapter(context, R.layout.simple_list_item, city__name);
        cityview.setAdapter(arrayAdapter);
    }
    @SuppressLint("ResourceType")
    public void submit_info(){
        city = findViewById(R.id.city_name_list);
        String city_name = city.getText().toString();
        GPS_coordinate gps = getGPS_by_cityname(city_name);
        if ( gps == null) {
            Toast t = new Toast(getApplicationContext());
            TextView textView = new TextView(com.weatherapp.activity.cityList.this);
            textView.setText("Vị trí không hợp lệ");
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
            textView.setAlpha((float)0.8);
            textView.setTextSize((float)18);
            textView.setPadding(10,10,10,10);
            t.setView(textView);
            t.setGravity(Gravity.CENTER, 0,300);
            t.show();
            return;
        }
        if( gps.getLon() == 999.99 || gps.getLat() == 999.99){
            Toast t = new Toast(getApplicationContext());
            TextView textView = new TextView(com.weatherapp.activity.cityList.this);
            textView.setText("Quá số lượt thêm");
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(getResources().getDrawable(R.xml.radius_toast));
            textView.setAlpha((float)0.8);
            textView.setTextSize((float)18);
            textView.setPadding(10,10,10,10);
            t.setView(textView);
            t.setGravity(Gravity.CENTER, 0,300);
            t.show();
            return;
        }
        saveCity(city_name, gps);
        Log.v("submit",city_name);
        Log.v("submit", gps.toString());
    }

    public void deleteCity_info(List<String> cities, int pos){
        cities.remove(pos);
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            for( int i = 0; i< cities.size(); i++){
                String tmp = cities.get(i) + "\n";
                fos.write(tmp.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
