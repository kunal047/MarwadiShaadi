package com.sid.marwadishaadi;

/**
 * Created by Sid on 01-Jul-17.
 */

public class Place {

    private String country;
    private String city;
    private String state;

    public Place(String city, String state, String country) {
        this.country = country;
        this.city = city;
        this.state = state;
    }

    public String getPlace() {
        return this.city + ", " + this.state + ", " + this.country;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
