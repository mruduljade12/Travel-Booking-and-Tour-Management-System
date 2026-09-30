package com.mrudul.tourandtravel.driver.driver_models;

public class DriverHomePageData {

    String destinationName,tourGuideName;

    public DriverHomePageData(){}

    public DriverHomePageData(String destinationName,String tourGuideName){
        this.destinationName = destinationName;
        this.tourGuideName = tourGuideName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }
}
