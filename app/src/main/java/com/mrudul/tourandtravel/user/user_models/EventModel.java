package com.mrudul.tourandtravel.user.user_models;

import java.util.Timer;

public class EventModel {
    private final String NAME,DATE,TIME;

    public EventModel(String name,String date,String time){
        this.NAME = name;
        this.DATE = date;
        this.TIME = time;
    }

    public String getNAME(){
        return NAME;
    }

    public String getDATE(){
        return DATE;
    }

    public String getTIME(){
        return TIME;
    }
}
