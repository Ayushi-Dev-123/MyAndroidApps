package com.example.booklist;

public class BookList {
    private String name;
    private int price;
    private int img;

    public BookList(int img, int price, String name) {
        this.img = img;
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

    public int getImg() {
        return img;
    }

    public void setImgId(int img) {
        this.img = img;
    }
}
