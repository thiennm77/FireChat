package com.thiennm77.firechat.home.custom;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiennm77.firechat.R;
import com.thiennm77.firechat.chat.ChatActivity;
import com.thiennm77.firechat.home.HomeContract;
import com.thiennm77.firechat.models.Conversation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationsViewHolder> {

    ArrayList<Conversation> mConversations;
    ViewGroup mParent;

    public ConversationsAdapter() {
        mConversations = new ArrayList<>();
    }

    @Override
    public ConversationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mParent == null) {
            mParent = parent;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation, parent, false);
        ConversationsViewHolder conversationsViewHolder = new ConversationsViewHolder(v);
        return conversationsViewHolder;
    }

    @Override
    public void onBindViewHolder(ConversationsViewHolder holder, final int position) {
        holder.username.setText(mConversations.get(position).getUsername());
        holder.message.setText(mConversations.get(position).getMessage());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_CHAT_ID, mConversations.get(position).getId());
                intent.putExtra(ChatActivity.EXTRA_USERNAME, mConversations.get(position).getUsername());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    public void clear() {
        mConversations.clear();
        notifyDataSetChanged();
    }

    public void refresh(ArrayList<Conversation> conversations) {
        mConversations = conversations;
        notifyDataSetChanged();
    }

    static class ConversationsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view) CardView cv;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.message) TextView message;

        ConversationsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
