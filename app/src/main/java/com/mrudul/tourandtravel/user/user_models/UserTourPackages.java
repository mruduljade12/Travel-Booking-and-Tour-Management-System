package com.mrudul.tourandtravel.user.user_models;

public class UserTourPackages {

    //variables of model class
    private final int IMAGE;
    private final float DURATION,RATING;
    private final String NAME;

    public UserTourPackages(int image,String name,float duration,float rating){

        //this is a parameterized constructor to set the values
        this.IMAGE = image;
        this.NAME = name;
        this.DURATION = duration;
        this.RATING = rating;
    }


    //getter methods of the model class
    public int getImage(){
        return IMAGE;
    }

    public String getName(){
        return NAME;
    }

    public float getDuration(){
        return DURATION;
    }

    public float getRating(){
        return RATING;
    }
}
