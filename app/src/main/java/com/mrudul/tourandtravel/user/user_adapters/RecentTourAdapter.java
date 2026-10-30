package com.mrudul.tourandtravel.user.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_interfaces.HomeTourClicked;
import com.mrudul.tourandtravel.user.user_models.BookingResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.user.user_models.UserTourPackages;

import java.util.ArrayList;

public class RecentTourAdapter extends RecyclerView.Adapter<RecentTourAdapter.ViewHolder> {

    Context context;
    ArrayList<TourPackageResponse> arrayList;
    HomeTourClicked listener;

    public RecentTourAdapter(Context context, ArrayList<TourPackageResponse> arrayList,HomeTourClicked listener){
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentTourAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_tour_package,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTourAdapter.ViewHolder holder, int position) {

        TourPackageResponse recentTour = arrayList.get(position);

        Glide.with(context)
                .load(recentTour.getPackThumbnail())
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(holder.img);

        holder.name.setText(recentTour.getPackName());
        holder.duration.setText(String.valueOf(recentTour.getPackDurationDay()));
        holder.rating.setText(String.valueOf(recentTour.getPackRating()));


        holder.itemView.setOnClickListener(v->{
            listener.onRecentHomeItemClicked(recentTour);
        });

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
