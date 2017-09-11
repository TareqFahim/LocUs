package com.example.amrfahim.capstoneproject.widget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViewsService;


import com.example.amrfahim.capstoneproject.R;

import java.util.List;

/**
 * Created by Hope on 8/10/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Bundle b = intent.getBundleExtra(getString(R.string.place_name_intent_extra));
        List placesNamesList;
        placesNamesList = b.getStringArrayList(getString(R.string.place_name_intent_extra));

        return new WidgetListProvider(getApplicationContext(), placesNamesList);
    }
}
