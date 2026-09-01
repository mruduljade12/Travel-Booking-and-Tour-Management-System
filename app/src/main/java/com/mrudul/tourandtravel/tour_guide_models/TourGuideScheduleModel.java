package com.mrudul.tourandtravel.tour_guide_models;

public class TourGuideScheduleModel {
    String packageName,dateAndTime,groupDetails,driverName;

    public TourGuideScheduleModel(String packageName,String dateAndTime,String groupDetails,String driverName){
        this.packageName = packageName;
        this.dateAndTime = dateAndTime;
        this.groupDetails = groupDetails;
        this.driverName = driverName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getGroupDetails() {
        return groupDetails;
    }

    public String getDriverName() {
        return driverName;
    }
}
