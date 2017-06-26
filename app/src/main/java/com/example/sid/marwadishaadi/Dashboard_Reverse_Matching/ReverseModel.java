package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

/**
 * Created by Sid on 31-May-17.
 */

public class ReverseModel {

    private String img_url;
    private String name,city,education;
    private int age;

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

    public ReverseModel(String img_url, String name, String city, String education, int age) {

        this.img_url = img_url;
        this.name = name;
        this.city = city;
        this.education = education;
        this.age = age;
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


}
