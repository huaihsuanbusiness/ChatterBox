package com.huaihsuanhuang.chatterbox.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huaihsuanhuang.chatterbox.Adapter.UsersViewHolder;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.Model.Users;
import com.huaihsuanhuang.chatterbox.R;


public class UsersActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView users_list;
    private DatabaseReference reference_users;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter_users;
    private ImageView users_image;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        toolbar = findViewById(R.id.toolbar_users);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        users_list = findViewById(R.id.users_list);
        users_list.setLayoutManager(new LinearLayoutManager(this));
        users_list.setDrawingCacheEnabled(true);
        users_list.setItemViewCacheSize(20);
        users_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        reference_users = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadinusers();
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter_users.startListening();

    }

    protected void onStop() {
        super.onStop();

        adapter_users.stopListening();

    }

    private void loadinusers() {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(reference_users, Users.class)
                        .build();
        adapter_users = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                Log.d("loadin", model.getImage() + "\n" + model.getName());
                holder.users_name.setText(model.getName());
                holder.users_status.setText(model.getStatus());
                holder.users_image.setImageResource(R.mipmap.empty_profile);

                if (!model.getImage().equals("null")) {
                    Glide.with(UsersActivity.this)
                            .load(model.getThumb_image())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                            .into(holder.users_image);
                } else {
                    holder.users_image.setImageResource(R.mipmap.empty_profile);
                }

                final String uid = getRef(position).getKey();
                holder.setItemonclicklistener(new ItemonClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean islongclick) {
                        Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_item, parent, false);

                return new UsersViewHolder(view);
            }
        };

        adapter_users.notifyDataSetChanged();
        users_list.setAdapter(adapter_users);
        users_list.setNestedScrollingEnabled(false);


    }
}
