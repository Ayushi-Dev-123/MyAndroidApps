package com.example.testsql;

public class User {
    private String name;
    private String email;
    private String mobile;
    private String age;
    private int id;

    public User(){

    }

    public User(String name, String email, String mobile, String age, int id) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String  age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
