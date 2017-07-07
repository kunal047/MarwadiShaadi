package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

/**
 * Created by Sid on 31-May-17.
 */

public class ReverseModel {

    private String img_url;
    private String name,customerID;
    private int age;
    private String educationDegree, location;

    public ReverseModel(String img_url, String name, String customerID, int age, String educationDegree, String location) {
        this.img_url = img_url;
        this.name = name;
        this.customerID = customerID;
        this.age = age;
        this.educationDegree = educationDegree;
        this.location = location;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
