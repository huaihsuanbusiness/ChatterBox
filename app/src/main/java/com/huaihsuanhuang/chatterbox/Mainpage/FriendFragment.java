package com.huaihsuanhuang.chatterbox.Mainpage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.huaihsuanhuang.chatterbox.Adapter.FriendsViewHolder;
import com.huaihsuanhuang.chatterbox.Messages.ChatActivity;
import com.huaihsuanhuang.chatterbox.Model.Friends;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {
    private DatabaseReference mfrienddatabase, muserreference;
    private FirebaseAuth mAuth;
    private String currentuser_uid;
    private View mrootview;
    private RecyclerView friend_list;
    private FirebaseUser user;
    private FirebaseRecyclerAdapter<Friends, FriendsViewHolder> adapter_firend;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mrootview = inflater.inflate(R.layout.fragment_friend, container, false);
        friend_list = mrootview.findViewById(R.id.friend_list);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentuser_uid = mAuth.getCurrentUser().getUid();
        mfrienddatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentuser_uid);
        muserreference = FirebaseDatabase.getInstance().getReference().child("Users");
        mfrienddatabase.keepSynced(true);
        muserreference.keepSynced(true);
        friend_list.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        friend_list.setDrawingCacheEnabled(true);
        friend_list.setItemViewCacheSize(20);
        friend_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mfrienddatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadinfriendlist(); //TODO 這個沒跑進去 直接跳過了 recyclerview 無法顯示
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return mrootview;
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

    private void loadinfriendlist() {
        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(mfrienddatabase, Friends.class)
                        .build();
        //筆記: 因mfrienddatabase只有date 其他資料不能直接加進model會crash
        adapter_firend = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull final Friends model) {
                if (!model.getDate().isEmpty()) {
                    holder.setDate(model.getDate());
                }

                final String user_id = getRef(position).getKey();
                if (user_id != null && !user_id.isEmpty()) {
                    muserreference.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String username = dataSnapshot.child("name").getValue().toString();
                            String userstatus = dataSnapshot.child("status").getValue().toString();
                            String userthumb = dataSnapshot.child("thumb_image").getValue().toString();
                            if (dataSnapshot.hasChild("online")) {
                                String useronline = dataSnapshot.child("online").getValue().toString();
                                holder.setuseronline(useronline);
                            }
                            holder.setName(username);
                            holder.setStatus(userstatus);
                            holder.setthumb(userthumb, getContext());
                            holder.setItemonclicklistener(new ItemonClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean islongclick) {

                                    if (islongclick) {
                                        showAlertdialog(user_id, username);
                                    } else {
                                        Intent intent_chat = new Intent(getContext(), ChatActivity.class);
                                        intent_chat.putExtra("uid", user_id);
                                        intent_chat.putExtra("name", username);
                                        startActivity(intent_chat);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_item, parent, false);

                return new FriendsViewHolder(view);
            }
        };

        adapter_firend.notifyDataSetChanged();
        friend_list.setAdapter(adapter_firend);
        friend_list.setNestedScrollingEnabled(false);


    }

    private void showAlertdialog(final String ad_uid, final String username) {
        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        CharSequence options[] = new CharSequence[]{"Opne Profile", "Send Message"};
        ad.setTitle("Select Options");
        ad.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("uid", ad_uid);
                    startActivity(intent);
                }
                if (which == 1) {
                    Intent intent_chat = new Intent(getContext(), ChatActivity.class);
                    intent_chat.putExtra("uid", ad_uid);
                    intent_chat.putExtra("name", username);
                    startActivity(intent_chat);
                }
            }
        });
        ad.show();

    }
}
