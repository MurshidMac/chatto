package com.vimoautomations.chatto.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoautomations.chatto.R;
import com.vimoautomations.chatto.models.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListRecylerViewHolder> {

    ArrayList<User> userlist;
    public UserListAdapter(ArrayList<User> userList) {
        this.userlist = userList;
    }

    @NonNull
    @Override
    public UserListRecylerViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user, null, false
        );
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        UserListRecylerViewHolder rcv = new UserListRecylerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListRecylerViewHolder holder,
                                 int position) {
        holder.mName.setText(userlist.get(position).getName());
        holder.mPhone.setText(userlist.get(position).getPhone());
    }

    // SIZE of the list
    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class UserListRecylerViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mPhone;
        public UserListRecylerViewHolder(@NonNull  View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.txt_name_of_contact);
            mPhone = itemView.findViewById(R.id.txt_contact_number);
        }
    }
}
