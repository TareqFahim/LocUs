package com.example.amrfahim.capstoneproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrfahim.capstoneproject.R;
import com.example.amrfahim.capstoneproject.ui.PlaceDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amr Fahim on 9/8/2017.
 */

public class PlacesGridAdapter extends RecyclerView.Adapter<PlacesGridAdapter.PlacesViewHolder> {

    private Context context;
    private List placesTitle = new ArrayList();
    private List imgsUrl = new ArrayList();

    public PlacesGridAdapter(Context c, List placesTitle, List imgsUrl){
        this.context = c;
        this.placesTitle = placesTitle;
        this.imgsUrl = imgsUrl;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.main_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        PlacesViewHolder viewHolder = new PlacesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder viewHolder, final int position) {
        viewHolder.placeTitleTextView.setText((String) placesTitle.get(position));
//        Picasso.with(context).load((String) imgsUrl.get(position)).fit().into(viewHolder.placeImageView);
        viewHolder.placesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceDetailsActivity.class);
                intent.putExtra(context.getString(R.string.place_name_intent_extra), (String) placesTitle.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesTitle.size();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_grid_card_view)
        CardView placesCardView;
        @BindView(R.id.main_grid_card_view_tv)
        TextView placeTitleTextView;
        @BindView(R.id.main_grid_card_view_imgview)
        ImageView placeImageView;

        public PlacesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
