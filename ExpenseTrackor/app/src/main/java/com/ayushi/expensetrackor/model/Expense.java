package com.ayushi.expensetrackor.model;

import java.io.Serializable;

public class Expense implements Serializable {
    private String expenseId;
    private String userId;
    private String categoryId;
    private String amount;
    private String tag;
    private String date;
    private String category;
    private String getPaymentMode;

    public Expense(){}

    public Expense(String expenseId, String userId, String categoryId, String amount, String tag, String date, String category, String getPaymentMode) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.tag = tag;
        this.date = date;
        this.category = category;
        this.getPaymentMode = getPaymentMode;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGetPaymentMode() {
        return getPaymentMode;
    }

    public void setGetPaymentMode(String getPaymentMode) {
        this.getPaymentMode = getPaymentMode;
    }
}
