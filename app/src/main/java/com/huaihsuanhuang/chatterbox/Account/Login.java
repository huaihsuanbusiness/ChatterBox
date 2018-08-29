package com.huaihsuanhuang.chatterbox.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Mainpage.MainActivity;
import com.huaihsuanhuang.chatterbox.R;

import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {
    private TextInputEditText login_input_account, login_input_pw;
    private Button login_button_login, login_button_forget;
    private SignInButton login_button_google;
    private TextView login_status;
    private ConstraintLayout login_layout;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressBar mCAprogressbar;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_layout = findViewById(R.id.login_layout);
        login_input_account = findViewById(R.id.login_input_account);
        login_input_pw = findViewById(R.id.login_input_pw);
        login_input_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickforget();
            }
        });
        login_button_login = findViewById(R.id.login_button_login);
        login_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_login();
            }
        });
        login_button_forget = findViewById(R.id.login_button_forget);
        login_button_google = findViewById(R.id.login_button_google);
        login_status = findViewById(R.id.login_status);
        mCAprogressbar = findViewById(R.id.progressbar_login);
        mCAprogressbar.setVisibility(View.GONE);
        mToolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) login_button_google.getChildAt(0);
        textView.setText("Login with Google");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateuserstatus(user);
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateuserstatus(user);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        login_button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCAprogressbar.setVisibility(View.VISIBLE);
                signIn();
            }
        });


    }

    private void onClick_login() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            String string_account = login_input_account.getText().toString();
            String string_password = login_input_pw.getText().toString();
            if (string_account.isEmpty()) {
                Toast.makeText(this, "Please enter account", Toast.LENGTH_LONG).show();
                return;
            }
            if (string_password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
                return;
            } else {
                mCAprogressbar.setVisibility(View.VISIBLE);
            }
            mAuth.signInWithEmailAndPassword(string_account, string_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mCAprogressbar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Login Successful!\n" + " Welcome back", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(login_layout, "Login failure " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    mCAprogressbar.setVisibility(View.GONE);
                }
            });
        } else {
            mAuth.signOut();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w("google signin failed", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = Objects.requireNonNull(user).getUid();
                            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                            DatabaseReference uid_Ref = firebaseDatabase.child(uid);
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = Objects.requireNonNull(user).getUid();
                                        DatabaseReference uid_inner = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                        HashMap<String, String> muserMap = new HashMap<>();
                                        muserMap.put("name", user.getDisplayName());
                                        muserMap.put("status", "Hi I begin using the ChatterBox!");
                                        muserMap.put("image", "null");
                                        muserMap.put("thumb_image", "null");
                                        uid_inner.setValue(muserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    mCAprogressbar.setVisibility(View.GONE);
                                                    Toast.makeText(Login.this, "Account Created", Toast.LENGTH_LONG).show();
                                                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(user.getDisplayName()).build();
                                                    user.updateProfile(request);
                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            };
                            uid_Ref.addValueEventListener(valueEventListener);

                            Snackbar.make(login_layout, "Login Successful", Snackbar.LENGTH_SHORT).show();
                            mCAprogressbar.setVisibility(View.GONE);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            updateuserstatus(user);
                            finish();
                        } else {
                            // If sign in fails, display a Messagemodel to the user.
                            Log.w("signinfailure", "signInWithCredential:failure", task.getException());
                            Snackbar.make(getCurrentFocus(), "Google Login Failed", Snackbar.LENGTH_LONG).show();
                            mCAprogressbar.setVisibility(View.GONE);
                            updateuserstatus(null);

                        }

                        // ...
                    }
                });
    }


    public void onClickforget() {

        String string_account = login_input_account.getText().toString();
        String string_password = login_input_pw.getText().toString();
        if (string_account.isEmpty()) {
            Toast.makeText(this, "Please enter account", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.sendPasswordResetEmail(string_account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(login_layout, "Please check your mailbox", Snackbar.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(login_layout, "Failure:  " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateuserstatus(FirebaseUser user) {
        if (user == null) {
            login_button_login.setText("LogIn");
            login_status.setText("Not logged in");
            login_button_forget.setVisibility(View.VISIBLE);
            login_button_google.setVisibility(View.VISIBLE);
        } else {

            login_button_login.setText("LogOut");
            login_status.setText(user.getDisplayName() + " you are logged in by email\n" + user.getEmail() + "\nVerified status:  " + user.isEmailVerified());
            login_button_forget.setVisibility(View.INVISIBLE);
            login_button_google.setVisibility(View.INVISIBLE);
            if (!(user.isEmailVerified())) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(login_layout, "Please check your mailbox", Snackbar.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(login_layout, "Failure sending the mail", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }


        }
    }
}
