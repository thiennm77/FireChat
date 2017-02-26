package com.thiennm77.firechat.models;

public class Conversation {

    private String mUsername;

    private String mMessage;

    public Conversation(String username, String lastMessage) {
        mUsername = username;
        mMessage = lastMessage;
    }

    public String getUsername() { return mUsername; }

    public String getMessage() { return mMessage; }

}
