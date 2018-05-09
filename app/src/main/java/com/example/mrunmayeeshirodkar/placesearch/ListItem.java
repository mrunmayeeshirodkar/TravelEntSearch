package com.example.mrunmayeeshirodkar.placesearch;

import android.media.Image;

public class ListItem {
    private String placeName;
    private String placeAddress;
    private String imageURL;
    private String placeId;

    public ListItem(String placeName, String placeAddress, String imageURL, String placeId) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.imageURL = imageURL;
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPlaceId() { return placeId; }
}
