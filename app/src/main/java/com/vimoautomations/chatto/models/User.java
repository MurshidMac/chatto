package com.vimoautomations.chatto.models;

public class User {
    private String name;
    private String phone;
    private String uid;
    public User(String name, String phone, String uid) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }
}
