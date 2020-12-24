package com.example.s1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TripAdapter {
    private LayoutInflater inflater;
    private int layout;
    private List<Trip> trips;

    public TripAdapter(Context context, int resource, List<Trip> trips) {
        this.trips = trips;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView modelView = (TextView) view.findViewById(R.id.model_textView);
        TextView timeView = (TextView) view.findViewById(R.id.time_textView);
        TextView sumView = (TextView) view.findViewById(R.id.sum_textView);

        Trip trip = trips.get(position);

        modelView.setText(trip.getModel());
        timeView.setText(trip.getTime());
        sumView.setText(trip.getSum());

        return view;
    }
}
