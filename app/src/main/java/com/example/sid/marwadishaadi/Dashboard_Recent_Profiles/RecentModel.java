package com.example.sid.marwadishaadi.Dashboard_Recent_Profiles;

/**
 * Created by USER on 02-06-2017.
 */

public class RecentModel {

    private String recentName, recentAge, recentHighestDegree, recentLocation, recentOnline, recentCustomerId;
    private String recentUserImage;

    public RecentModel(String recentCustomerId, String recentName, String recentAge, String recentHighestDegree, String recentLocation, String recentOnline, String recentUserImage) {
        this.recentName = recentName;
        this.recentAge = recentAge;
        this.recentHighestDegree = recentHighestDegree;
        this.recentLocation = recentLocation;
        this.recentOnline = recentOnline;
        this.recentUserImage = recentUserImage;
    }

    public String getRecentCustomerId() {
        return recentCustomerId;
    }

    public void setRecentCustomerId(String recentCustomerId) {
        this.recentCustomerId = recentCustomerId;
    }

    public String getRecentName() {
        return recentName;
    }

    public void setRecentName(String recentName) {
        this.recentName = recentName;
    }

    public String getRecentAge() {
        return recentAge;
    }

    public void setRecentAge(String recentAge) {
        this.recentAge = recentAge;
    }

    public String getRecentHighestDegree() {
        return recentHighestDegree;
    }

    public void setRecentHighestDegree(String recentHighestDegree) {
        this.recentHighestDegree = recentHighestDegree;
    }

    public String getRecentLocation() {
        return recentLocation;
    }

    public void setRecentLocation(String recentLocation) {
        this.recentLocation = recentLocation;
    }

    public String getRecentOnline() {
        return recentOnline;
    }

    public void setRecentOnline(String recentOnline) {
        this.recentOnline = recentOnline;
    }

    public String getRecentUserImage() {
        return recentUserImage;
    }

    public void setRecentUserImage(String recentUserImage) {
        this.recentUserImage = recentUserImage;
    }
}
