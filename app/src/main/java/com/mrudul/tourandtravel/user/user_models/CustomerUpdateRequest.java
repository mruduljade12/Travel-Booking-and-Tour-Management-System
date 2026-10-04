package com.mrudul.tourandtravel.user.user_models;

public class CustomerUpdateRequest {

    private String custEmail;
    private String custPhone;
    private String custAddress;
    private String imageUrl;

    public CustomerUpdateRequest() {
    }

    public CustomerUpdateRequest(String custEmail, String custPhone, String custAddress, String imageUrl) {
        this.custEmail = custEmail;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.imageUrl = imageUrl;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
