package com.example.covid19helper.data_sets;

import java.util.ArrayList;

public class postSet {

    private String lead_type;
    private String address;
    private String city;
    private String contact_no;
    private double longitude;
    private double latitude;
    private int feedback_value;
    private ArrayList<String> feedback_list;

    public postSet(){
    }

    public postSet(String lead_type,  double longitude, double latitude, String address, String city, String contact_no,int feedback_value,ArrayList<String> feedback_list){
        this.lead_type = lead_type;
        this.address = address;
        this.city = city;
        this.contact_no = contact_no;
        this.longitude = longitude;
        this.latitude = latitude;
        this.feedback_value = feedback_value;
        this.feedback_list = feedback_list;
    }


    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
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

    public int getFeedback_value() {
        return feedback_value;
    }

    public void setFeedback_value(int feedback_value) {
        this.feedback_value = feedback_value;
    }

    public ArrayList<String> getFeedback_list() {
        return feedback_list;
    }

    public void setFeedback_list(ArrayList<String> feedback_list) {
        this.feedback_list = feedback_list;
    }

}
