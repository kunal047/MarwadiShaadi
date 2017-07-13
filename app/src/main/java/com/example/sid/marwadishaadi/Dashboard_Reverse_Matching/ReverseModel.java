package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

/**
 * Created by Sid on 31-May-17.
 */

public class ReverseModel {

    private String img_url;
    private String name,customerID;
    private int age;
    private String educationDegree, location, customerNo;

    public ReverseModel(String img_url, String name, int age, String educationDegree, String location, String customerNo) {
        this.img_url = img_url;
        this.name = name;

        this.age = age;
        this.educationDegree = educationDegree;
        this.location = location;
        this.customerNo = customerNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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
    }}


