package com.thiennm77.firechat.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.FirebaseHelper;
import com.thiennm77.firechat.models.Conversation;

import java.util.ArrayList;

class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private FirebaseAuth.AuthStateListener mListener;

    HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void logOut() {
        FirebaseHelper.signOut();
    }

    @Override
    public void onGettingConversationsListCompleted(ArrayList<Conversation> conversations) {
        mView.onRefreshCompleted(conversations);
    }

    @Override
    public void getConversationsList() {
        FirebaseHelper.getUsernamesList(this);
    }


    @Override
    public void addAuthStateListener() {

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(AppHelper.TAG, "Home > onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(AppHelper.TAG, "Home > onAuthStateChanged:signed_out");
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
