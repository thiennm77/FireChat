package com.thiennm77.firechat.models;

public class Conversation {

    private String mId;

    private String mUsername;

    private String mMessage;

    public Conversation(String id, String username, String lastMessage) {
        mId = id;
        mUsername = username;
        mMessage = lastMessage;
    }

    public String getUsername() { return mUsername; }

    public String getMessage() { return mMessage; }

    public String getId() { return mId; }

}
