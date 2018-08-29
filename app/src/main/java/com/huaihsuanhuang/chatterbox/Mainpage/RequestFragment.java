package com.huaihsuanhuang.chatterbox.Mainpage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Account.StartActivity;
import com.huaihsuanhuang.chatterbox.Adapter.RequestAdapter;
import com.huaihsuanhuang.chatterbox.Model.Requestlistmodel;
import com.huaihsuanhuang.chatterbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private DatabaseReference request_reference, request_currentuserreceived, muserreference;
    private FirebaseAuth mAuth;
    private View rootview;
    private String currentuid;
    private RecyclerView request_recyclerview;
    private LinearLayoutManager layoutManager;
    private Map<String, Object> mMap = new HashMap<>();
    private List<Requestlistmodel> requestlistmodelList;
    private RequestAdapter mAdapter;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_request, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentuid = mAuth.getCurrentUser().getUid();
        request_reference = FirebaseDatabase.getInstance().getReference().child("Request");

        muserreference = FirebaseDatabase.getInstance().getReference().child("Users");
        request_currentuserreceived = request_reference.child(currentuid);
        request_recyclerview = rootview.findViewById(R.id.request_recyclerview);
        layoutManager = new LinearLayoutManager(this.getContext());
        request_recyclerview.setDrawingCacheEnabled(true);
        request_recyclerview.setItemViewCacheSize(20);
        request_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        request_recyclerview.setLayoutManager(layoutManager);
        requestlistmodelList = new ArrayList<>();
        mAdapter = new RequestAdapter(this.getContext(), requestlistmodelList);
        request_recyclerview.setAdapter(mAdapter);
        requestlistmodelList.clear();
        mAdapter.notifyDataSetChanged();
        loadinrequest();

        return rootview;
    }

    private void loadinrequest() {
        request_currentuserreceived.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    if (snapshot.getValue().toString().equals("received")) {

                        muserreference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    mMap.put(dataSnapshot1.getKey(), dataSnapshot1.getValue());

                                }
                                String name = mMap.get("name").toString();
                                String thumb_image = mMap.get("thumb_image").toString();


                                requestlistmodelList.add(new Requestlistmodel(name, thumb_image, dataSnapshot.getKey()));

                                mAdapter.notifyDataSetChanged();
                                mMap.clear();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                requestlistmodelList.clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //remind: check internet connection
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getContext(), StartActivity.class);
            startActivity(intent);
        }
    }


}
