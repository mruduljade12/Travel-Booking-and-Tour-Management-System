package com.mrudul.tourandtravel.user.user_models;

public class CustomerLoginRequest {


    private String custEmail;
    private String password;

    public CustomerLoginRequest() {
    }

    public CustomerLoginRequest(String custEmail, String password) {
        this.custEmail = custEmail;
        this.password = password;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
