package com.mrudul.tourandtravel.tour_guide.tour_guide_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.tour_guide.tour_guide_interfaces.TourGuidePackageClickListener;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideScheduleModel;

import java.util.ArrayList;

public class TourGuideScheduleAdapter extends RecyclerView.Adapter<TourGuideScheduleAdapter.ViewHolder> {

    Context context;
    ArrayList<TourGuideScheduleModel> packageList;
    TourGuidePackageClickListener listener;

    public TourGuideScheduleAdapter(Context context,ArrayList<TourGuideScheduleModel> packageList){
        this.context = context;
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public TourGuideScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour_guide_assignment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourGuideScheduleAdapter.ViewHolder holder, int position) {

        TourGuideScheduleModel tourModel = packageList.get(position);
        holder.packageName.setText(tourModel.getPackageName());
        holder.dateAndTime.setText(tourModel.getDateAndTime());
        holder.groupDetails.setText(tourModel.getGroupDetails());
        holder.driverName.setText(tourModel.getDriverName());


        holder.itemView.setOnClickListener(v->{
            listener.onTourGuidePackageClick(tourModel);
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView packageName,dateAndTime,groupDetails,driverName;
        AppCompatButton acceptBtn;

        public ViewHolder(@NonNull View view) {
            super(view);

            packageName = view.findViewById(R.id.tvTourName);
            dateAndTime = view.findViewById(R.id.tvTourTimeDate);
            groupDetails = view.findViewById(R.id.tvGroupDetails);
            driverName = view.findViewById(R.id.tvAssignedDriverName);
            acceptBtn = view.findViewById(R.id.btnAcceptTour);
        }
    }
}
