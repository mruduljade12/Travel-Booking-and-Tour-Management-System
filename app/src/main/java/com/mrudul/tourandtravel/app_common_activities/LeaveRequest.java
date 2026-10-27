package com.mrudul.tourandtravel.app_common_activities;

public class LeaveRequest {

    private Integer driverId;
    private Integer guideId;

    private String startDate;
    private String endDate;

    private String subject;
    private String description;

    public LeaveRequest(
            Integer driverId,
            Integer guideId,
            String startDate,
            String endDate,
            String subject,
            String description) {

        this.driverId = driverId;
        this.guideId = guideId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subject = subject;
        this.description = description;
    }

    // getters and setters

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getGuideId() {
        return guideId;
    }

    public void setGuideId(Integer guideId) {
        this.guideId = guideId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
