package com.nicolosponziello.carparking.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * classe che gestisce i dati di un parcheggio salvato
 */
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
    private List<String> photoPath;
    private long date; //ms
    private String id;

    public ParkingData(){
        photoPath = new ArrayList<>();
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

    public List<String> getPhotoPath() {
        return photoPath;
    }

    public long getDate() {
        return date;
    }

    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public void setPhotoPath(String path) {
        Log.d("CarParking", "setting photopath" + path);
        if(photoPath == null){
            photoPath = new ArrayList<>();
        }
        photoPath.add(path);
    }

    @Override
    public String toString() {
        return "ParkingData{" +
                "address='" + address + '\'' +
                ", active=" + active +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", parkSpot='" + parkSpot + '\'' +
                ", parkLevel='" + parkLevel + '\'' +
                ", note='" + note + '\'' +
                ", cost=" + cost +
                ", expiration=" + expiration +
                ", city='" + city + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", date=" + date +
                ", id='" + id + '\'' +
                '}';
    }
}
