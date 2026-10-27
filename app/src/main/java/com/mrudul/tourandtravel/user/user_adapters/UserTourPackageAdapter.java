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
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.user.user_models.UserTourPackages;

import java.util.ArrayList;

public class UserTourPackageAdapter extends RecyclerView.Adapter<UserTourPackageAdapter.ViewHolder> {

    Context context;
    ArrayList<TourPackageResponse> arrayList;
    HomeTourClicked listener;

    public UserTourPackageAdapter(Context context,ArrayList<TourPackageResponse> arrayList,HomeTourClicked listener){
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserTourPackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.popular_tour_package,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTourPackageAdapter.ViewHolder holder, int position) {

        TourPackageResponse userPackage = arrayList.get(position);

        Glide.with(context)
                .load(userPackage.getPackThumbnail())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(holder.img);

        holder.name.setText(userPackage.getPackName());
        holder.duration.setText(String.valueOf(userPackage.getPackDurationDay()));
        holder.rating.setText(String.valueOf(userPackage.getPackRating()));


        holder.itemView.setOnClickListener(v->{
            listener.onHomeTourClicked(userPackage);
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
