package com.sid.marwadishaadi.FB_Gallery_Photo_Upload;

/**
 * Created by Sid on 28-Jun-17.
 */

public class FbGalleryModel {

    private String url;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public FbGalleryModel(String url, boolean isSelected) {
        this.url = url;
        this.isSelected = isSelected;
    }
}
