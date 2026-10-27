package com.mrudul.tourandtravel.user.user_models;

public class BookingRequest {

    private Integer packageId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Integer numberOfPeople;
    private String packageName;
    private Integer totalPrice;

    public BookingRequest(){}


    public BookingRequest(Integer packageId, String customerName, String customerEmail, String customerPhone, Integer numberOfPeople, String packageName, Integer totalPrice) {
        this.packageId = packageId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.numberOfPeople = numberOfPeople;
        this.packageName = packageName;
        this.totalPrice = totalPrice;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
