package com.mrudul.tourandtravel.user.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_interfaces.HomeTourClicked;
import com.mrudul.tourandtravel.user.user_models.EventModel;
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    Context context;
    ArrayList<PackageDestinationResponse> arrayList;
    HomeTourClicked listener;

    public EventListAdapter(Context context,ArrayList<PackageDestinationResponse> arrayList,HomeTourClicked listener){
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {

        PackageDestinationResponse events = arrayList.get(position);

        holder.event.setText(events.getName());
        holder.date.setText(events.getVisitDate());


        holder.itemView.setOnClickListener(v->{
            listener.onMapEventClicked(events);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView event,date;

        public ViewHolder(@NonNull View view) {
            super(view);

            event = view.findViewById(R.id.eventName);
            date = view.findViewById(R.id.eventDate);
        }
    }
}
