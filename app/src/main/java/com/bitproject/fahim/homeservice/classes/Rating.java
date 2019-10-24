package com.bitproject.fahim.homeservice.classes;

public class Rating {
    private String rating_id, sp_id;
    private float rating_value;

    public Rating(String rating_id, String sp_id, float rating_value) {
        this.rating_id = rating_id;
        this.sp_id = sp_id;
        this.rating_value = rating_value;
    }

    public Rating() {

    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public float getRating_value() {
        return rating_value;
    }

    public void setRating_value(float rating_value) {
        this.rating_value = rating_value;
    }
}
