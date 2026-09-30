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
import com.mrudul.tourandtravel.driver.driver_models.DriverAssignPackageModel;

import java.util.ArrayList;

public class DriverAssignPackageAdapter extends RecyclerView.Adapter<DriverAssignPackageAdapter.ViewHolder> {

    Context context;
    ArrayList<DriverAssignPackageModel> packageList;

    public DriverAssignPackageAdapter(Context context,ArrayList<DriverAssignPackageModel> packageList){
        this.context = context;
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public DriverAssignPackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_assign_package_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAssignPackageAdapter.ViewHolder holder, int position) {

        DriverAssignPackageModel packageModel = packageList.get(position);
        holder.packageName.setText(packageModel.getPackageName());
        holder.noOfPassenger.setText("Passengers : "+ packageModel.getNoOfPassenger());
        holder.pickUpAddress.setText(packageModel.getPickUpAddress());
        holder.guideName.setText(packageModel.getGuideName());
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView packageName,noOfPassenger,pickUpAddress,guideName;
        AppCompatButton acceptPackageBtn;

        public ViewHolder(@NonNull View view) {
            super(view);

            packageName = view.findViewById(R.id.tvPackageTitle);
            noOfPassenger = view.findViewById(R.id.tvPassengerCount);
            pickUpAddress = view.findViewById(R.id.tvPickupLocation);
            guideName = view.findViewById(R.id.tvTourGuideAssigned);
            acceptPackageBtn = view.findViewById(R.id.btnAcceptPackage);
        }
    }
}
