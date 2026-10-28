package com.mrudul.tourandtravel.driver.driver_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_models.DriverTripModel;

import java.util.ArrayList;

/**
 * RecyclerView adapter for the Driver Schedule screen.
 * Displays Ongoing and Completed trip cards.
 */
public class DriverTripAdapter extends RecyclerView.Adapter<DriverTripAdapter.ViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(DriverTripModel trip);
    }

    private final Context                   context;
    private final ArrayList<DriverTripModel> list;
    private final OnTripClickListener       listener;

    public DriverTripAdapter(Context context,
                             ArrayList<DriverTripModel> list,
                             OnTripClickListener listener) {
        this.context  = context;
        this.list     = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_driver_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        DriverTripModel trip = list.get(position);

        h.tvTime.setText(trip.getTime());
        h.tvPickup.setText(trip.getPickupLocation());
        h.tvDestination.setText(trip.getDestination());
        h.tvSeats.setText(trip.getSeats() + " Seats");
        h.tvFare.setText(trip.getFare());
        h.tvDistance.setText(trip.getDistance());

        // Badge styling
        if (trip.getStatus() == DriverTripModel.STATUS_COMPLETED) {
            h.tvBadge.setText(R.string.sched_badge_completed);
            h.tvBadge.setTextColor(ContextCompat.getColor(context, R.color.sched_badge_completed_text));
            h.tvBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_completed));
        } else {
            h.tvBadge.setText(R.string.sched_badge_ongoing);
            h.tvBadge.setTextColor(ContextCompat.getColor(context, R.color.sched_badge_ongoing_text));
            h.tvBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_sched_badge_ongoing));
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(trip);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvBadge, tvPickup, tvDestination,
                 tvSeats, tvFare, tvDistance;

        ViewHolder(@NonNull View view) {
            super(view);
            tvTime        = view.findViewById(R.id.tv_trip_time);
            tvBadge       = view.findViewById(R.id.tv_trip_badge);
            tvPickup      = view.findViewById(R.id.tv_trip_pickup);
            tvDestination = view.findViewById(R.id.tv_trip_destination);
            tvSeats       = view.findViewById(R.id.tv_trip_seats);
            tvFare        = view.findViewById(R.id.tv_trip_fare);
            tvDistance    = view.findViewById(R.id.tv_trip_distance);
        }
    }
}