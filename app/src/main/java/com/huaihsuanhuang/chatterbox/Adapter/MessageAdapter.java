package com.huaihsuanhuang.chatterbox.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.huaihsuanhuang.chatterbox.Messages.ChatActivity;
import com.huaihsuanhuang.chatterbox.Model.Messagemodel;
import com.huaihsuanhuang.chatterbox.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messagemodel> mMessagelist;
    private String mchatuid;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    String image_currentuser,displayname_currentuser;
    String image_chatuser,displayname_chatuser;
    Context context;

    public MessageAdapter(List<Messagemodel> mMessagelist, String mchatuid, Context context) {
        this.mMessagelist = mMessagelist;
        this.mchatuid = mchatuid;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        mAuth = FirebaseAuth.getInstance();

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String currentuid = mAuth.getCurrentUser().getUid();

        Messagemodel c = mMessagelist.get(position);

        String from_user = c.getFrom();
        String messgae_type= c.getType();
        reference.child("Users").child(currentuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                image_currentuser =dataSnapshot.child("thumb_image").getValue().toString();
                displayname_currentuser  =dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Users").child(mchatuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                image_chatuser =dataSnapshot.child("thumb_image").getValue().toString();
                displayname_chatuser  =dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(from_user.equals(currentuid)){

            holder.chatitem_text.setBackgroundColor(Color.WHITE);
            holder.chatitem_text.setTextColor(Color.BLACK);
            holder.chatitem_imageview.setBackgroundColor(Color.WHITE);
            holder.chatitem_dispalyname.setText(displayname_currentuser);
            if (!image_currentuser.equals("null")) {
                Glide.with(context)
                        .load(image_currentuser)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.chatitem_image);
            } else {
                holder.chatitem_image.setImageResource(R.mipmap.empty_profile);
            }


// remind bind 時間

        }
        else{
            holder.chatitem_text.setBackgroundResource(R.drawable.message_text_background);
            holder.chatitem_text.setTextColor(Color.WHITE);
            holder.chatitem_imageview.setBackgroundResource(R.drawable.message_text_background);
            holder.chatitem_dispalyname.setText(displayname_chatuser);
            if (!image_chatuser.equals("null")) {
                Glide.with(context)
                        .load(image_chatuser)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.chatitem_image);
            } else {
                holder.chatitem_image.setImageResource(R.mipmap.empty_profile);
            }

        }

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formatedtime = formatter.format(date);
        holder.chatitem_time.setText(formatedtime);

        if (messgae_type.equals("text")) {
            holder.chatitem_text.setText(c.getMessage());
            holder.chatitem_text.setVisibility(View.VISIBLE);
            holder.chatitem_imageview.setVisibility(View.GONE);

        }else{
            holder.chatitem_text.setVisibility(View.GONE);
            holder.chatitem_imageview.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(c.getMessage())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.chatitem_imageview);

        }


    }

    public int getItemCount() {

        return mMessagelist.size();
    }

}
