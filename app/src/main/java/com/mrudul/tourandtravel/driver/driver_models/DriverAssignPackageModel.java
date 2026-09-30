package com.mrudul.tourandtravel.driver.driver_models;

public class DriverAssignPackageModel {
    String packageName,pickUpAddress,guideName;
    int noOfPassenger;

    public DriverAssignPackageModel(String packageName,int noOfPassenger,String pickUpAddress,String guideName){
        this.packageName = packageName;
        this.noOfPassenger = noOfPassenger;
        this.pickUpAddress = pickUpAddress;
        this.guideName = guideName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public String getGuideName() {
        return guideName;
    }

    public int getNoOfPassenger() {
        return noOfPassenger;
    }
}
