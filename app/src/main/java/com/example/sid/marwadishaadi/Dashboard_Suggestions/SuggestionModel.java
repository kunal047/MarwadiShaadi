package com.example.sid.marwadishaadi.Dashboard_Suggestions;

/**
 * Created by Sid on 02-Jun-17.
 */

public class SuggestionModel {
    String imgAdd, name, cusId, highDeg, workLoc, height, company, annInc, mariSta, hometown, designation, favouriteStatus, interestStatus;
    int age;

    public SuggestionModel(int age, String imgAdd, String name, String cusId, String highDeg, String workLoc, String height, String comapany, String annInc, String mariSta, String hometown, String designation, String favouriteStatus, String interestStatus) {
        this.imgAdd = imgAdd;
        this.age = age;
        this.name = name;
        this.cusId = cusId;
        this.highDeg = highDeg;
        this.workLoc = workLoc;
        this.height = height;
        this.company = comapany;
        this.annInc = annInc;
        this.mariSta = mariSta;
        this.hometown = hometown;
        this.designation = designation;
        this.favouriteStatus = favouriteStatus;
        this.interestStatus = interestStatus;

    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }

    public String getInterestStatus() {
        return interestStatus;
    }

    public void setInterestStatus(String interestStatus) {
        this.interestStatus = interestStatus;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCompany() {

        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImgAdd() {
        return imgAdd;
    }

    public void setImgAdd(String imgAdd) {
        this.imgAdd = imgAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }


    public String getHighDeg() {
        return highDeg;
    }

    public void setHighDeg(String highDeg) {
        this.highDeg = highDeg;
    }

    public String getWorkLoc() {
        return workLoc;
    }

    public void setWorkLoc(String workLoc) {
        this.workLoc = workLoc;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getComapany() {
        return company;
    }

    public void setComapany(String comapany) {
        this.company = comapany;
    }


    public String getAnnInc() {
        return annInc;
    }

    public void setAnnInc(String annInc) {
        this.annInc = annInc;
    }

    public String getMariSta() {
        return mariSta;
    }

    public void setMariSta(String mariSta) {
        this.mariSta = mariSta;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
}
