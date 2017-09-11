package com.example.amrfahim.capstoneproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.ui.MainActivity;
import com.example.amrfahim.capstoneproject.utils.PlacesJSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class FavPlacesWidgetProvider extends AppWidgetProvider {

    SharedPreferences prefs;
    List favPlacesList;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String favPlacesJsonStr = prefs.getString(context.getString(R.string.prefrences_fav_places), "No Favourite Places");
        if(!favPlacesJsonStr.equals("No Favourite Places")){
            try {
                favPlacesList = new PlacesJSONParser().parseJson(favPlacesJsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int appWidgetId : appWidgetIds) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.fav_places_widget_provider);
            rv.setPendingIntentTemplate(R.id.widget_listview, pendingIntent);
            if (favPlacesList == null) {
                rv.setViewVisibility(R.id.empty_text_view, View.VISIBLE);
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            } else {
                Intent intent = new Intent(context, WidgetService.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(context.getString(R.string.place_name_intent_extra), (ArrayList<String>) favPlacesList);
                intent.putExtra(context.getString(R.string.place_name_intent_extra), bundle);

                rv.setRemoteAdapter(R.id.widget_listview, intent);
                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}