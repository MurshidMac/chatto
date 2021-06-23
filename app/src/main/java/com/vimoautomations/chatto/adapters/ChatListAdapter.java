package com.vimoautomations.chatto.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoautomations.chatto.R;
import com.vimoautomations.chatto.models.Chat;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListRecylerViewHolder> {

    ArrayList<Chat> chatlist;
    public ChatListAdapter(ArrayList<Chat> chatList) {
        this.chatlist = chatList;
    }

    @NonNull
    @Override
    public ChatListRecylerViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_chat, null, false
        );
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListRecylerViewHolder rcv = new ChatListRecylerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ChatListRecylerViewHolder holder,
                                 int position) {
        holder.mTitle.setText(chatlist.get(position).getChatId());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // SIZE of the list
    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    public class ChatListRecylerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;
        public ChatListRecylerViewHolder(@NonNull  View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.txt_chat_title);
            mLayout = itemView.findViewById(R.id.rv_item_chat_layout);
        }
    }
}
