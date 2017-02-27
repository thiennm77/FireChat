package com.thiennm77.firechat.chat.custom;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiennm77.firechat.R;
import com.thiennm77.firechat.models.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private ArrayList<Message> messages;
    private Context context;

    public MessagesAdapter() {
        messages = new ArrayList<>();
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(v);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        String sender = messages.get(position).getSender();
        holder.sender.setText(sender + ":");
        holder.message.setText(messages.get(position).getMessage());
        if (!sender.equals("You")) {
            holder.message.setBackground(context.getResources().getDrawable(R.drawable.message_background_light_grey));
            holder.message.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            holder.message.setBackground(context.getResources().getDrawable(R.drawable.message_background_accent));
            holder.message.setTextColor(context.getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateWholeList(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sender) TextView sender;
        @BindView(R.id.message) TextView message;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
