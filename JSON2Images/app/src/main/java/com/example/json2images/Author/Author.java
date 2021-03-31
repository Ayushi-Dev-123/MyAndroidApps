package com.example.json2images.Author;

public class Author {
    private String id;
    private String author;
    private String download_url;
    private int width;
    private int heigth;

    public Author(String id, String author, String download_url, int width, int heigth) {
        this.id = id;
        this.author = author;
        this.download_url = download_url;
        this.width = width;
        this.heigth = heigth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

}
