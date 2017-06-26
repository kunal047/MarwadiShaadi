package com.example.sid.marwadishaadi.Dashboard_Interest_Received;

/**
 * Created by USER on 01-06-2017.
 */

public class InterestReceivedModel {

    private String customerId;
    private String name, age, highestDegree, location;
    private String userImage;


    private int status;

    public InterestReceivedModel(String customerId, String name, String age, String highestDegree, String location, String userImage, int status) {

        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.highestDegree = highestDegree;
        this.location = location;
        this.userImage = userImage;
        this.status = status;

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}