package com.example.amrfahim.capstoneproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.adapters.PlacesGridAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    DatabaseReference mPlacesRef;
    DatabaseReference mSelectedPlaceRef, mImgsRef;

    @BindView(R.id.collapsing_toolbar_main_grid) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.main_screen_recycler_view) RecyclerView mPlacesTitlesGrid;
    @BindView(R.id.add_place_fab) FloatingActionButton addPlaceFab;
    @BindView(R.id.toolbar_main_grid)
    Toolbar toolbar;

    Context context = this;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String email, password;

    @Override
    protected void onStart() {
        super.onStart();
        placesTitles = new ArrayList();
        imgsUrl = new ArrayList();
        mAuth.addAuthStateListener(mAuthListener);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_sign_out){
                    mAuth.signOut();
                    Intent intent = new Intent(context, LogInActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
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
        mPlacesRef = mRootRef.child(getString(R.string.firebase_database_places_ref));
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.main_activity_menu);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(context, LogInActivity.class);
                    startActivity(intent);
                }
            }
        };

        int noOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns);
        mPlacesTitlesGrid.setLayoutManager(layoutManager);
        mPlacesTitlesGrid.setHasFixedSize(true);

    }

    private void setPlacesGridAdapter() {
        mGridAdapter = new PlacesGridAdapter(this, placesTitles, imgsUrl);
        mPlacesTitlesGrid.setAdapter(mGridAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
