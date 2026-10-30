package com.mrudul.tourandtravel.tour_guide.tour_guide_models;

public class TourGuideLoginRequest {

    private String tourGuideEmail;
    private String accessCode;

    public TourGuideLoginRequest(){}

    public TourGuideLoginRequest(String tourGuideEmail, String accessCode) {
        this.tourGuideEmail = tourGuideEmail;
        this.accessCode = accessCode;
    }

    public String getTourGuideEmail() {
        return tourGuideEmail;
    }

    public void setTourGuideEmail(String tourGuideEmail) {
        this.tourGuideEmail = tourGuideEmail;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
