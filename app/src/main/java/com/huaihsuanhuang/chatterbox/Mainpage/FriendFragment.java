package com.huaihsuanhuang.chatterbox.Mainpage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Account.StartActivity;
import com.huaihsuanhuang.chatterbox.Adapter.FriendlistAdapter;
import com.huaihsuanhuang.chatterbox.Model.FriendUsers;
import com.huaihsuanhuang.chatterbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {
    private DatabaseReference mfrienddatabase, muserreference;
    private FirebaseAuth mAuth;
    private String currentuser_uid;
    private View mrootview;
    private RecyclerView friend_list;
    private FriendlistAdapter mAdapter;
    private List<FriendUsers> friendUsersList;
    private Map<String, Object> mMap = new HashMap<>();
    private SwipeRefreshLayout friend_swipe;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mrootview = inflater.inflate(R.layout.fragment_friend, container, false);
        friend_list = mrootview.findViewById(R.id.friend_list);
        friend_swipe = mrootview.findViewById(R.id.friend_swipe);
        mAuth = FirebaseAuth.getInstance();
        currentuser_uid = mAuth.getCurrentUser().getUid();
        mfrienddatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentuser_uid);
        muserreference = FirebaseDatabase.getInstance().getReference().child("Users");
        mfrienddatabase.keepSynced(true);
        muserreference.keepSynced(true);
        friendUsersList = new ArrayList<>();

        friend_list.setLayoutManager(new LinearLayoutManager(this.getContext()));
        friend_list.setDrawingCacheEnabled(true);
        friend_list.setItemViewCacheSize(20);
        friend_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mAdapter = new FriendlistAdapter(getContext(), friendUsersList);
        friend_list.setAdapter(mAdapter);
        loadinfriendlist();

        friend_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendUsersList.clear();
                loadinfriendlist();
                friend_swipe.setRefreshing(false);
            }
        });
        return mrootview;
    }

    private void loadinfriendlist() {
        mfrienddatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ((DatabaseReference) muserreference).child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("FriendFragment", dataSnapshot1.getKey() + ":" + dataSnapshot1.getValue());
                                mMap.put(dataSnapshot1.getKey(), dataSnapshot1.getValue());

                            }
                            String name = mMap.get("name").toString();

                            String thumb_image = mMap.get("thumb_image").toString();
                            String online = mMap.get("online").toString();
                            String status = mMap.get("status").toString();
                            String image = mMap.get("image").toString();
                            String chatuserid = snapshot.getKey();


                            friendUsersList.add(new FriendUsers(name, status, image, online, thumb_image, chatuserid));

                            Log.d("mmap", mMap.toString());
                            mAdapter.notifyDataSetChanged();
                            mMap.clear();

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
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), StartActivity.class);
            startActivity(intent);
        }
    }


}
