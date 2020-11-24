package com.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.weatherapp.API.converter;
import com.weatherapp.API.timestamp_Converter;
import com.weatherapp.R;
import com.weatherapp.model.GPS_coordinate;
import com.weatherapp.model.weatherDetail;

import static com.weatherapp.activity.MainActivity.weather;

public class detail_weather extends AppCompatActivity {

    private LocationManager locationManager;
    GPS_coordinate gps;
    weatherDetail wea;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // khi chưa có lịch sử donate thì không hiện tab report để không bị lỗi null
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        wea = (weatherDetail) i.getSerializableExtra("weatherDetail");
        setDetail(this);

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
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.aqi_view:{
                startActivity(new Intent(this, aqi.class));
                break;
            }
            case R.id.current_detail:{
                Intent i = new Intent(detail_weather.this, detail_weather.class);
                i.putExtra("weatherDetail", weather);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public void setDetail(Context context ){
        TextView realtemp = findViewById(R.id.temp_detail);
        ImageView icon = findViewById(R.id.icon_detail);
        TextView fliketemp = findViewById(R.id.flike_temp_detail);
        TextView des = findViewById(R.id.description_detail);
        TextView humi = findViewById(R.id.humi_detail);
        TextView pop = findViewById(R.id.pop_detail);
        TextView press= findViewById(R.id.press_detail);
        TextView wind_speed = findViewById(R.id.wind_speed_detail);
        TextView wind_dir = findViewById(R.id.wind_dir_detail);
        TextView visi = findViewById(R.id.visi_detail);
        TextView time = findViewById(R.id.time_detail);
        TextView location = findViewById(R.id.location_detail);

        realtemp.setText(String.valueOf(wea.getReal_temp()));
        fliketemp.setText("Cảm giác như " + String.valueOf(wea.getFeelLike_temp()) + "°C");
        des.setText(converter.UppingCaseFirstCharacter(wea.getDescription()));
        humi.setText("Độ ẩm \n"+ String.valueOf(wea.getHumidity()) + "%");
        pop.setText("Có mưa \n"+ String.valueOf((double)Math.round(wea.getPop() * 100d *10d )/10d) +"%");
        press.setText("Áp suất \n"+ String.valueOf(wea.getPressure()) + "HPa");

        wind_speed.setText("Gió \n"+ String.valueOf((double)Math.round(wea.getWind_speed()*3.6d * 10d) / 10d) + "km/h");
        wind_dir.setText("Hướng gió \n" + wea.getWind_direction_description());
        visi.setText("Tầm nhìn \n" + String.valueOf(wea.getVisibility()/1000d) +"km");
        location.setText(wea.getCity_name());

        String icon_uri = "@drawable/icon"+ wea.getIcon();
        int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        icon.setImageDrawable(res);

        time.setText(timestamp_Converter.convert( Long.parseLong(wea.getTime()) , 3));
    }
}
