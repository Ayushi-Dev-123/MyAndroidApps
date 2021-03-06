package com.example.productdetails;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private int price;
    private String description;

    public Product(String name, int price, String description) {
        this.description = description;
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
