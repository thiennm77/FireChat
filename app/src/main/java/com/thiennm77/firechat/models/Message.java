package com.thiennm77.firechat.models;

public class Message {

    private String sender;

    private String message;

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
