package com.thiennm77.firechat.home;

import com.thiennm77.firechat.BasePresenter;

interface HomeContract {

    interface View {

        void showLoginScreen();

    }

    interface Presenter extends BasePresenter {

        void logOut();


    }
}
