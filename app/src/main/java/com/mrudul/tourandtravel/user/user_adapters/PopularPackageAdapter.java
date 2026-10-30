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
import com.mrudul.tourandtravel.user.user_models.TourPackageModel;

import java.util.ArrayList;

/**
 * Adapter for the Popular Packages RecyclerView in SearchFragment.
 * Uses item_popular_package.xml.
 * Lives in the existing user_adapters package.
 */
public class PopularPackageAdapter
        extends RecyclerView.Adapter<PopularPackageAdapter.ViewHolder> {

    public interface OnPackageClickListener {
        void onPackageClick(TourPackageModel pkg);
    }

    private final Context                     ctx;
    private final ArrayList<TourPackageModel> list;
    private final OnPackageClickListener      listener;

    public PopularPackageAdapter(Context ctx,
                                  ArrayList<TourPackageModel> list,
                                  OnPackageClickListener listener) {
        this.ctx      = ctx;
        this.list     = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_popular_package, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TourPackageModel pkg = list.get(position);

        // Load image — if imageUrl is set use Glide URL, otherwise use drawable
        if (pkg.getImageUrl() != null && !pkg.getImageUrl().isEmpty()) {
            Glide.with(ctx)
                    .load(pkg.getImageUrl())
                    .placeholder(R.drawable.pkg_image_bg)
                    .error(R.drawable.pkg_image_bg)
                    .centerCrop()
                    .into(h.image);
        } else {
            // Use pre-set drawable resource id stored in image field
            h.image.setImageResource(pkg.getImage());
            h.image.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        }

        h.name.setText(pkg.getTitle());
        h.duration.setText(pkg.getDuration());
        h.location.setText(pkg.getLocation());
        h.price.setText("\u20B9 " + pkg.getPrice());
        h.rating.setText(pkg.getRating() + " (" + pkg.getReviewCount() + " reviews)");

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPackageClick(pkg);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView  name, duration, location, price, rating;

        ViewHolder(@NonNull View v) {
            super(v);
            image    = v.findViewById(R.id.popPkgImage);
            name     = v.findViewById(R.id.popPkgName);
            duration = v.findViewById(R.id.popPkgDuration);
            location = v.findViewById(R.id.popPkgLocation);
            price    = v.findViewById(R.id.popPkgPrice);
            rating   = v.findViewById(R.id.popPkgRating);
        }
    }
}