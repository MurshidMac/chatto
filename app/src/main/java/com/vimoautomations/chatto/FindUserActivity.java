package com.vimoautomations.chatto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vimoautomations.chatto.adapters.UserListAdapter;
import com.vimoautomations.chatto.models.User;
import com.vimoautomations.chatto.util.CountryISO2Phone;

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
        rv_UserListAdapter = new UserListAdapter(userList);
        rv_UserList.setAdapter(rv_UserListAdapter);
        getContactsList();
    }

    private void getContactsList(){
        String countryIso2Phone = getCountryISO();

        Cursor phones = getContentResolver().query(ContactsContract
                        .CommonDataKinds.Phone.CONTENT_URI
        , null, null, null, null);
        while(phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if(!String.valueOf(phone.charAt(0)).equals("+")){
                phone = countryIso2Phone + phone;
            }

            User user = new User(name, phone);
            contactList.add(user);
            //rv_UserListAdapter.notifyDataSetChanged();
            getUserList(user);
        }
    }

    private void getUserList(User user) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = userDb.orderByChild("phone").equalTo(user.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String phone = "";
                    String name = "";

                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("phone").getValue() != null){
                            phone = snap.child("phone").getValue().toString();
                        }
                        if(snap.child("name").getValue() != null){
                            name = snap.child("name").getValue().toString();
                        }
                        User firebaseUser = new User(name, phone);
                        userList.add(firebaseUser);
                        rv_UserListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getCountryISO(){
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext()
                .getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso() != null){
            if(!telephonyManager.getNetworkCountryIso().toString().equals("")){
                iso = telephonyManager.getNetworkCountryIso().toString();
            }
        }
        return CountryISO2Phone.getPhone(iso);
    }
}