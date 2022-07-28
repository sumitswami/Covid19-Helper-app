package com.example.covid19helper.data_sets;

public class detailSet {

    private String name;
    private String address;
    private String city;
    private String contact_no;
    private double longitude;
    private double latitude;
    private String uid;
    private int category_no;

    public detailSet(){

    }

    public detailSet(String name, String address, String city, String contact_no,int category_no, double longitude, double latitude, String uid){
        this.name = name;
        this.address = address;
        this.city = city;
        this.contact_no = contact_no;
        this.category_no = category_no;
        this.longitude = longitude;
        this.latitude = latitude;
        this.uid = uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public int getCategory_no() {
        return category_no;
    }

    public void setCategory_no(int category_no) {
        this.category_no = category_no;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
