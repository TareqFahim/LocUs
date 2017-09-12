package com.example.amrfahim.capstoneproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.adapters.PlaceDetailsListAdapter;
import com.example.amrfahim.capstoneproject.adapters.PlacesGridAdapter;
import com.example.amrfahim.capstoneproject.utils.PlacesJSONParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_screen_recycler_view)
    RecyclerView mPlaceInfoListView;
    @BindView(R.id.details_screen_title_tv)
    TextView titleTextView;
    @BindView(R.id.time_table_btn)
    Button timeTableBtn;
    @BindView(R.id.fav_btn)
    ImageButton favBtn;
    @BindView(R.id.toolbar_image_view)
    ImageView placeImageView;

    private List placeInfoTitleList, placeInfoValueList, favPlacesList;
    private String placeNameStr = "";
    private PlaceDetailsListAdapter mListAdapter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();                    // Realtime Database Root
    DatabaseReference mPlacesRef;
    DatabaseReference mSelectedPlaceRef;

    SharedPreferences prefs;
    Context context = this;

    String favPlacesJsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        mPlacesRef = mRootRef.child(getString(R.string.firebase_database_places_ref));
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.place_name_intent_extra))) {
            placeNameStr = intent.getStringExtra(getString(R.string.place_name_intent_extra));
            mSelectedPlaceRef = mPlacesRef.child(placeNameStr);
        }

        int noOfColumns = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns);
        mPlaceInfoListView.setLayoutManager(layoutManager);
        mPlaceInfoListView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        placeInfoTitleList = new ArrayList();
        placeInfoValueList = new ArrayList();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();

        try {
            favPlacesJsonStr = prefs.getString(getString(R.string.prefrences_fav_places), getString(R.string.prefrences_default_value));
            favPlacesList = new PlacesJSONParser().parseJson(favPlacesJsonStr);
            if (favPlacesList.contains(placeNameStr)) {
                favBtn.setImageResource(R.drawable.ic_favorite_white_24px);
            } else {
                favBtn.setImageResource(R.drawable.ic_favorite_border_white_24px);
            }
        } catch (Exception e) {
            favBtn.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }

        if (!placeNameStr.equals("")) {
            titleTextView.setText(placeNameStr);
            mSelectedPlaceRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        String titleStr = dataSnapshot.getKey();
                        String infoStr = (String) dataSnapshot.getValue();
                        if (titleStr.equals(getString(R.string.firebase_database_pic_ref))) {
                            Picasso.with(context).load(infoStr).fit().into(placeImageView);
                        }
                        placeInfoTitleList.add(titleStr);
                        placeInfoValueList.add(infoStr);
                        setPlaceInfoListAdapter();
                    } catch (Exception ex) {

                    }
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

            final Intent intent = new Intent(this, TimeTableActivity.class);
            timeTableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra(getString(R.string.place_name_intent_extra), placeNameStr);
                    startActivity(intent);
                }
            });
            final Context c = this;
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject placesJson = new JSONObject();
                    List temp = new ArrayList();
                    try {
                        temp = new PlacesJSONParser().parseJson(favPlacesJsonStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!checkIfExsistsInPref(placeNameStr)) {
                        temp.add(placeNameStr);
                    }
                    try {
                        placesJson.put("places_json", new JSONArray(temp));
                        String ss = placesJson.toString();
                        editor.putString(getString(R.string.prefrences_fav_places), ss);
                        editor.commit();
                        favBtn.setImageResource(R.drawable.ic_favorite_white_24px);
                    } catch (JSONException e) {
                        Toast.makeText(c, getString(R.string.toast_error_creating_json), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean checkIfExsistsInPref(String s) {
        if(favPlacesList == null)
            return false;
        return favPlacesList.contains(s);
    }

    private void setPlaceInfoListAdapter() {
        mListAdapter = new PlaceDetailsListAdapter(this, placeInfoTitleList, placeInfoValueList);
        mPlaceInfoListView.setAdapter(mListAdapter);
    }
}
