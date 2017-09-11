package com.example.amrfahim.capstoneproject.ui;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Amr Fahim on 9/10/2017.
 */

public class OfflineFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
