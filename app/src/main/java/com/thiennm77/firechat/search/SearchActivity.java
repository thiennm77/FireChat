package com.thiennm77.firechat.search;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.thiennm77.firechat.AppHelper;
import com.thiennm77.firechat.R;
import com.thiennm77.firechat.login.LoginActivity;
import com.thiennm77.firechat.models.User;
import com.thiennm77.firechat.search.custom.UsersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.users)
    RecyclerView mUsers;

    @BindView(R.id.search)
    EditText mSearch;

    private SearchContract.Presenter mPresenter;
    private UsersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new SearchPresenter(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppHelper.isNetworkAvailable(SearchActivity.this)) {
                    mAdapter.clear();
                    mPresenter.getUsersList();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showToast("No internet connection");
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mAdapter = new UsersAdapter();
        mUsers.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mUsers.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mUsers.getContext(),
                linearLayoutManager.getOrientation());
        mUsers.addItemDecoration(dividerItemDecoration);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterUsersList(mPresenter.getUsers(), s.toString());
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        if (AppHelper.isNetworkAvailable(SearchActivity.this)) {
            mPresenter.getUsersList();
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
    public void showToast(String message) {
        Toast.makeText(this, message,
                (message.length() < 70) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onGettingUsersListCompleted(ArrayList<User> users) {
        final String search = mSearch.getText().toString();
        filterUsersList(users, search);
    }

    private void filterUsersList(ArrayList<User> users, String search) {
        if (search.isEmpty()) {
            mAdapter.refresh(users);
        } else {
            ArrayList<User> filteredUsers = new ArrayList<>();
            for (User user : users) {
                if (user.getUsername().contains(search)) {
                    filteredUsers.add(user);
                }
            }

            mAdapter.refresh(filteredUsers);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
