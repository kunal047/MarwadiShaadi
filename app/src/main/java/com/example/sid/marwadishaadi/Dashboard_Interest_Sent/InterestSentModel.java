package com.example.sid.marwadishaadi.Dashboard_Interest_Sent;

/**
 * Created by Lawrence Dalmet on 07-06-2017.
 */

public class InterestSentModel {
    String name, city, degree, imgAdd, reqStatus, date;
    int age;

    public InterestSentModel(String name, String city, String degree, String imgAdd, String reqStatus, int age, String date) {
        this.name = name;
        this.city = city;
        this.degree = degree;
        this.imgAdd = imgAdd;
        this.reqStatus = reqStatus;
        this.age = age;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getImgAdd() {
        return imgAdd;
    }

    public void setImgAdd(String imgAdd) {
        this.imgAdd = imgAdd;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
