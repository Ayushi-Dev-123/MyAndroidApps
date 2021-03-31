package com.ayushi.apnachating.model;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    private String status;
    private String image;
    private String state;
    private String time;
    private String date;
    private boolean isChecked = false;

    public User(String uid, String name, String status, String image) {
        this.uid = uid;
        this.name = name;
        this.status = status;
        this.image = image;
    }

    public User(String uid, String name, String status, String image, String state, String time, String date) {
        this.uid = uid;
        this.name = name;
        this.status = status;
        this.image = image;
        this.state = state;
        this.time = time;
        this.date = date;
    }

    public User() {

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
