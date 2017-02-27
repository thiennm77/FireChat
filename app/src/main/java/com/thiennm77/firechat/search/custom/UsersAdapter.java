package com.thiennm77.firechat.search.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thiennm77.firechat.R;
import com.thiennm77.firechat.models.User;
import com.thiennm77.firechat.search.SearchContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private ArrayList<User> mUsers;
    private SearchContract.Presenter mPresenter;

    public UsersAdapter(SearchContract.Presenter presenter) {
        mUsers = new ArrayList<>();
        mPresenter = presenter;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(v);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final String uid = mUsers.get(position).getUid();
        final String username = mUsers.get(position).getUsername();
        holder.uid.setText(uid);
        holder.username.setText(username);

        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.attemptCreatingNewConversation(uid, username);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    public void refresh(ArrayList<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user)
        LinearLayout user;

        @BindView(R.id.uid)
        TextView uid;

        @BindView(R.id.username)
        TextView username;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
