package com.example.ambuapp.entities;

import java.io.Serializable;

public class User implements Serializable {

    public String uid;
    public String email;
    public String username;
    public String phoneNumber;
    public String address;

    public User() {
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public User(String uid, String username, String email, String phoneNumber) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String uid, String username, String email, String phoneNumber, String address) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
