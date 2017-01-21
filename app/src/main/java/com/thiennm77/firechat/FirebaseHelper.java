package com.thiennm77.firechat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseAuth.AuthStateListener mListener;

    public static void signIn(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void addAuthStateListener(FirebaseAuth.AuthStateListener listener) {
        mListener = listener;
        mAuth.addAuthStateListener(mListener);
    }

    public static void removeAuthStateListener() {
        if (mListener != null) {
            mAuth.removeAuthStateListener(mListener);
            mListener = null;
        }
    }

}
