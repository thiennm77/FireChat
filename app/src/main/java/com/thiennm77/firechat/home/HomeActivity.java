package com.thiennm77.firechat.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.R;
import com.thiennm77.firechat.home.custom.ConversationsAdapter;
import com.thiennm77.firechat.login.LoginActivity;
import com.thiennm77.firechat.models.Conversation;
import com.thiennm77.firechat.search.SearchActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private HomeContract.Presenter mPresenter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.conversationsRecyclerView)
    RecyclerView mConversationsView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    ConversationsAdapter mAdapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mPresenter = new HomePresenter(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppHelper.isNetworkAvailable(HomeActivity.this)) {
                    mAdapter.clear();
                    mPresenter.getConversationsList();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showToast("No internet connection");
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mAdapter = new ConversationsAdapter();
        mConversationsView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mConversationsView.setLayoutManager(linearLayoutManager);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        if (AppHelper.isNetworkAvailable(HomeActivity.this)) {
            mPresenter.getConversationsList();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showToast("No internet connection");
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.log_out))
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.logOut();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));
        }

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
    public void onRefreshCompleted(ArrayList<Conversation> conversations) {
        mAdapter.refresh(conversations);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message,
                (message.length() < 70) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }
}
