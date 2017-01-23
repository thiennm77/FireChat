package com.thiennm77.firechat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void signIn(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void signUp(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void signOut() {
        mAuth.signOut();
    }

    public static void setUsername(FirebaseUser user, String username) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(user.getUid()).setValue(username);
    }

    public static void addAuthStateListener(FirebaseAuth.AuthStateListener listener) {
        mAuth.addAuthStateListener(listener);
    }

    public static void removeAuthStateListener(FirebaseAuth.AuthStateListener listener) {
        mAuth.removeAuthStateListener(listener);
    }

}
