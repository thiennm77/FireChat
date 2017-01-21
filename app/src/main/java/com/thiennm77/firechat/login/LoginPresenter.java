package com.thiennm77.firechat.login;

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


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void attempLogin(String email, String password) {
        if (!AppHelper.isNetworkAvailable((Context) mView)) {
            mView.showToast("No Internet connection.");
            return;
        }

        if (!AppHelper.validateEmail(email)) {
            mView.showEmailError();
            return;
        }
        if (!AppHelper.validatePassword(password)) {
            mView.showPasswordError();
            return;
        }

        mView.showProgressDialog();
        FirebaseHelper.signIn(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(AppHelper.TAG, "Login > signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    mView.closeProgressDialog();
                    Exception ex = task.getException();
                    if (ex != null) {
                        mView.showToast(ex.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void register() {
        mView.showRegisterScreen();
    }

    @Override
    public void login() {
        mView.showHomeScreen();
    }

    @Override
    public void addAuthStateListener() {
        FirebaseHelper.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mView.closeProgressDialog();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(AppHelper.TAG, "Login > onAuthStateChanged:signed_in:" + user.getUid());
                    login();
                } else {
                    // User is signed out
                    Log.d(AppHelper.TAG, "Login > onAuthStateChanged:signed_out");
                }
            }
        });
    }

    @Override
    public void removeAuthStateListener() {
        FirebaseHelper.removeAuthStateListener();
    }
}
