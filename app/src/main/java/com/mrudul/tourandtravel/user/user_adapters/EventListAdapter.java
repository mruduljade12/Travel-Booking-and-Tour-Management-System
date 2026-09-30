package com.mrudul.tourandtravel.user.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_models.EventModel;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    Context context;
    ArrayList<EventModel> arrayList;

    public EventListAdapter(Context context,ArrayList<EventModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {

        EventModel events = arrayList.get(position);

        holder.event.setText(events.getNAME());
        holder.date.setText(events.getDATE());
        holder.time.setText(events.getTIME());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView event,date,time;

        public ViewHolder(@NonNull View view) {
            super(view);

            event = view.findViewById(R.id.eventName);
            date = view.findViewById(R.id.eventDate);
            time = view.findViewById(R.id.eventTime);
        }
    }
}
