package com.nicolosponziello.carparking.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Parkimeter {
    private Float cost;
    private String exipiration;


    public Parkimeter(){

    }
    public Parkimeter(Float cost, String exipiration){
        this.cost = cost;
        this.exipiration = exipiration;
    }

    public Float getCost() {
        return cost;
    }

    public String getExipiration() {
        return exipiration;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public void setExipiration(String date) {
        this.exipiration = date;
    }
}
