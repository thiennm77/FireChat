package com.thiennm77.firechat.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thiennm77.firechat.R;
import com.thiennm77.firechat.chat.custom.MessagesAdapter;
import com.thiennm77.firechat.login.LoginActivity;
import com.thiennm77.firechat.models.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends Activity implements ChatContract.View {

    @BindView(R.id.loading)
    TextView mLoading;

    @BindView(R.id.chat_list)
    RecyclerView mChatList;

    @BindView(R.id.chat_message)
    EditText mChatMessage;

    @BindView(R.id.send)
    ImageButton mSend;

    public static String EXTRA_USERNAME = "username";
    public static String EXTRA_CHAT_ID = "chatId";

    private String mUsername;
    private String mId;
    private ChatContract.Presenter mPresenter;

    private MessagesAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mUsername = intent.getStringExtra(ChatActivity.EXTRA_USERNAME);
        mId = intent.getStringExtra(ChatActivity.EXTRA_CHAT_ID);

        setTitle(mUsername);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mPresenter = new ChatPresenter(this, mId, mUsername);

        mAdapter = new MessagesAdapter();
        mChatList.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        mChatList.setLayoutManager(linearLayoutManager);

        mPresenter.getMessagesList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.addAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.removeAuthStateListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGettingMessagesListCompleted(ArrayList<Message> messages) {
        mAdapter.updateWholeList(messages);
        mLoading.setVisibility(View.GONE);
        mChatList.setVisibility(View.VISIBLE);
    }
}
