package com.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.weatherapp.API.converter;
import com.weatherapp.activity.MainActivity;

import static com.weatherapp.activity.MainActivity.weather;

public class widgetprovider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v("widget", "update");
        for(int appWidgetId : appWidgetIds){

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);
            views.setTextViewText(R.id.temp_wid, String.valueOf(weather.getReal_temp()) + "°C");
            views.setTextViewText(R.id.feelliketemp_wid,"Như " + String.valueOf(weather.getFeelLike_temp() + "°C"));
            views.setTextViewText(R.id.des_wid, converter.UppingCaseFirstCharacter(weather.getDescription()));

            String icon_uri = "@drawable/icon"+ weather.getIcon();
            int imageResource = context.getResources().getIdentifier(icon_uri, null, context.getPackageName());
            views.setImageViewResource(R.id.icon_wid, imageResource);

            views.setOnClickPendingIntent(R.id.icon_wid, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }
}
