package com.thiennm77.firechat.home;

import com.thiennm77.firechat.BasePresenter;
import com.thiennm77.firechat.models.Conversation;

import java.util.ArrayList;

public interface HomeContract {

    interface View {

        void showLoginScreen();

        void onRefreshCompleted(ArrayList<Conversation> conversations);

        void showToast(String message);
    }

    interface Presenter extends BasePresenter {

        void logOut();

        void onGettingConversationsListCompleted(ArrayList<Conversation> conversations);

        void getConversationsList();
    }
}
