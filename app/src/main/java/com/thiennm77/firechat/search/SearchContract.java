package com.thiennm77.firechat.search;


import com.thiennm77.firechat.BasePresenter;
import com.thiennm77.firechat.models.User;

import java.util.ArrayList;

public interface SearchContract {

    interface View {

        void showLoginScreen();

        void showToast(String message);

        void onGettingUsersListCompleted(ArrayList<User> users);
    }

    interface Presenter extends BasePresenter {

        void getUsersList();

        ArrayList<User> getUsers();

        void onGettingUsersListCompleted(ArrayList<User> result);
    }

}
