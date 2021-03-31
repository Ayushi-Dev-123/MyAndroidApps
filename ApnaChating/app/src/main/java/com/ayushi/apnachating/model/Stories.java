package com.ayushi.apnachating.model;

import java.io.Serializable;

public class Stories implements Serializable {
    private String uid;
    private String storyId;
    private String date;
    private String time;
    private String type;
    private String text;
    private String imageUrl;
    private long timeStamp;

    public Stories() {
    }

    public Stories(String uid, String storyId, String date, String time, String type, String text, String imageUrl, long timeStamp) {
        this.uid = uid;
        this.storyId = storyId;
        this.date = date;
        this.time = time;
        this.type = type;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
