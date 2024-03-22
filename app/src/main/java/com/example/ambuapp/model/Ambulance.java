package com.example.ambuapp.model;

public class Ambulance {
    private String ambulanceId;
    private String availabilityStatus;
    private double latitude;
    private double longitude;

    private Integer number;
    private String registration;
    private String driver;
    private String hospital;

    public Ambulance() {
        // Default constructor required for Firebase
    }

    public Ambulance(String ambulanceId, String availabilityStatus, double latitude, double longitude, Integer number, String registration, String driver, String hospital) {
        this.ambulanceId = ambulanceId;
        this.availabilityStatus = availabilityStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.number = number;
        this.registration = registration;
        this.driver = driver;
        this.hospital = hospital;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(String ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }


    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
