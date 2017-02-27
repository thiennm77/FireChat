package com.thiennm77.firechat.models;

public class User {

    private String uid;
    private String username;

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }
}
