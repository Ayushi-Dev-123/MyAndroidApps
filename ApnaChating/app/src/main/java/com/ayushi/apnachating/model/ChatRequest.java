package com.ayushi.apnachating.model;

public class ChatRequest {
    private String request_type;

    public ChatRequest(){

    }

    public ChatRequest(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
