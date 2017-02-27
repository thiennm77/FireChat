package com.thiennm77.firechat.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.FirebaseHelper;
import com.thiennm77.firechat.models.Message;

import java.util.ArrayList;

public class ChatPresenter implements ChatContract.Presenter {

    private FirebaseAuth.AuthStateListener mListener;

    private ChatContract.View mView;
    private String mId;
    private String mUsername;
    private String mCurrentUser;

    public ChatPresenter(ChatContract.View view, String chatId, String username) {
        mView = view;
        mId = chatId;
        mUsername = username;
        mCurrentUser = FirebaseHelper.getCurrentUID();
    }

    @Override
    public void getMessagesList() {
        FirebaseHelper.getMessagesList(this, mId, mCurrentUser, mUsername);
    }

    @Override
    public void onGettingMessagesListCompleted(ArrayList<Message> messages) {
        mView.onGettingMessagesListCompleted(messages);
    }

    @Override
    public void addAuthStateListener() {

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(AppHelper.TAG, "Chat > onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(AppHelper.TAG, "Chat > onAuthStateChanged:signed_out");
                    mView.showLoginScreen();
                }
            }
        };

        FirebaseHelper.addAuthStateListener(mListener);
    }

    @Override
    public void removeAuthStateListener() {
        FirebaseHelper.removeAuthStateListener(mListener);
        mListener = null;
    }
}
