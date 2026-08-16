package com.mrudul.tourandtravel.user_models;

public class SearchTourModel {
    String tourPackageId,toutPackageName;
    int tourPackagePrice;
    float tourPackageRating,tourPackageDuration;

    public SearchTourModel(){}

    public SearchTourModel(String tourPackageId, String toutPackageName, int tourPackagePrice, float tourPackageDuration, float tourPackageRating) {
        this.tourPackageId = tourPackageId;
        this.toutPackageName = toutPackageName;
        this.tourPackagePrice = tourPackagePrice;
        this.tourPackageDuration = tourPackageDuration;
        this.tourPackageRating = tourPackageRating;
    }

    public String getTourPackageId() {
        return tourPackageId;
    }

    public void setTourPackageId(String tourPackageId) {
        this.tourPackageId = tourPackageId;
    }

    public String getToutPackageName() {
        return toutPackageName;
    }

    public void setToutPackageName(String toutPackageName) {
        this.toutPackageName = toutPackageName;
    }

    public int getTourPackagePrice() {
        return tourPackagePrice;
    }

    public void setTourPackagePrice(int tourPackagePrice) {
        this.tourPackagePrice = tourPackagePrice;
    }

    public float getTourPackageRating() {
        return tourPackageRating;
    }

    public void setTourPackageRating(float tourPackageRating) {
        this.tourPackageRating = tourPackageRating;
    }

    public float getTourPackageDuration() {
        return tourPackageDuration;
    }

    public void setTourPackageDuration(float tourPackageDuration) {
        this.tourPackageDuration = tourPackageDuration;
    }
}
