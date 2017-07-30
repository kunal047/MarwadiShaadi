package com.example.sid.marwadishaadi.Dashboard_Favourites;

/**
 * Created by pranay on 01-06-2017.
 */

public class FavouriteModel {
    private String name, location, highest_degree, imageurl, customerId, interestStatus;
    private int age;

    public FavouriteModel(String customerId, String name, String location, String highest_degree, int age, String imageurl, String interestStatus) {

        this.customerId = customerId;
        this.name = name;
        this.location = location;
        this.highest_degree = highest_degree;
        this.age = age;
        this.imageurl = imageurl;
        this.interestStatus = interestStatus;
    }

    public String getInterestStatus() {
        return interestStatus;
    }

    public void setInterestStatus(String interestStatus) {
        this.interestStatus = interestStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHighest_degree() {
        return highest_degree;
    }

    public void setHighest_degree(String highest_degree) {
        this.highest_degree = highest_degree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
