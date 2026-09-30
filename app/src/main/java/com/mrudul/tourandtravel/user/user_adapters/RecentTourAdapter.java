package com.mrudul.tourandtravel.user.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_models.UserTourPackages;

import java.util.ArrayList;

public class RecentTourAdapter extends RecyclerView.Adapter<RecentTourAdapter.ViewHolder> {

    Context context;
    ArrayList<UserTourPackages> arrayList;

    public RecentTourAdapter(Context context, ArrayList<UserTourPackages> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecentTourAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_tour_package,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTourAdapter.ViewHolder holder, int position) {

        UserTourPackages recentTour = arrayList.get(position);

        holder.img.setImageResource(recentTour.getImage());
        holder.name.setText(recentTour.getName());
        holder.duration.setText(String.valueOf(recentTour.getDuration()));
        holder.rating.setText(String.valueOf(recentTour.getRating()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name,duration,rating;

        public ViewHolder(@NonNull View view) {
            super(view);

            img = view.findViewById(R.id.popPlaceImg);
            name = view.findViewById(R.id.placeName);
            duration = view.findViewById(R.id.duration);
            rating = view.findViewById(R.id.pRating);

        }
    }
}
