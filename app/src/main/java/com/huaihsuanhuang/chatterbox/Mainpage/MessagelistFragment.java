package com.huaihsuanhuang.chatterbox.Mainpage;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Adapter.MessageListAdapter;
import com.huaihsuanhuang.chatterbox.Model.MessageListUsers;
import com.huaihsuanhuang.chatterbox.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagelistFragment extends Fragment {

    private RecyclerView mChatRV;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private MessageListAdapter mAdapter;

    public MessagelistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_chat, container, false);
        mChatRV = rootview.findViewById(R.id.msglistfm_recyclerview);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(auth.getCurrentUser().getUid());
        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mChatRV.setLayoutManager(linearLayoutManager);
        loadChatList();
        return rootview;
    }

    private void loadChatList() {
        final List<MessageListUsers> chatList = new ArrayList<>();
        Query sortQuery = mConvDatabase.orderByChild("timestamp");
        sortQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                // 取得所有聊天列表所有User
                chatList.clear(); // 資料異動，清除Chat List
                for (final DataSnapshot snapshot : chatSnapshot.getChildren()) {
                    final String chatUid = snapshot.getKey();
                    final String lastMsg = TextUtils.isEmpty((String) snapshot.child("lastMessage").getValue()) ? "" : snapshot.child("lastMessage").getValue().toString();
                    final String timestamp = TextUtils.isEmpty((String) snapshot.child("timestamp").getValue()) ? "" : snapshot.child("timestamp").getValue().toString();

                    mUsersDatabase.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            // 取得到User資料
                            String userName = userSnapshot.child("name").getValue().toString();
                            String userThumb = userSnapshot.child("thumb_image").getValue().toString();
                            String userOnline = userSnapshot.child("online").getValue().toString();

                            chatList.add(new MessageListUsers(chatUid, userName, userThumb, userOnline, lastMsg, timestamp));
                            mAdapter = new MessageListAdapter(getContext(), chatList);
                            mChatRV.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
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

}


