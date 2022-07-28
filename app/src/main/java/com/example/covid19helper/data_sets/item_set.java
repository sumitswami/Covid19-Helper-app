package com.example.covid19helper.data_sets;

import java.util.Comparator;

public class item_set {

    private String address;
    private String contact;
    private String lead_type;
    private String city;
    private String distance;
    private String post_id;
    private int feedback_value;

    public item_set(){

    }

    public item_set(String address,String contact,String lead_type,String city,String distance,String post_id,int feedback_value){
        this.address = address;
        this.contact = contact;
        this.lead_type = lead_type;
        this.city = city;
        this.distance = distance;
        this.post_id = post_id;
        this.feedback_value = feedback_value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


    public int getFeedback_value() {
        return feedback_value;
    }

    public void setFeedback_value(int feedback_value) {
        this.feedback_value = feedback_value;
    }


    public static Comparator<item_set> distanceComparator = new Comparator<item_set>() {
        @Override
        public int compare(item_set o1, item_set o2) {
          double distance1 = Double.parseDouble(o1.getDistance());
          double distance2 = Double.parseDouble(o2.getDistance());

          return (int) (distance1 - distance2);
        }
    };

}
