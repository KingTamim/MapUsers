package com.mangolab.mapusers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tamim on 8/8/2017.
 */

public class CustomAdapter extends ArrayAdapter<Location_Tracker> {

    private Activity context;
    private List<Location_Tracker> location_trackers;


    public CustomAdapter( Activity context, List<Location_Tracker> location_trackers) {
        super(context, R.layout.background_location_lists,location_trackers);
        this.context = context;
        this.location_trackers = location_trackers;
    }

 /*   @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.background_location_lists,null, true);
        TextView textViewLat =  listViewItem.findViewById(R.id.latitude);
        TextView textViewLng =  listViewItem.findViewById(R.id.longitude);

        Location_Tracker locations = location_trackers.get(position);
        *//*textViewLat.setText(locations.getLatitude());
        textViewLng.setText(locations.getLongitude());*//*


    }*/
}
