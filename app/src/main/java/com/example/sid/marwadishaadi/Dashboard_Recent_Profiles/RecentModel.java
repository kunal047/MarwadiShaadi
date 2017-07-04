    package com.example.sid.marwadishaadi.Dashboard_Recent_Profiles;

    /**
     * Created by USER on 02-06-2017.
     */

    public class RecentModel {

        private String recentName, recentAge, recentHighestDegree, recentLocation, recentOnline, recentCustomerId, recentFavouriteStatus, recentInterestStatus;
        private String recentUserImage;

        public RecentModel(String recentCustomerId, String recentName, String recentAge, String recentHighestDegree, String recentLocation, String recentOnline, String recentUserImage, String recentFavouriteStatus, String recentInterestStatus) {
            this.recentCustomerId = recentCustomerId;
            this.recentName = recentName;
            this.recentAge = recentAge;
            this.recentHighestDegree = recentHighestDegree;
            this.recentLocation = recentLocation;
            this.recentOnline = recentOnline;
            this.recentUserImage = recentUserImage;
            this.recentFavouriteStatus = recentFavouriteStatus;
            this.recentInterestStatus = recentInterestStatus;
        }

        public String getRecentFavouriteStatus() {
            return recentFavouriteStatus;
        }

        public void setRecentFavouriteStatus(String recentFavouriteStatus) {
            this.recentFavouriteStatus = recentFavouriteStatus;
        }

        public String getRecentInterestStatus() {
            return recentInterestStatus;
        }

        public void setRecentInterestStatus(String recentInterestStatus) {
            this.recentInterestStatus = recentInterestStatus;
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
