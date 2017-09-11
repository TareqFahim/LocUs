package com.example.amrfahim.capstoneproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amrfahim.capstoneproject.R;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amr Fahim on 9/9/2017.
 */

public class TimeTableListAdapter extends RecyclerView.Adapter<TimeTableListAdapter.TimeTableViewHolder> {
    List<Boolean> timeAvailability = new ArrayList<>();
    List<Integer> time = new ArrayList();
    Context context;

    public TimeTableListAdapter(Context c, List time, List timeAvailability) {
        this.context = c;
        this.time = time;
        this.timeAvailability = timeAvailability;
    }

    @Override
    public TimeTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.time_table_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TimeTableViewHolder viewHolder = new TimeTableViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeTableViewHolder holder, int position) {
        String timeStr;
        if (time.get(position) == 12) {
            timeStr = "12" + " P.M";
        } else if (time.get(position) == 24) {
            timeStr = "12" + " A.M";
        } else if (time.get(position) > 12) {
            timeStr = Integer.toString(time.get(position) - 12) + " P.M";
        } else {
            timeStr = Integer.toString(time.get(position)) + " A.M";
        }
        holder.timeTextView.setText(timeStr);

        if (timeAvailability.get(position) == true) {
            holder.avalImageView.setImageResource(R.drawable.ic_check_circle_green_24dp);
        } else {
            holder.avalImageView.setImageResource(R.drawable.ic_cancel_red_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return timeAvailability.size();
    }

    public class TimeTableViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time_table_tv)
        TextView timeTextView;
        @BindView(R.id.time_table_image_view)
        ImageView avalImageView;

        public TimeTableViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
