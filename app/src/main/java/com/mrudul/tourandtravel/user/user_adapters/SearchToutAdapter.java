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
import com.mrudul.tourandtravel.user.user_interfaces.SearchItemClick;
import com.mrudul.tourandtravel.user.user_models.SearchTourModel;

import java.util.ArrayList;

public class SearchToutAdapter extends RecyclerView.Adapter<SearchToutAdapter.ViewHolder> {


    Context context;
    ArrayList<SearchTourModel> tourList;
    SearchItemClick itemClick;



    public SearchToutAdapter(Context context,ArrayList<SearchTourModel> tourList,SearchItemClick itemClick){
        this.context = context;
        this.tourList = tourList;
        this.itemClick = itemClick;
    }



    @NonNull
    @Override
    public SearchToutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_item_cards,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchToutAdapter.ViewHolder holder, int position) {


        SearchTourModel searchModel = tourList.get(position);

        holder.tourName.setText(searchModel.getToutPackageName());
        holder.tourPrice.setText(String.valueOf(searchModel.getTourPackagePrice()));
        holder.tourDuration.setText(String.valueOf(searchModel.getTourPackageDuration()));
        holder.tourRating.setText(String.valueOf(searchModel.getTourPackageRating()));

        Glide.with(context)
                        .load(searchModel.getTourImageUri())
                                .error(R.drawable.advertize)
                                        .placeholder(R.drawable.advertize)
                                                .into(holder.tourImage);

        holder.itemView.setOnClickListener(v->{
            itemClick.onSearchItemClick(searchModel);
        });

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView tourImage;
        TextView tourName,tourPrice,tourDuration,tourRating;



        public ViewHolder(@NonNull View view) {
            super(view);

            tourImage = view.findViewById(R.id.searchTourImage);
            tourName = view.findViewById(R.id.searchTourName);
            tourPrice = view.findViewById(R.id.searchTourPrice);
            tourDuration = view.findViewById(R.id.searchTourDuration);
            tourRating = view.findViewById(R.id.searchTourRating);
        }
    }
}
