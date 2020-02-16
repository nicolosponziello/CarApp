package com.nicolosponziello.carparking.model;

import java.time.LocalDateTime;

public class Parkimeter {
    private Float cost;
    private LocalDateTime exipiration;


    public Parkimeter(){

    }
    public Parkimeter(Float cost, LocalDateTime exipiration){
        this.cost = cost;
        this.exipiration = exipiration;
    }

    public Float getCost() {
        return cost;
    }

    public LocalDateTime getExipiration() {
        return exipiration;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public void setExipiration(LocalDateTime exipiration) {
        this.exipiration = exipiration;
    }
}
