package com.sjsu.hackathon.merfstsdb;

public class Data {
    private String year;
    private double data;
    private String country;
    public Data(String year, double data, String country) {
        this.year = year;
        this.data = data;
        this.country = country;
    }
    public String getYear() {
        return year;
    }
    public double getData() {
        return data;
    }
    public String getCountry() {
        return country;
    }
    @Override
    public String toString() {
        return this.year + " " + this.data + " " + this.country;
    }
}
