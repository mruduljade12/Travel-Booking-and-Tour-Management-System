package com.mrudul.tourandtravel.user_models;

public class UserModel {
    String userId,uri,userName,phone,email,address;

    public UserModel(){}

    public UserModel(String userId, String uri, String userName, String email, String phone, String address) {
        this.userId = userId;
        this.uri = uri;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
