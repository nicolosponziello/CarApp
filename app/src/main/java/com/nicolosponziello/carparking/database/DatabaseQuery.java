package com.nicolosponziello.carparking.database;

public enum DatabaseQuery {
    CREATE_TABLE("create table");

    private String query;
    DatabaseQuery(String query){
        this.query = query;
    }
    String getQuery(){
        return this.query;
    }
}
