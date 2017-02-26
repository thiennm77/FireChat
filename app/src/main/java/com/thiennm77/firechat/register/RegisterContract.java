package com.thiennm77.firechat.register;

import com.thiennm77.firechat.BasePresenter;

interface RegisterContract {

    interface View {

        void showEmailError();

        void showUsernameError();

        void showPasswordError();

        void showConfirmPasswordError();

        void showToast(String message);

        void showHomeScreen();

        void showProgressDialog();

        void closeProgressDialog();

    }

    interface Presenter  extends BasePresenter {

        void attemptRegister(String email, String username, String password, String confirmPassword);

        void login();

    }

}