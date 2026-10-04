package com.mrudul.tourandtravel.driver.driver_models;

public class DriverLoginRequest {

    private String driverEmail;
    private String accessCode;

    public DriverLoginRequest() {
    }

    public DriverLoginRequest(String driverEmail, String accessCode) {
        this.driverEmail = driverEmail;
        this.accessCode = accessCode;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
