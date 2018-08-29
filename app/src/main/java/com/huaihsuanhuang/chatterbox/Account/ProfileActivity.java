package com.huaihsuanhuang.chatterbox.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    private String uid;
    private TextView profile_name, profile_status, profile_count;
    private ImageView profile_image;
    private DatabaseReference mreference, mfriend_request, mfrienddatabase, mcloud_message;
    private FirebaseUser currentUser;
    public static int current_state;
    private Button profile_btn_send, profile_btn_decline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_name = findViewById(R.id.profile_name);
        profile_image = findViewById(R.id.profile_image);
        profile_status = findViewById(R.id.profile_status);
        profile_count = findViewById(R.id.profile_count);
        profile_btn_decline = findViewById(R.id.profile_btn_decline);
        profile_btn_decline.setVisibility(View.GONE);
        profile_btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfriend_request.child(currentUser.getUid()).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mfriend_request.child(uid).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                profile_btn_send.setEnabled(true);
                                current_state = 0;
                                profile_btn_send.setText("Send friend request");
                                Toast.makeText(ProfileActivity.this, "Request declined", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        profile_btn_send = findViewById(R.id.profile_btn_send);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = getIntent().getStringExtra("uid");
        mreference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mreference.keepSynced(true);
        mfriend_request = FirebaseDatabase.getInstance().getReference().child("Request");
        mfriend_request.keepSynced(true);
        mfrienddatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mfrienddatabase.keepSynced(true);
        mcloud_message = FirebaseDatabase.getInstance().getReference().child("CM");
        //note remove request 方法待合併
        if (uid.equals(currentUser.getUid())) {
            profile_btn_send.setVisibility(View.GONE);
            profile_count.setVisibility(View.GONE);
        }
        {
            profile_btn_send.setText("Send friend request");
        }
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                final String status = dataSnapshot.child("status").getValue().toString();
                profile_name.setText(name);
                profile_status.setText(status);
                current_state = 0;

                if (!image.equals("null")) {
                    Glide.with(getApplicationContext())
                            .load(image)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                            .into(profile_image);

                } else {
                    profile_image.setImageResource(R.mipmap.empty_profile);
                }
                mfriend_request.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uid)) {
                            //有請求沒好友時判斷ok
                            String reqest_type = dataSnapshot.child(uid).child("request_type").getValue().toString();
                            if (reqest_type.equals("received")) {
                                current_state = 2;
                                profile_btn_send.setText("Accept request");
                                profile_btn_decline.setVisibility(View.VISIBLE);


                            } else if (reqest_type.equals("sent")) {
                                profile_btn_send.setText("Cancel request");
                                current_state = 1;
                            }
                        } else {
                            //沒請求時判斷ok
                            //note: renew button or instant renew?
                            mfrienddatabase.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uid)) {
                                        current_state = 3;
                                        profile_btn_send.setText("UnFriend");
                                        profile_btn_decline.setVisibility(View.GONE);
                                        //remind 通知好友被接受 2223 node.js unable to connect with firebase
                                        // https://github.com/firebase/functions-samples/blob/Node-8/fcm-notifications/functions/index.js
                                    } else {
                                        current_state = 0;
                                        profile_btn_send.setText("Send friend request");
                                        profile_btn_decline.setVisibility(View.GONE);
                                        //remind 通知好友被取消
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        profile_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_btn_send.setEnabled(false);
                if (current_state == 0) { //不是朋友 發送邀請ok

                    sendrequest();
                }
                if (current_state == 1) { //取消好友邀請ok
                    cancelrequest();
                }
                if (current_state == 2) { //接受好友ok
                    acceptfriend();
                }
                if (current_state == 3) { //取消好友ok
                    deletefriend();

                }
            }
        });


    }

    private void deletefriend() {
        mfrienddatabase.child(currentUser.getUid()).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mfrienddatabase.child(uid).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        profile_btn_send.setEnabled(true);
                        current_state = 0;
                        profile_btn_send.setText("Send friend request");
                        Toast.makeText(ProfileActivity.this, "Unfriended ", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void cancelrequest() {
        mfriend_request.child(currentUser.getUid()).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mfriend_request.child(uid).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        profile_btn_send.setEnabled(true);
                        current_state = 0;
                        profile_btn_send.setText("Send friend request");
                        Toast.makeText(ProfileActivity.this, "Request canceled", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sendrequest() {
        mfriend_request.child(currentUser.getUid()).child(uid).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mfriend_request.child(uid).child(currentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            profile_btn_send.setEnabled(true);
                            current_state = 1;
                            profile_btn_send.setText("Cancel request");
                            Toast.makeText(ProfileActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(ProfileActivity.this, "Failed sending request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void acceptfriend() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formatedtime = formatter.format(date);
        mfrienddatabase.child(currentUser.getUid()).child(uid).child("date").setValue(formatedtime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mfrienddatabase.child(uid).child(currentUser.getUid()).child("date").setValue(formatedtime).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //remind : 發送一條訊息給對方告知已完成邀請
                    // remind：刪除好友 ok
                    //remind ：拒絕邀請 ok
                    //remeind :發送方狀態更新 ok
                    @Override
                    public void onSuccess(Void aVoid) {
                        mfriend_request.child(currentUser.getUid()).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mfriend_request.child(uid).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        profile_btn_send.setEnabled(true);
                                        current_state = 3;
                                        profile_btn_send.setText("UnFriend");
                                        profile_btn_decline.setVisibility(View.GONE);
                                        Toast.makeText(ProfileActivity.this, "Friend accepted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }


}
