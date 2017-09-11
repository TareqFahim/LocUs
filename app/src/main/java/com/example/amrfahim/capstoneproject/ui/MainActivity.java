package com.example.amrfahim.capstoneproject.ui;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.adapters.PlacesGridAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    List placesTitles, imgsUrl;
    PlacesGridAdapter mGridAdapter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();                    // Realtime Database Root
    DatabaseReference mPlacesRef = mRootRef.child("places");
    DatabaseReference mSelectedPlaceRef, mImgsRef;

    @BindView(R.id.collapsing_toolbar_main_grid) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.main_screen_recycler_view) RecyclerView mPlacesTitlesGrid;
    @BindView(R.id.add_place_fab) FloatingActionButton addPlaceFab;

    @Override
    protected void onStart() {
        super.onStart();
        placesTitles = new ArrayList();
        imgsUrl = new ArrayList();
        mPlacesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String placeNameStr = dataSnapshot.getKey();
                placesTitles.add(placeNameStr);
                setPlacesGridAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
        mPlacesRef.keepSynced(true);

        final Intent intent = new Intent(this, AddPlaceActivity.class);
        addPlaceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        collapsingToolbarLayout.setTitle("LocUs");
        int noOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns);
        mPlacesTitlesGrid.setLayoutManager(layoutManager);
        mPlacesTitlesGrid.setHasFixedSize(true);

    }

    private void setPlacesGridAdapter() {
        mGridAdapter = new PlacesGridAdapter(this, placesTitles, imgsUrl);
        mPlacesTitlesGrid.setAdapter(mGridAdapter);
    }


}
