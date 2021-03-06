package com.thiennm77.firechat;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thiennm77.firechat.chat.ChatContract;
import com.thiennm77.firechat.home.HomeContract;
import com.thiennm77.firechat.models.Conversation;
import com.thiennm77.firechat.models.Message;
import com.thiennm77.firechat.models.User;
import com.thiennm77.firechat.search.SearchContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseHelper {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static JSONObject mUsernames;
    private static ArrayList<Message> mMessages = null;
    private static ValueEventListener mValueEventListener = null;

    public static String getCurrentUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return "";
    }

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

    public static String getUsername(String uid) {

        try {
            String username = mUsernames.get(uid).toString();
            return username;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void getUsernamesList(final HomeContract.Presenter presenter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mUsernames = new JSONObject(dataSnapshot.getValue().toString());
                    getConversationsList(presenter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void getConversationsList(final HomeContract.Presenter presenter) {
        final ArrayList<Conversation> result = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String uid = getCurrentUID();

        databaseReference.child("conversations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    if (key.contains(uid)) { // conversation relating to current user
                        ArrayList messages = (ArrayList) child.getValue();
                        HashMap<String, String> lastMessage = (HashMap<String, String>) messages.get(messages.size() - 1);
                        String otherUid = key.replace(uid, "").replace("_", "");
                        String username = getUsername(otherUid);
                        String message;
                        if (lastMessage.get("sender").toString().equals(uid)) {
                            message = "You: " + lastMessage.get("message").toString();
                        } else {
                            message = lastMessage.get("message").toString();
                        }

                        Conversation conversation = new Conversation(key, username, message);
                        result.add(conversation);
                    }
                }
                presenter.onGettingConversationsListCompleted(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void getUsersList(final SearchContract.Presenter presenter) {
        final ArrayList<User> result = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String currenUid = getCurrentUID();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String uid = child.getKey();
                    if (!uid.equals(currenUid)) {
                        String username = child.getValue().toString();
                        User user = new User(uid, username);
                        result.add(user);
                    }
                }
                presenter.onGettingUsersListCompleted(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getMessagesList(final ChatContract.Presenter presenter, String id, final String uid, final String username) {
        mMessages = new ArrayList<>();
        final ArrayList<Message> result = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("conversations").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList messages = (ArrayList) dataSnapshot.getValue();
                int size = messages.size();
                for (int i = 0; i < size; i++) {
                    HashMap<String, String> messageItem = (HashMap<String, String>) messages.get(i);
                    String origSender = messageItem.get("sender");
                    String sender;
                    if (origSender.equals(uid)) {
                        sender = "You";
                    } else {
                        sender = username;
                    }
                    String message = messageItem.get("message");
                    Message newMsg = new Message(sender, message);
                    result.add(newMsg);
                    mMessages.add(new Message(origSender, message));
                }
                presenter.onGettingMessagesListCompleted(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void sendMessage(String id, String sender, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Message newMsg = new Message(sender, message);
        mMessages.add(newMsg);
        databaseReference.child("conversations").child(id).setValue(mMessages);
    }

    public static void addChatListener(final ChatContract.Presenter presenter, String id, final String uid, final String username) {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        final ArrayList<Message> result = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList messages = (ArrayList) dataSnapshot.getValue();
                int size = messages.size();
                for (int i = mMessages.size(); i < size; i++) {
                    HashMap<String, String> messageItem = (HashMap<String, String>) messages.get(i);
                    String origSender = messageItem.get("sender");
                    String sender;
                    if (origSender.equals(uid)) {
                        sender = "You";
                    } else {
                        sender = username;
                    }
                    String message = messageItem.get("message");
                    Message newMsg = new Message(sender, message);
                    result.add(newMsg);
                    mMessages.add(new Message(origSender, message));
                }
                presenter.onGettingMessagesListCompleted(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.child("conversations").child(id).addValueEventListener(mValueEventListener);
    }

    public static void removeChatListener(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("conversations").child(id).removeEventListener(mValueEventListener);
        mValueEventListener = null;
        mMessages = null;
    }

    public static void attemptCreatingNewConversation(final SearchContract.Presenter presenter, final String uid, final String username) {
        final String currentUid = getCurrentUID();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("conversations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    if (key.contains(currentUid) && key.contains(uid)) { // conversation already existed
                        presenter.openConversation(key, username);
                        return;
                    }
                }
                final String newConversationId = currentUid + "_" + uid;
                ArrayList<Message> msg = new ArrayList<>();
                Message hi = new Message(currentUid, "Hi");
                msg.add(hi);
                databaseReference.child("conversations").child(newConversationId).setValue(msg)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presenter.openConversation(newConversationId, username);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
