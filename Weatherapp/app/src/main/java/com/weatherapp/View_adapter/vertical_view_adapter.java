package com.weatherapp.View_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.API.timestamp_Converter;
import com.weatherapp.R;
import com.weatherapp.model.main_adapter_onClick;
import com.weatherapp.model.weatherDetail;

import java.util.List;

public class vertical_view_adapter extends RecyclerView.Adapter<vertical_view_adapter.ViewHolder> {
    Context context;
    List<weatherDetail> list;
    main_adapter_onClick mainAdapterOnClick;

    public vertical_view_adapter(Context context, List<weatherDetail> list, main_adapter_onClick ma) {
        this.context = context;
        this.list = list;
        mainAdapterOnClick = ma;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_120h, parent, false);
        return new vertical_view_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.real_temp.setText("Nhiệt độ " + String.valueOf(list.get(position).getReal_temp()) + "°C");
        holder.feelLike_temp.setText("Cảm giác " +String.valueOf(list.get(position).getFeelLike_temp()) +"°C");
        holder.pop.setText( String.valueOf((double)Math.round(list.get(position).getPop() * 100d *10d )/10d) +"%");

        String icon_uri = "@drawable/icon"+ list.get(position).getIcon();
        int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.icon.setImageDrawable(res);

        Long time = Long.parseLong(list.get(position).getTime());
        holder.hour.setText(timestamp_Converter.convert(time, 2));
        holder.date.setText(timestamp_Converter.convert(time, 1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hour, date, real_temp, feelLike_temp, pop;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour_120h);
            date = itemView.findViewById(R.id.date_120h);
            real_temp = itemView.findViewById(R.id.realtemp_120h);
            feelLike_temp = itemView.findViewById(R.id.feelLike_temp_120h);
            pop = itemView.findViewById(R.id.pop_120h);
            icon = itemView.findViewById(R.id.icon_120h);

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
