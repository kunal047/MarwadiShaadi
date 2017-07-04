package com.example.sid.marwadishaadi.Search;

/**
 * Created by vivonk on 05-06-2017.
 */

public class User {
    public String name;
    public String hometown;
     boolean box;

    public User(String name, String hometown) {
        this.name = name;
        this.hometown = hometown;
    }

    public User(String name, String hometown, boolean box) {
        this.name = name;
        this.hometown = hometown;
        this.box = box;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public boolean isBox() {

        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }
}
