package com.vimoautomations.chatto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vimoautomations.chatto.adapters.ChatListAdapter;
import com.vimoautomations.chatto.adapters.UserListAdapter;
import com.vimoautomations.chatto.models.Chat;
import com.vimoautomations.chatto.models.User;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private Button logout;
    private Button findUser;

    private RecyclerView rv_chatList;
    private RecyclerView.Adapter rv_chatListAdapter;
    private RecyclerView.LayoutManager rv_chatLayoutManger;
    private ArrayList<Chat> chatList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        logout = findViewById(R.id.btn_logout);
        findUser = findViewById(R.id.btn_find_user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindUserActivity.class));
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }else {
            getPermission();
        }
        initRecyclyerView();
        getUserChatList();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }


    private void initRecyclyerView() {
        chatList = new ArrayList<>();
        rv_chatList = findViewById(R.id.rv_chatList);
        rv_chatList.setNestedScrollingEnabled(false);
        rv_chatList.setHasFixedSize(false);
        rv_chatLayoutManger = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        rv_chatList.setLayoutManager(rv_chatLayoutManger);
        rv_chatListAdapter = new ChatListAdapter(chatList);
        rv_chatList.setAdapter(rv_chatListAdapter);
    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user")
                .child(FirebaseAuth.getInstance().getUid()).child("chat");
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot childSnap: snapshot.getChildren()){
                        Chat chat = new Chat(childSnap.getKey());

                        chatList.add(chat);
                        rv_chatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}