package com.mrudul.tourandtravel.user.user_models;

public class CustomerRegisterRequest {


    private String custName;
    private String custEmail;
    private String custPhone;
    private String custAddress;
    private String custGender;
    private String password;
    private String imageUrl;


    public CustomerRegisterRequest(){}


    public CustomerRegisterRequest(String custName, String custEmail, String custPhone, String custAddress, String custGender, String password, String imageUrl) {
        this.custName = custName;
        this.custEmail = custEmail;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.custGender = custGender;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
