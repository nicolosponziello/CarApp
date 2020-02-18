package com.nicolosponziello.carparking.model;

import android.media.Image;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ParkingData {

    private String address;
    private boolean active;
    private String longitude;
    private String latitude;
    private String parkSpot;
    private String parkLevel;
    private String note;
    private float cost;
    private long expiration;
    private String city;
    private String photoPath;
    private Date date; //ms
    private UUID id;

    public ParkingData(){
    }

    public String getAddress() {
        return address;
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

    public boolean isActive() {
        return active;
    }

    public String getCity() {
        return city;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Date getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

    public float getCost() {
        return cost;
    }

    public long getExpiration() {
        return expiration;
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

    public void setParkLevel(String parkLevel) {
        this.parkLevel = parkLevel;
    }

    public void setParkSpot(String parkSpot) {
        this.parkSpot = parkSpot;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
