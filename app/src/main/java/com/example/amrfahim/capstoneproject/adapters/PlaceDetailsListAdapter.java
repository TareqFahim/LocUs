package com.example.amrfahim.capstoneproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amrfahim.capstoneproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amr Fahim on 9/9/2017.
 */

public class PlaceDetailsListAdapter extends RecyclerView.Adapter<PlaceDetailsListAdapter.DetailsListViewHolder> {

    private Context context;
    private List placeDetailsInfo = new ArrayList();
    private List placeDetailsTitle = new ArrayList();

    public PlaceDetailsListAdapter(Context c, List placeDetailsTitle, List placeDetailsInfo){
        this.context = c;
        this.placeDetailsTitle = placeDetailsTitle;
        this.placeDetailsInfo = placeDetailsInfo;
    }

    @Override
    public DetailsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.main_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DetailsListViewHolder viewHolder = new DetailsListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsListViewHolder holder, int position) {
        holder.titleTextView.setText((String)placeDetailsTitle.get(position));
        holder.infoTextView.setText((String) placeDetailsInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return placeDetailsInfo.size() - 1;
    }

    public class DetailsListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.details_screen_card_view_tv_title) TextView titleTextView;
        @BindView(R.id.details_screen_card_view_tv_info) TextView infoTextView;

        public DetailsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
