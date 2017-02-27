package com.thiennm77.firechat.search;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.FirebaseHelper;
import com.thiennm77.firechat.chat.ChatActivity;
import com.thiennm77.firechat.models.User;

import java.util.ArrayList;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;
    private FirebaseAuth.AuthStateListener mListener;
    private ArrayList<User> mUsers;

    public SearchPresenter(SearchContract.View view) {
        mView = view;
        mUsers = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return mUsers;
    }

    @Override
    public void addAuthStateListener() {
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(AppHelper.TAG, "Search > onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(AppHelper.TAG, "Search > onAuthStateChanged:signed_out");
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

    @Override
    public void getUsersList() {
        FirebaseHelper.getUsersList(this);
    }

    @Override
    public void onGettingUsersListCompleted(ArrayList<User> result) {
        mUsers = result;
        mView.onGettingUsersListCompleted(result);
    }

    @Override
    public void attemptCreatingNewConversation(String uid, String username) {
        mView.showProgressDialog();
        FirebaseHelper.attemptCreatingNewConversation(this, uid, username);
    }

    @Override
    public void openConversation(String conversationId, String username) {
        mView.closeProgressDialog();
        Intent intent = new Intent( (Activity) mView, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_ID, conversationId);
        intent.putExtra(ChatActivity.EXTRA_USERNAME, username);
        ((Activity) mView).startActivity(intent);
    }
}
