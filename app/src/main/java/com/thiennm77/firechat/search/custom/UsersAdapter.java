package com.thiennm77.firechat.search.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiennm77.firechat.R;
import com.thiennm77.firechat.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private ArrayList<User> mUsers;

    public UsersAdapter() {
        mUsers = new ArrayList<>();
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(v);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.uid.setText(mUsers.get(position).getUid());
        holder.username.setText(mUsers.get(position).getUsername());
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
