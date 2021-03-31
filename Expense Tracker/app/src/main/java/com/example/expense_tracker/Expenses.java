package com.example.expense_tracker;

public class Expenses {
    private int id;
    private String tag;
    private String date;
    private int amount;
    private int categoryId;
    private String paymentMode;

    public Expenses(){
    }

    public Expenses(int id, String tag, String date, int amount, int categoryId, String paymentMode) {
        this.id = id;
        this.tag = tag;
        this.date = date;
        this.amount = amount;
        this.categoryId = categoryId;
        this.paymentMode = paymentMode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
