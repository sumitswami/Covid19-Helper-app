package com.example.covid19helper.resources_activities;

public class vaccinemodel {


    private String vaccinationcenter;
    private String vaccinationCharges;
    private String vaccinationage;
    private String vaccinationTimings;
    private String vaccinationName;
    private String vaccinationCentertime;
    private String vaccinationcenteraddress;
    private String vaccineavailable;

    public vaccinemodel(String vaccinationcenter, String vaccinationCharges, String vaccinationage,
                        String vaccinationTimings, String vaccinationName,
                        String vaccinationCentertime, String vaccinationcenteraddress,
                        String vaccineavailable) {
        this.vaccinationcenter = vaccinationcenter;
        this.vaccinationCharges = vaccinationCharges;
        this.vaccinationage = vaccinationage;
        this.vaccinationTimings = vaccinationTimings;
        this.vaccinationName = vaccinationName;
        this.vaccinationCentertime = vaccinationCentertime;
        this.vaccinationcenteraddress = vaccinationcenteraddress;
        this.vaccineavailable = vaccineavailable;
    }

    public vaccinemodel() {

    }

    public String getVaccinationcenter() {
        return vaccinationcenter;
    }

    public void setVaccinationcenter(String vaccinationcenter) {
        this.vaccinationcenter = vaccinationcenter;
    }

    public String getVaccinationCharges() {
        return vaccinationCharges;
    }

    public void setVaccinationCharges(String vaccinationCharges) {
        this.vaccinationCharges = vaccinationCharges;
    }

    public String getVaccinationage() {
        return vaccinationage;
    }

    public void setVaccinationage(String vaccinationage) {
        this.vaccinationage = vaccinationage;
    }

    public String getVaccinationTimings() {
        return vaccinationTimings;
    }

    public void setVaccinationTimings(String vaccinationTimings) {
        this.vaccinationTimings = vaccinationTimings;
    }

    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public String getVaccinationCentertime() {
        return vaccinationCentertime;
    }

    public void setVaccinationCentertime(String vaccinationCentertime) {
        this.vaccinationCentertime = vaccinationCentertime;
    }

    public String getVaccinationcenteraddress() {
        return vaccinationcenteraddress;
    }

    public void setVaccinationcenteraddress(String vaccinationcenteraddress) {
        this.vaccinationcenteraddress = vaccinationcenteraddress;
    }

    public String getVaccineavailable() {
        return vaccineavailable;
    }

    public void setVaccineavailable(String vaccineavailable) {
        this.vaccineavailable = vaccineavailable;
    }
}
