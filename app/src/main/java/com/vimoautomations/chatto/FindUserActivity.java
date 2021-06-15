package com.vimoautomations.chatto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.vimoautomations.chatto.adapters.UserListAdapter;
import com.vimoautomations.chatto.models.User;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView rv_UserList;
    private RecyclerView.Adapter rv_UserListAdapter;
    private RecyclerView.LayoutManager rv_LayoutManger;
    private ArrayList<User> contactList;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        initRecyclyerView();
    }

    private void initRecyclyerView() {
        contactList = new ArrayList<>();
        userList = new ArrayList<>();
        rv_UserList = findViewById(R.id.rv_user_list);
        rv_UserList.setNestedScrollingEnabled(false);
        rv_UserList.setHasFixedSize(false);
        rv_LayoutManger = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        rv_UserList.setLayoutManager(rv_LayoutManger);
        rv_UserListAdapter = new UserListAdapter(contactList);
        rv_UserList.setAdapter(rv_UserListAdapter);
        getContactsList();
    }

    private void getContactsList(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        , null, null, null, null);
        while(phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            User user = new User(name, phone);
            contactList.add(user);
            rv_UserListAdapter.notifyDataSetChanged();
        }
    }
}