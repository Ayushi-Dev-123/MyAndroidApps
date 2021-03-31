package com.example.contactrecyclerview;

public class Contact {
    private String name;
    private  String number;
    private int imageid;

    public Contact(int imageid, String name, String number) {
        this.name = name;
        this.number = number;
        this.imageid = imageid;
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }
}
