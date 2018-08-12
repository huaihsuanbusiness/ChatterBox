package com.huaihsuanhuang.chatterbox.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huaihsuanhuang.chatterbox.R;

public class StatusActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mtoolbar;
    private TextInputLayout mstatusinput;
    private Button mbtnSaveStatus;
    private DatabaseReference mRef;
    private FirebaseUser mcurrentuser;
    private ProgressBar mCAprogressbar;
    private String status_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mcurrentuser.getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        status_value = getIntent().getStringExtra("statusvalue");
        mCAprogressbar = findViewById(R.id.progressbar_status);
        mCAprogressbar.setVisibility(View.GONE);

        mtoolbar = findViewById(R.id.toolbar_status);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mstatusinput = findViewById(R.id.status_input);
        mstatusinput.getEditText().setText(status_value);
        mbtnSaveStatus = findViewById(R.id.status_btnsavestatus);
        mbtnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCAprogressbar.setVisibility(View.VISIBLE);
                String status = mstatusinput.getEditText().getText().toString();
                mRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mCAprogressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(StatusActivity.this, "Status updated", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StatusActivity.this, "Failed to save the change", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}
