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
import com.mrudul.tourandtravel.user.user_models.TourPackageModel;

import java.util.ArrayList;

/**
 * Adapter for the Packages section in HomeFragment.
 * Each item uses item_tour_package.xml.
 * Lives in the existing user_adapters package.
 */
public class TourPackageAdapter extends RecyclerView.Adapter<TourPackageAdapter.ViewHolder> {

    private final Context                  context;
    private final ArrayList<TourPackageModel> list;

    public TourPackageAdapter(Context context, ArrayList<TourPackageModel> list) {
        this.context = context;
        this.list    = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_tour_package, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TourPackageModel pkg = list.get(position);

        h.image.setImageResource(pkg.getImage());
        h.title.setText(pkg.getTitle());
        h.subtitle.setText(pkg.getSubtitle());
        h.duration.setText(pkg.getDuration());
        h.location.setText(pkg.getLocation());
        h.group.setText(pkg.getGroupSize());
        h.price.setText(pkg.getPrice());
        h.rating.setText(String.valueOf(pkg.getRating()));
        h.badge.setText(pkg.getCategory());

        // Category badge colour
        switch (pkg.getCategory()) {
            case "Festival Special":
                h.badge.setBackgroundResource(R.drawable.pkg_badge_festival);
                break;
            case "Holiday Package":
                h.badge.setBackgroundResource(R.drawable.pkg_badge_holiday);
                break;
            case "Adventure":
                h.badge.setBackgroundResource(R.drawable.pkg_badge_adventure);
                break;
            default:
                h.badge.setBackgroundResource(R.drawable.pkg_badge_popular);
                break;
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView  title, subtitle, duration, location, group, price, rating, badge;

        public ViewHolder(@NonNull View v) {
            super(v);
            image    = v.findViewById(R.id.pkgImage);
            title    = v.findViewById(R.id.pkgTitle);
            subtitle = v.findViewById(R.id.pkgSubtitle);
            duration = v.findViewById(R.id.pkgDuration);
            location = v.findViewById(R.id.pkgLocation);
            group    = v.findViewById(R.id.pkgGroup);
            price    = v.findViewById(R.id.pkgPrice);
            rating   = v.findViewById(R.id.pkgRating);
            badge    = v.findViewById(R.id.pkgCategoryBadge);
        }
    }
}