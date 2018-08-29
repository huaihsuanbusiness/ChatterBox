package com.huaihsuanhuang.chatterbox.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huaihsuanhuang.chatterbox.Mainpage.MainActivity;
import com.huaihsuanhuang.chatterbox.R;

import java.util.HashMap;


public class CreateAccount extends AppCompatActivity {
    private TextInputEditText create_input_account;
    private TextInputEditText create_input_display;
    private TextInputEditText create_input_password;
    private TextInputEditText create_input_confirm;
    private Button create_button_createaccount;
    private ConstraintLayout createaccount_layout;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressBar mCAprogressbar;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        createaccount_layout = findViewById(R.id.createaccount_layout);
        create_input_account = findViewById(R.id.create_input_account);
        create_input_display = findViewById(R.id.create_input_display);
        create_input_password = findViewById(R.id.create_input_password);
        create_input_confirm = findViewById(R.id.create_input_confirm);
        create_button_createaccount = findViewById(R.id.create_button_createaccount);
        create_button_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });
        mCAprogressbar = findViewById(R.id.progressbar_createaccounnt);
        mCAprogressbar.setVisibility(View.GONE);

        mToolbar = findViewById(R.id.toolbar_creataccount);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void createaccount() {


        final String account = create_input_account.getText().toString();
        final String display = create_input_display.getText().toString();
        final String password = create_input_password.getText().toString();
        final String confirm = create_input_confirm.getText().toString();

        if (account.isEmpty()) {
            Toast.makeText(this, "Please enter account", Toast.LENGTH_LONG).show();
            return;
        }
        if (display.isEmpty()) {
            Toast.makeText(this, "Please enter display name", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if (confirm.isEmpty()) {
            Toast.makeText(this, "Confirm passward", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(password.equals(confirm))) {
            Toast.makeText(this, "Confirm password", Toast.LENGTH_LONG).show();
            return;
        } else {
            mCAprogressbar.setVisibility(View.VISIBLE);
        }

        final FirebaseAuth myAuth = FirebaseAuth.getInstance();
        myAuth.createUserWithEmailAndPassword(account, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> muserMap = new HashMap<>();
                    muserMap.put("name", display);
                    muserMap.put("status", "Hi I begin using the ChatterBox!");
                    muserMap.put("image", "null");
                    muserMap.put("thumb_image", "null");
                    firebaseDatabase.setValue(muserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mCAprogressbar.setVisibility(View.GONE);
                                Toast.makeText(CreateAccount.this, "Account Created", Toast.LENGTH_LONG).show();
                                FirebaseUser user = myAuth.getCurrentUser();
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(display).build();
                                user.updateProfile(request);
                                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                                // TODO 有人輸入錯仍會上傳 導致資料會抓錯crash 待修
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccount.this, "Fail reason: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                mCAprogressbar.setVisibility(View.GONE);
                finish();
            }
        });
    }
}
