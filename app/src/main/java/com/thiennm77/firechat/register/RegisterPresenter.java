package com.thiennm77.firechat.register;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.FirebaseHelper;

class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView;
    private FirebaseAuth.AuthStateListener mListener;

    RegisterPresenter(RegisterContract.View view) {
        mView = view;
    }

    @Override
    public void attemptRegister(String email, final String username, String password, String confirmPassword) {
        if (!AppHelper.isNetworkAvailable((Context) mView)) {
            mView.showToast("No Internet connection.");
            return;
        }

        if (!AppHelper.validateEmail(email)) {
            mView.showEmailError();
            return;
        }
        if (!AppHelper.validateUsername(username)) {
            mView.showUsernameError();
            return;
        }
        if (!AppHelper.validatePassword(password)) {
            mView.showPasswordError();
            return;
        }
        if (!password.equals(confirmPassword)) {
            mView.showConfirmPasswordError();
            return;
        }

        mView.showProgressDialog();
        FirebaseHelper.signUp(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(AppHelper.TAG, "Register > createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    mView.closeProgressDialog();
                    Exception ex = task.getException();
                    if (ex != null) {
                        mView.showToast(ex.getMessage());
                    }
                } else {
                    FirebaseUser user = task.getResult().getUser();
                    FirebaseHelper.setUsername(user, username);
                }
            }
        });
    }

    @Override
    public void login() {
        mView.showHomeScreen();
    }

    @Override
    public void addAuthStateListener() {

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mView.closeProgressDialog();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(AppHelper.TAG, "Register > onAuthStateChanged:signed_in:" + user.getUid());
                    login();
                } else {
                    // User is signed out
                    Log.d(AppHelper.TAG, "Register > onAuthStateChanged:signed_out");
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
