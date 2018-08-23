package com.huaihsuanhuang.chatterbox.Mainpage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Adapter.MessagelistViewHolder;
import com.huaihsuanhuang.chatterbox.Messages.ChatActivity;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.Model.Seen;
import com.huaihsuanhuang.chatterbox.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagelistFragment extends Fragment {

    private RecyclerView msglistfm_recyclerview;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerAdapter<Seen, MessagelistViewHolder> adapter_messagelist;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View rootview;


    public MessagelistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_chat, container, false);

        msglistfm_recyclerview = rootview.findViewById(R.id.msglistfm_recyclerview);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);

        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        msglistfm_recyclerview.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadinmessagelist();
    }

    private void loadinmessagelist() {
        Query sortQuery = mConvDatabase.orderByChild("timestamp");
        FirebaseRecyclerOptions<Seen> options =
                new FirebaseRecyclerOptions.Builder<Seen>()
                        .setQuery(sortQuery, Seen.class)
                        .build();
        adapter_messagelist = new FirebaseRecyclerAdapter<Seen, MessagelistViewHolder>(options) {
            @NonNull
            @Override
            public MessagelistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_item, parent, false);
                return new MessagelistViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MessagelistViewHolder holder, int position, @NonNull final Seen model) {
                final String list_user_id = getRef(position).getKey();
                if (list_user_id != null && !list_user_id.isEmpty()) {
                    Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                    lastMessageQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                            String data = dataSnapshot.child("message").getValue().toString();
                            MessagelistViewHolder.setMessage(data, model.seen);


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final String userName = dataSnapshot.child("name").getValue().toString();
                            String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                            if (dataSnapshot.hasChild("online")) {

                                String userOnline = dataSnapshot.child("online").getValue().toString();
                                MessagelistViewHolder.setUserOnline(userOnline);

                            }

                            MessagelistViewHolder.setName(userName);
                            MessagelistViewHolder.setUserImage(userThumb, getContext());
                            holder.setItemonclicklistener(new ItemonClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean islongclick) {

                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                    chatIntent.putExtra("user_id", list_user_id);
                                    chatIntent.putExtra("user_name", userName);
                                    startActivity(chatIntent);
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }
                adapter_messagelist.notifyDataSetChanged();
                msglistfm_recyclerview.setAdapter(adapter_messagelist);
                msglistfm_recyclerview.setNestedScrollingEnabled(false);
            }

        };
    }


}