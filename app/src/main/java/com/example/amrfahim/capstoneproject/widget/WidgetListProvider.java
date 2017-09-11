package com.example.amrfahim.capstoneproject.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.amrfahim.capstoneproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 8/10/2017.
 */

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private List placesNamesList = new ArrayList();
    private Context context;

    public WidgetListProvider(Context context, List list){
        this.placesNamesList = list;
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(placesNamesList == null)
            return 0;
        return placesNamesList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.wideget_textview, (CharSequence) placesNamesList.get(position));
        final Intent activityIntent = new Intent();
        activityIntent.putExtra(context.getResources().getString(R.string.place_name_intent_extra), (String) placesNamesList.get(position));
        remoteViews.setOnClickFillInIntent(R.id.wideget_textview, activityIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
