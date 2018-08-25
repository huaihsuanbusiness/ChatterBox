package com.huaihsuanhuang.chatterbox.Messages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.huaihsuanhuang.chatterbox.Account.StartActivity;
import com.huaihsuanhuang.chatterbox.Adapter.MessageAdapter;
import com.huaihsuanhuang.chatterbox.Model.Messagemodel;
import com.huaihsuanhuang.chatterbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String mchatuserid, mchatusername, mcurrentuserid;
    private DatabaseReference friend_selected_ref, rooffef;
    private android.support.v7.widget.Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private TextView chatbar_display;
    private TextView chatbar_lastseen;
    private String chat_name, chat_image;
    private String currentuser_name, currentuser_image;
    private ImageButton chat_ib_plus, chat_ib_send;
    private EditText chat_messageview;
    private RecyclerView chat_meassagelist;
    private SwipeRefreshLayout refreshLayout;
    private List<Messagemodel> messagemodelList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter mMessageAdapter;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private static final int GALLERY_PICK = 1;
    private int mcurrentpage = 1;
    private int itemposition = 0;
    private String mlastkey = "";
    private String mprevkey = "";
    private StorageReference mProfileImage;
    private Map<String, Object> mMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mchatuserid = getIntent().getStringExtra("uid");
        mchatusername = getIntent().getStringExtra("name");
        mAuth = FirebaseAuth.getInstance();
        mcurrentuserid = mAuth.getCurrentUser().getUid();
        mToolbar = findViewById(R.id.toolbar_chat);
        chat_ib_plus = findViewById(R.id.chat_ib_plus);
        chat_ib_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plusbtnintent = new Intent();
                plusbtnintent.setType("image/*");
                plusbtnintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(plusbtnintent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });
        chat_ib_send = findViewById(R.id.chat_ib_send);
        chat_messageview = findViewById(R.id.chat_text);
        chat_meassagelist = findViewById(R.id.chat_messagelist);
        refreshLayout = findViewById(R.id.chat_swipe);
        linearLayoutManager = new LinearLayoutManager(this);
        chat_meassagelist.setLayoutManager(linearLayoutManager);
        chat_meassagelist.setAdapter(mMessageAdapter);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        rooffef = FirebaseDatabase.getInstance().getReference();
        rooffef.keepSynced(true);
        friend_selected_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        friend_selected_ref.keepSynced(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_bar, null);
        actionBar.setCustomView(action_bar_view);
        chatbar_display = findViewById(R.id.chatbar_display);
        chatbar_lastseen = findViewById(R.id.chat_lastseen);
        chatbar_display.setText(mchatusername);
        mProfileImage = FirebaseStorage.getInstance().getReference();
        friend_selected_ref.child(mchatuserid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat_name = dataSnapshot.child("name").getValue().toString();
                chat_image = dataSnapshot.child("thumb_image").getValue().toString();
                String chat_onlinestatus = dataSnapshot.child("online").getValue().toString();
                if (!chat_onlinestatus.equals("true")) {
                    long date_longtype = Long.parseLong(chat_onlinestatus);
                    Date date = new Date(date_longtype);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final String formatedtime = formatter.format(date);
                    chatbar_lastseen.setText(formatedtime);
                } else {
                    chatbar_lastseen.setText("Online");
                    //remind: create a class for 多久前上線 28
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        friend_selected_ref.child(mcurrentuserid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentuser_name = dataSnapshot.child("name").getValue().toString();
                currentuser_image = dataSnapshot.child("thumb_image").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMessageAdapter = new MessageAdapter(messagemodelList, mchatuserid, currentuser_image,
                currentuser_name, chat_image, chat_name, getBaseContext());

        rooffef.child("Chat").child(mcurrentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mchatuserid)) {
                    Map chataddmap = new HashMap();
                    chataddmap.put("seen", "false");
                    chataddmap.put("timestamp", System.currentTimeMillis());

                    Map chatusermap = new HashMap();
                    chatusermap.put("Chat/" + mcurrentuserid + "/" + mchatuserid, chataddmap);
                    chatusermap.put("Chat/" + mchatuserid + "/" + mcurrentuserid, chataddmap);
                    rooffef.updateChildren(chatusermap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {

                                Log.d("Chaterror", databaseError.getMessage().toString());

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chat_ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mcurrentpage++;
                //            messagemodelList.clear();
                //            loadmessage();
                itemposition = 0;
                loadmoremessage();

            }
        });
        loadmessage();
    }

    private void loadmoremessage() {

        DatabaseReference loadmsgref = rooffef.child("Messages").child(mcurrentuserid).child(mchatuserid);
        Query messageQuery = loadmsgref.orderByKey().endAt(mlastkey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Messagemodel messagemodel = dataSnapshot.getValue(Messagemodel.class);

                    String messagekey = dataSnapshot.getKey();

                    if (!mlastkey.equals(messagekey)) {
                        messagemodelList.add(itemposition++, messagemodel);

                    } else {

                        mprevkey = messagekey;
                    }
                    if (itemposition == 1) {

                        mlastkey = messagekey;
                    }


                    mMessageAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    linearLayoutManager.scrollToPositionWithOffset(10, 0);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadmessage() {

        DatabaseReference loadmsgref = rooffef.child("Messages").child(mcurrentuserid).child(mchatuserid);

        Query messageQuery = loadmsgref.limitToLast(mcurrentpage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //    Log.d("snapmmmMap",snapshot.getKey()+" : "+snapshot.getValue());
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Log.d("snapmmmMap1", snapshot.getKey() + " : " + snapshot.getValue());
                        mMap.put(snapshot1.getKey(), snapshot1.getValue());
                        }
                    Log.d("snapmMap1", mMap.toString());
                    String message = mMap.get("message").toString();
                    String seen = mMap.get("seen").toString();
                    String time = mMap.get("time").toString();
                    String type = mMap.get("type").toString();
                    String from = mMap.get("from").toString();
                    messagemodelList.add(new Messagemodel(message, seen, time, type, from));
                    mMessageAdapter.notifyDataSetChanged();

                    //TODO E/RecyclerView: No adapter attached; skipping layout
                    mMap.clear();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {

        String message = chat_messageview.getText().toString();
        String current_user_ref = "Messages/" + mcurrentuserid + "/" + mchatuserid;
        String chat_user_fef = "Messages/" + mchatuserid + "/" + mcurrentuserid;

        DatabaseReference user_message_push = rooffef
                .child("Messages").child(mcurrentuserid).child(mchatuserid).push();
        String push_id = user_message_push.getKey();

        if (!TextUtils.isEmpty(message)) {
            Map messagemap = new HashMap();
            messagemap.put("message", message);
            messagemap.put("seen", "false");
            messagemap.put("type", "text");
            messagemap.put("time", System.currentTimeMillis());
            messagemap.put("from", mcurrentuserid);

            Map messageusermap = new HashMap();
            messageusermap.put(current_user_ref + "/" + push_id, messagemap);
            messageusermap.put(chat_user_fef + "/" + push_id, messagemap);
            chat_messageview.setText("");
            rooffef.updateChildren(messageusermap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {

                        Log.d("Chaterror", databaseError.getMessage().toString());

                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            final String current_user_ref = "Messages/" + mcurrentuserid + "/" + mchatuserid;
            final String chat_user_fef = "Messages/" + mchatuserid + "/" + mcurrentuserid;
            DatabaseReference user_message_push = rooffef
                    .child("Messages").child(mcurrentuserid).child(mchatuserid).push();
            final String push_id = user_message_push.getKey();

            final StorageReference filepath = mProfileImage.child("messageimage").child(push_id + ".jpg");
            filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map messagemap = new HashMap();
                                messagemap.put("message", uri);
                                messagemap.put("seen", false);
                                messagemap.put("type", "text");
                                messagemap.put("time", System.currentTimeMillis());
                                messagemap.put("from", mcurrentuserid);

                                Map messageusermap = new HashMap();
                                messageusermap.put(current_user_ref + "/" + push_id, messagemap);
                                messageusermap.put(chat_user_fef + "/" + push_id, messagemap);

                                chat_messageview.setText("");
                                rooffef.child("Chat").child(mcurrentuserid).child(mchatuserid).child("seen").setValue(true);
                                rooffef.child("Chat").child(mcurrentuserid).child(mchatuserid).child("timestamp").setValue(System.currentTimeMillis());

                                rooffef.child("Chat").child(mchatuserid).child(mcurrentuserid).child("seen").setValue(false);
                                rooffef.child("Chat").child(mchatuserid).child(mcurrentuserid).child("timestamp").setValue(System.currentTimeMillis());
                                rooffef.updateChildren(messageusermap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Log.d("Chaterror", databaseError.getMessage().toString());
                                        }

                                    }
                                });
                            }
                        });
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(ChatActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
