package com.mrudul.tourandtravel.user.user_models;

import android.net.Uri;

public class SearchTourModel {
    String package_id,pack_name,pack_status,pack_create_date,pack_description;
    int pack_thumbnail;
    int pack_price,pack_max_capacity;
    float tourPackageRating,pack_duration_day,pack_duration_night,pack_rating;

    public SearchTourModel(){}

    public SearchTourModel(String package_id,int pack_thumbnail,String pack_name,String pack_status,float pack_rating,
                           int pack_price,float pack_duration_day,float pack_duration_night,int pack_max_capacity,String pack_description){
        this.package_id = package_id;
        this.pack_thumbnail = pack_thumbnail;
        this.pack_name = pack_name;
        this.pack_status = pack_status;
        this.pack_rating = pack_rating;
        this.pack_price = pack_price;
        this.pack_duration_day = pack_duration_day;
        this.pack_duration_night = pack_duration_night;
        this.pack_max_capacity = pack_max_capacity;
        this.pack_description = pack_description;
    }


    public String getPackage_id() {
        return package_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public String getPack_status() {
        return pack_status;
    }

    public String getPack_create_date() {
        return pack_create_date;
    }

    public String getPack_description() {
        return pack_description;
    }

    public int getPack_thumbnail() {
        return pack_thumbnail;
    }

    public int getPack_price() {
        return pack_price;
    }

    public int getPack_max_capacity() {
        return pack_max_capacity;
    }

    public float getTourPackageRating() {
        return tourPackageRating;
    }

    public float getPack_duration_day() {
        return pack_duration_day;
    }

    public float getPack_duration_night() {
        return pack_duration_night;
    }

    public float getPack_rating() {
        return pack_rating;
    }
}
