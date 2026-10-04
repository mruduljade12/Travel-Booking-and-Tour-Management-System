package com.mrudul.tourandtravel.user.user_models;

public class CustomerResponse {

    private Integer custId;
    private String custName;
    private String custEmail;
    private String custPhone;
    private String custAddress;
    private String custGender;
    private String imageUrl;

    public CustomerResponse() {
    }


    public CustomerResponse(Integer custId, String custName, String custEmail, String custPhone, String custAddress, String custGender, String imageUrl) {
        this.custId = custId;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.custGender = custGender;
        this.imageUrl = imageUrl;
    }


    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
