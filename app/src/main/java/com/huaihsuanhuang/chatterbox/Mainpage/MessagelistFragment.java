package com.huaihsuanhuang.chatterbox.Mainpage;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Adapter.MessageListAdapter;
import com.huaihsuanhuang.chatterbox.Model.MessageListUsers;
import com.huaihsuanhuang.chatterbox.Model.Seen;
import com.huaihsuanhuang.chatterbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagelistFragment extends Fragment {

    private RecyclerView msglistfm_recyclerview;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;
    //  private FirebaseRecyclerAdapter<Seen, MessagelistViewHolder> adapter_messagelist;
    private FirebaseAuth mAuth;
    private Map<String, Object> mMap = new HashMap<>();
    private String mCurrent_user_id;
    private List<Seen> seenlist;
    private List<MessageListUsers> messageListUsersList;
    private View rootview;
    private MessageListAdapter mAdapter;
    private String chat_uid;
   // private String lastdata;
    private String chat_userName;
    private String chat_userThumb;
    private String chat_userOnline;

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
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        seenlist = new ArrayList<>();
        messageListUsersList = new ArrayList<>();
        msglistfm_recyclerview.setLayoutManager(linearLayoutManager);

        loadinmessagelist();


        return rootview;
    }

    private void loadinmessagelist() {


        Query sortQuery = mConvDatabase.orderByChild("timestamp");
        sortQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mConvDatabase.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            mMap.clear();
                            messageListUsersList.clear();
                            seenlist.clear();
                            chat_uid = "";


                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                mMap.put(snapshot1.getKey(), snapshot1.getValue());
                            }
                            chat_uid = snapshot.getKey();
                            seenlist.add(new Seen(mMap.get("seen").toString(), mMap.get("timestamp").toString()));

                            mMap.clear();

                            queryLastMessage(snapshot.getKey());

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

    private void queryLastMessage(final String key) {
        Query lastMessageQuery = mMessageDatabase.child(key).limitToLast(1);
        lastMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               String lastdata = dataSnapshot.child("message").getValue().toString();
                loadinuserinfo(key,lastdata);

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

    private void loadinuserinfo(String key, final String lastdata) {
        mUsersDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chat_userName = dataSnapshot.child("name").getValue().toString();
                chat_userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                chat_userOnline = dataSnapshot.child("online").getValue().toString();
                messageListUsersList.add(new MessageListUsers(chat_userName, chat_userThumb, chat_userOnline));
                Log.d("putintoadapter", chat_userName+" "+ lastdata);
                //TODO 到放進去adapter前一刻都正常(上面的putintoadapter) 放進去值就亂了
                mAdapter = new MessageListAdapter(getContext(), chat_uid, lastdata, messageListUsersList, seenlist);
                msglistfm_recyclerview.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // loadinmessagelist();
    }

//    private void ml() {
//        Query sortQuery = mConvDatabase.orderByChild("timestamp");
//        FirebaseRecyclerOptions<Seen> options =
//                new FirebaseRecyclerOptions.Builder<Seen>()
//                        .setQuery(sortQuery, Seen.class)
//                        .build();
//     //   adapter_messagelist = new FirebaseRecyclerAdapter<Seen, MessagelistViewHolder>(options) {
//            @NonNull
//            @Override
//            public MessagelistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.users_item, parent, false);
//                return new MessagelistViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull final MessagelistViewHolder holder, int position, @NonNull final Seen model) {
//                final String list_user_id = getRef(position).getKey();
//                if (list_user_id != null && !list_user_id.isEmpty()) {
//                    Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);
//
//                    lastMessageQuery.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//
//                            String data = dataSnapshot.child("message").getValue().toString();
//                            MessagelistViewHolder.setMessage(data, model.seen);
//
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            final String userName = dataSnapshot.child("name").getValue().toString();
//                            String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
//
//                            if (dataSnapshot.hasChild("online")) {
//
//                                String userOnline = dataSnapshot.child("online").getValue().toString();
//                                MessagelistViewHolder.setUserOnline(userOnline);
//
//                            }
//
//                            MessagelistViewHolder.setName(userName);
//                            MessagelistViewHolder.setUserImage(userThumb, getContext());
//                            holder.setItemonclicklistener(new ItemonClickListener() {
//                                @Override
//                                public void onClick(View view, int position, boolean islongclick) {
//
//                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                    chatIntent.putExtra("user_id", list_user_id);
//                                    chatIntent.putExtra("user_name", userName);
//                                    startActivity(chatIntent);
//                                }
//                            });
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//
//
//                    });
//                }
//                adapter_messagelist.notifyDataSetChanged();
//                msglistfm_recyclerview.setAdapter(adapter_messagelist);
//                msglistfm_recyclerview.setNestedScrollingEnabled(false);
//            }
//
//        };
}


