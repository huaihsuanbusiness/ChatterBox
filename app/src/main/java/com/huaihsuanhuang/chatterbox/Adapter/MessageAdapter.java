package com.huaihsuanhuang.chatterbox.Adapter;

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
import com.huaihsuanhuang.chatterbox.Model.Messagemodel;
import com.huaihsuanhuang.chatterbox.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messagemodel> mMessagelist;
    private String mchatuid;
    private FirebaseAuth mAuth;

    String image_currentuser, displayname_currentuser;
    String image_chatuser, displayname_chatuser;
    Context context;

    public MessageAdapter(List<Messagemodel> mMessagelist, String mchatuid,
                          String image_currentuser, String displayname_currentuser,
                          String image_chatuser, String displayname_chatuser, Context context) {
        this.mMessagelist = mMessagelist;
        this.mchatuid = mchatuid;
        this.image_currentuser = image_currentuser;
        this.displayname_currentuser = displayname_currentuser;
        this.image_chatuser = image_chatuser;
        this.displayname_chatuser = displayname_chatuser;
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

        String from_user = "";
        String messgae_type = "";
        Messagemodel c = new Messagemodel();
        if (!mMessagelist.isEmpty()) {
            c = mMessagelist.get(position);

            from_user = c.getFrom();
            messgae_type = c.getType();
        }


        if (from_user.equals(currentuid)) {

            holder.chatitem_text.setBackgroundColor(Color.WHITE);
            holder.chatitem_text.setTextColor(Color.BLACK);
            holder.chatitem_imageview.setBackgroundColor(Color.WHITE);
            holder.chatitem_dispalyname.setText(displayname_currentuser);
            if (image_currentuser != null) {
                Glide.with(context)
                        .load(image_currentuser)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.chatitem_image);
            } else {
                holder.chatitem_image.setImageResource(R.mipmap.empty_profile);
            }


// remind bind 時間

        } else {
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

        } else {
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
