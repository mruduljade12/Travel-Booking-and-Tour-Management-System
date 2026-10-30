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
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;

import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    Context context;
    ArrayList<PackageDestinationResponse> list;

    public DestinationAdapter(Context context, ArrayList<PackageDestinationResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DestinationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.destination_data_display_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationAdapter.ViewHolder holder, int position) {

        PackageDestinationResponse model = list.get(position);

        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(holder.image);

        holder.name.setText(model.getName());
        holder.address.setText(model.getCity()+","+model.getCountry());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,address;

        public ViewHolder(@NonNull View view) {
            super(view);

            image = view.findViewById(R.id.ivDestination);
            name = view.findViewById(R.id.tvDestinationName);
            address = view.findViewById(R.id.tvDestinationAddress);
        }
    }
}
