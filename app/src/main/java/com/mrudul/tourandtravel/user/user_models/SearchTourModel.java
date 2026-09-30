package com.mrudul.tourandtravel.user.user_models;

import android.net.Uri;

public class SearchTourModel {
    String tourPackageId,toutPackageName;
    int tourImageUri;
    int tourPackagePrice;
    float tourPackageRating,tourPackageDuration;

    public SearchTourModel(){}

    public SearchTourModel(String tourPackageId, int tourImageUri, String toutPackageName, int tourPackagePrice, float tourPackageDuration, float tourPackageRating) {
        this.tourPackageId = tourPackageId;
        this.tourImageUri = tourImageUri;
        this.toutPackageName = toutPackageName;
        this.tourPackagePrice = tourPackagePrice;
        this.tourPackageDuration = tourPackageDuration;
        this.tourPackageRating = tourPackageRating;
    }

    public int getTourImageUri() {
        return tourImageUri;
    }

    public String getTourPackageId() {
        return tourPackageId;
    }

    public String getToutPackageName() {
        return toutPackageName;
    }

    public int getTourPackagePrice() {
        return tourPackagePrice;
    }

    public float getTourPackageRating() {
        return tourPackageRating;
    }

    public float getTourPackageDuration() {
        return tourPackageDuration;
    }

}
