package com.huaihsuanhuang.chatterbox.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.huaihsuanhuang.chatterbox.R;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button start_btn_tolinin;
    private Button start_btn_tocreatenew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        start_btn_tolinin=findViewById(R.id.start_btn_tologin);
        start_btn_tolinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(StartActivity.this,Login.class);
                startActivity(intent_login);
            }
        });
        start_btn_tocreatenew=findViewById(R.id.start_btn_tocreatenew);
        start_btn_tocreatenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_register = new Intent(StartActivity.this,CreateAccount.class);
                startActivity(intent_register);

            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }



}
