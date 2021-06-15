package com.vimoautomations.chatto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phonenumber, code;
    private Button sendVefication;
    private FirebaseAuth mAuth;
    private String mVerificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIds();
        FirebaseApp.initializeApp(this);
        userIsLoggedIn();
        mAuth = FirebaseAuth.getInstance();

        sendVefication.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mVerificationCode != null){
                    verifyPhoneNumbeWithCode();
                }else {
                    startPhoneNumberVerfication();
                }
            }
        });
    }
    private void setIds(){
        phonenumber = findViewById(R.id.et_phonenumber);
        code = findViewById(R.id.et_code);
        sendVefication = findViewById(R.id.btn_verfication);
    }

    private void startPhoneNumberVerfication(){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phonenumber.getText().toString())
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)
                .setCallbacks(mcallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed( FirebaseException e) {

        }

        @Override
        public void onCodeSent(String verificationId, @NonNull
                PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            mVerificationCode = verificationId;
            code.setText("Code sent");
        }
    };

    private void verifyPhoneNumbeWithCode(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationCode,
                code.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user !=null){
                        final DatabaseReference dbreference = FirebaseDatabase.getInstance()
                                .getReference().child("user").child(user.getUid());
                        dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());
                                    userMap.put("name", user.getPhoneNumber());
                                    dbreference.updateChildren(userMap);
                                }
                                userIsLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
        }
    }

}