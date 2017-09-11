package com.example.amrfahim.capstoneproject.ui;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.adapters.TimeTableListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeTableActivity extends AppCompatActivity {

    @BindView(R.id.time_table_time_list_view) RecyclerView mTimeTableRecyclerView;
    private TimeTableListAdapter mTimeTableAdapter;
    private List<Integer> time;
    private List<Boolean> timeAvail;
    private Intent intent;
    String placeNamrStr;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();                    // Realtime Database Root
    DatabaseReference mPlacesRef = mRootRef.child("places");
    DatabaseReference mSelectedPlaceRef, mTimeTableRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        ButterKnife.bind(this);
        int noOfColumns = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns);
        mTimeTableRecyclerView.setLayoutManager(layoutManager);
        mTimeTableRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        if(intent.hasExtra(getString(R.string.place_name_intent_extra))){
            placeNamrStr = intent.getStringExtra(getString(R.string.place_name_intent_extra));
            mSelectedPlaceRef = mPlacesRef.child(placeNamrStr);
            mTimeTableRef = mSelectedPlaceRef.child("TimeTable");
        }
        time = new ArrayList<>();
        timeAvail = new ArrayList<>();
        if(mTimeTableRef != null){
            mTimeTableRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String timeStr = dataSnapshot.getKey();
                    boolean timeAvailBool = (boolean) dataSnapshot.getValue();
                    time.add(Integer.parseInt(timeStr));
                    timeAvail.add(timeAvailBool);
                    setTimeTableAdapter();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String timeStr = dataSnapshot.getKey();
                    int index = time.indexOf(Integer.parseInt(timeStr));
                    boolean timeAvailBool = (boolean) dataSnapshot.getValue();
                    timeAvail.set(index, timeAvailBool);
                    mTimeTableAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setTimeTableAdapter() {
        mTimeTableAdapter = new TimeTableListAdapter(this, time, timeAvail);
        mTimeTableRecyclerView.setAdapter(mTimeTableAdapter);
    }
}
