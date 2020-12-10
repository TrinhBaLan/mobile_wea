package com.weatherapp.View_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.API.timestamp_Converter;
import com.weatherapp.model.*;

import com.weatherapp.R;

import java.util.List;
// dành cho việc tạo listview ở trang chính, hiện thông tin thời tiết theo giờ

public class horizontal_view_adapter extends RecyclerView.Adapter<horizontal_view_adapter.ViewHolder> {
    Context context;
    List<weatherDetail> list;
    main_adapter_onClick mainAdapterOnClick;

    public horizontal_view_adapter(Context context, List<weatherDetail> list, main_adapter_onClick adapter_onClick) {
        this.context = context;
        this.list = list;
        this.mainAdapterOnClick = adapter_onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.time.setText(timestamp_Converter.convert( Long.parseLong(list.get(position).getTime()) , 2));
        holder.date.setText(timestamp_Converter.convert( Long.parseLong(list.get(position).getTime()) , 4));
        holder.real_temp.setText(String.valueOf(list.get(position).getReal_temp()) + "°C");
        holder.flike_temp.setText( "Như " +String.valueOf(list.get(position).getFeelLike_temp()) +"°C");

        holder.pop.setText( "Mưa " + String.valueOf((double)Math.round(list.get(position).getPop() * 100d *10d )/10d) +"%");


        holder.windspeed.setText(String.valueOf((double)Math.round(list.get(position).getWind_speed()*3.6 * 10d) / 10d) +"km/h");
        String icon_uri = "@drawable/icon"+ list.get(position).getIcon();
        int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.icon.setImageDrawable(res);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time, real_temp, flike_temp, pop, windspeed, date;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_hourly);
            date = itemView.findViewById(R.id.date_hourly);
            real_temp = itemView.findViewById(R.id.realtemp_hourly);
            flike_temp = itemView.findViewById(R.id.flike_temp_hourly);
            pop = itemView.findViewById(R.id.pop_hourly);
            icon = itemView.findViewById(R.id.icon_hourly);
            windspeed = itemView.findViewById(R.id.windspeed_hourly);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainAdapterOnClick.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mainAdapterOnClick.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });



        }

        @Override
        public void onClick(View v) {

        }
    }
}

