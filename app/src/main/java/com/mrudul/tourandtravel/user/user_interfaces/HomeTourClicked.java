package com.mrudul.tourandtravel.user.user_interfaces;

import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;

public interface HomeTourClicked {

    void onHomeTourClicked(TourPackageResponse response);
    void onRecentHomeItemClicked(TourPackageResponse response);
    void onMapEventClicked(PackageDestinationResponse response);
}
