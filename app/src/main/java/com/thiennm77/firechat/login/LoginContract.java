package com.thiennm77.firechat.login;

import com.google.firebase.auth.FirebaseAuth;
import com.thiennm77.firechat.BasePresenter;

interface LoginContract {

    interface View {

        void showEmailError();

        void showPasswordError();

        void showToast(String message);

        void showRegisterScreen();

        void showHomeScreen();

        void showProgressDialog();

        void closeProgressDialog();

    }

    interface  Presenter extends BasePresenter {

        void attempLogin(String email, String password);

        void register();

        void login();
    }

}