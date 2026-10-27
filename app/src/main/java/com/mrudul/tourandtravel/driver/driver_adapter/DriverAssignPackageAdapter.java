package com.mrudul.tourandtravel.driver.driver_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_interfaces.DriverPackageClickListener;
import com.mrudul.tourandtravel.driver.driver_interfaces.OnDriverHomeClick;
import com.mrudul.tourandtravel.driver.driver_models.DriverAssignPackageModel;
import com.mrudul.tourandtravel.user.user_interfaces.HomeTourClicked;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;

import java.util.ArrayList;

public class DriverAssignPackageAdapter extends RecyclerView.Adapter<DriverAssignPackageAdapter.ViewHolder> {

    Context context;
    ArrayList<TourPackageResponse> packageList;
    HomeTourClicked listener;

    public DriverAssignPackageAdapter(Context context,ArrayList<TourPackageResponse> packageList,HomeTourClicked listener){
        this.context = context;
        this.packageList = packageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DriverAssignPackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_assign_package_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAssignPackageAdapter.ViewHolder holder, int position) {

        TourPackageResponse packageModel = packageList.get(position);
        holder.packageName.setText(packageModel.getPackName());
        holder.noOfPassenger.setText("Passengers : "+ packageModel.getPackMaxCapacity());
        holder.guideName.setText(packageModel.getGuide().getGuideName());

        holder.itemView.setOnClickListener(v->{
            listener.onRecentHomeItemClicked(packageModel);
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView packageName,noOfPassenger,guideName;

        public ViewHolder(@NonNull View view) {
            super(view);

            packageName = view.findViewById(R.id.tvPackageTitle);
            noOfPassenger = view.findViewById(R.id.tvPassengerCount);
            guideName = view.findViewById(R.id.tvTourGuideAssigned);
        }
    }
}
