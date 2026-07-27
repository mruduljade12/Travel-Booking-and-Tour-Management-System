package com.mrudul.tourandtravel.user_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user_models.UserTourPackages;

import java.util.ArrayList;

public class UserTourPackageAdapter extends RecyclerView.Adapter<UserTourPackageAdapter.ViewHolder> {

    Context context;
    ArrayList<UserTourPackages> arrayList;

    public UserTourPackageAdapter(Context context,ArrayList<UserTourPackages> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public UserTourPackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.popular_tour_package,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTourPackageAdapter.ViewHolder holder, int position) {

        UserTourPackages userPackage = arrayList.get(position);

        holder.img.setImageResource(userPackage.getImage());
        holder.name.setText(userPackage.getName());
        holder.duration.setText(String.valueOf(userPackage.getDuration()));
        holder.rating.setText(String.valueOf(userPackage.getRating()));
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
