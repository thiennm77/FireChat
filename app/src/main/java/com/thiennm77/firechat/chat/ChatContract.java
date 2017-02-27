package com.thiennm77.firechat.chat;

import com.thiennm77.firechat.BasePresenter;
import com.thiennm77.firechat.models.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {

        void showLoginScreen();

        void onGettingMessagesListCompleted(ArrayList<Message> messages);

    }

    interface Presenter extends BasePresenter {

        void getMessagesList();

        void onGettingMessagesListCompleted(ArrayList<Message> messages);

        void sendMessage(String message);

        void addChatListener();

        void removeChatListener();
    }

}
