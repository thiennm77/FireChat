package com.thiennm77.firechat.register;

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

    interface Presenter {

        void attempRegister(String email, String username, String password, String confirmPassword);

        void login();

        void addAuthStateListener();

        void removeAuthStateListener();

    }

}