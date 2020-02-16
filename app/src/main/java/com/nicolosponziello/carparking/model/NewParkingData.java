package com.nicolosponziello.carparking.model;

import android.media.Image;

public class NewParkingData {

    private String address;
    private String longitude;
    private String latitude;
    private String parkSpot;
    private String parkLevel;
    private String note;
    private Parkimeter parkimeter;
    private Image image;

    public NewParkingData(){

    }

    public String getAddress() {
        return address;
    }

    public Parkimeter getParkimeter() {
        return parkimeter;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getNote() {
        return note;
    }

    public String getParkLevel() {
        return parkLevel;
    }

    public String getParkSpot() {
        return parkSpot;
    }

    public Image getImage() {
        return image;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setParkimeter(Parkimeter parkimeter) {
        this.parkimeter = parkimeter;
    }

    public void setParkLevel(String parkLevel) {
        this.parkLevel = parkLevel;
    }

    public void setParkSpot(String parkSpot) {
        this.parkSpot = parkSpot;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
