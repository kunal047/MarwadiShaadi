package com.sid.marwadishaadi.Similar_Profiles;

/**
 * Created by Lawrence Dalmet on 13-06-2017.
 */

public class SimilarModel {

    String name, city, education, imgAdd, age;
    String customer_id;

    public SimilarModel(String name, String city, String education, String imgAdd, String age, String customer_id) {
        this.customer_id = customer_id;
        this.name = name;
        this.city = city;
        this.education = education;
        this.imgAdd = imgAdd;
        this.age = age;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

