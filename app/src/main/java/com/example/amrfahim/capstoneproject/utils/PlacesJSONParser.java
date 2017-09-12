package com.example.amrfahim.capstoneproject.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amr Fahim on 9/10/2017.
 */

public class PlacesJSONParser {
    final String OWM_RECIPE_PLACES = "places_json";
    List places;

    public List parseJson(String json) throws Exception{

        places = new ArrayList();
        if(json == null || json.equals("")){
            places.add("");
            return places;
        }
        JSONObject placesJSONObject = new JSONObject(json);
        JSONArray placesArray = placesJSONObject.getJSONArray(OWM_RECIPE_PLACES);
        for (int i = 0; i < placesArray.length(); i++) {
            this.places.add(placesArray.get(i));
        }
        return places;
    }
}
