package com.nicolosponziello.carparking.model;

import android.media.Image;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Date;

public class ParkingData {

    private String address;
    private boolean active;
    private String longitude;
    private String latitude;
    private String parkSpot;
    private String parkLevel;
    private String note;
    private Parkimeter parkimeter;
    private Image image;
    private String city;
    private byte[] photoBlob;
    private String date;
    private long id;

    public ParkingData(){
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

    public boolean isActive() {
        return active;
    }

    public String getCity() {
        return city;
    }

    public byte[] getPhotoBlob() {
        return photoBlob;
    }

    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhotoBlob(byte[] photoBlob) {
        this.photoBlob = photoBlob;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
