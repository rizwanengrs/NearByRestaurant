package com.example.abc.resturantdemo.model;


public class PlaceItem {
    public final String placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef,rating,contact_detail;

    public PlaceItem(String placeName, String placeVicinity, String placeType, String placeDistance, String placeLat, String placeLng, String placePhotoRef,String rating,String contact_detail){
        this.placeName = placeName;
        this.placeVicinity = placeVicinity;
        this.placeType = placeType;
        this.placeDistance = placeDistance;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.placePhotoRef = placePhotoRef;
        this.rating = rating;
        this.contact_detail = contact_detail;
    }
}
