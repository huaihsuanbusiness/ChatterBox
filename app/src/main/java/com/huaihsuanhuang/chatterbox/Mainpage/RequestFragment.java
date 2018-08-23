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
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Account.ProfileActivity;
import com.huaihsuanhuang.chatterbox.Account.StartActivity;
import com.huaihsuanhuang.chatterbox.Account.UsersActivity;
import com.huaihsuanhuang.chatterbox.Adapter.RequestViewHolder;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.Model.Requestsmodel;
import com.huaihsuanhuang.chatterbox.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private DatabaseReference request_reference, request_currentuserreceived,muserreference;
    private FirebaseAuth mAuth;
    private View rootview;
    private String currentuid;
    private RecyclerView request_recyclerview;
    private LinearLayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requestsmodel, RequestViewHolder> adapter_request;

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
        request_currentuserreceived=request_reference.child(currentuid);
        request_recyclerview=rootview.findViewById(R.id.request_recyclerview);
        layoutManager = new LinearLayoutManager(this.getContext());
        request_recyclerview.setDrawingCacheEnabled(true);
        request_recyclerview.setItemViewCacheSize(20);
        request_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        request_recyclerview.setLayoutManager(layoutManager);


        request_currentuserreceived.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loadinrequestlist();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return rootview;
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

    private void loadinrequestlist() {
        FirebaseRecyclerOptions<Requestsmodel> options =
                new FirebaseRecyclerOptions.Builder<Requestsmodel>()
                        .setQuery(request_currentuserreceived, Requestsmodel.class)
                        .build();
        adapter_request = new FirebaseRecyclerAdapter<Requestsmodel, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Requestsmodel model) {
            String type = model.getRequest_type();
            if (type.equals("received")){

                final String user_id = getRef(position).getKey();
                if (user_id != null && !user_id.isEmpty()) {
                    muserreference.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String username = dataSnapshot.child("name").getValue().toString();
                            String userthumb = dataSnapshot.child("thumb_image").getValue().toString();
                            holder.setName(username);
                            holder.setthumb(userthumb, getContext());
                            holder.setStatus();
                            holder.setItemonclicklistener(new ItemonClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean islongclick) {
                                    Intent intent = new Intent(RequestFragment.this.getContext(), ProfileActivity.class);
                                    intent.putExtra("uid", user_id);
                                    startActivity(intent);
                                }
                            });
                         //   holder.setacceptButton();
                         //   holder.setdeclineButton();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_item, parent, false);
                return new RequestViewHolder(view);
            }
        };

        adapter_request.notifyDataSetChanged();
        request_recyclerview.setAdapter(adapter_request);
        request_recyclerview.setNestedScrollingEnabled(false);
    }

}
