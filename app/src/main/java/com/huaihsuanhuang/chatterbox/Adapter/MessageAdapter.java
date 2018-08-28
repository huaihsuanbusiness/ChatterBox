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

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolderfromchat> {

    private List<Messagemodel> mMessagelist;

    private FirebaseAuth mAuth;

    private String currentuser_image, currentuser_name;
    private String chat_image, chat_name;
    private Context context;

    public MessageAdapter(List<Messagemodel> mMessagelist, String currentuser_image, String currentuser_name, String chat_image, String chat_name, Context context) {
        this.mMessagelist = mMessagelist;
        this.currentuser_image = currentuser_image;
        this.currentuser_name = currentuser_name;
        this.chat_image = chat_image;
        this.chat_name = chat_name;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolderfromchat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        mAuth = FirebaseAuth.getInstance();

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.message_item, parent, false);
        return new MessageViewHolderfromchat(view);
    }
//    @Override
//    public int getItemViewType(int position) {
//        // Just as an example, return 0 or 2 depending on position
//        // Note that unlike in ListView adapters, types don't have to be contiguous
//        return position % 2 * 2;
//    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolderfromchat holder, int position) {
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
       //     MessageViewHolderfromcurrent viewHolder0 = (MessageViewHolderfromcurrent) holder;
            holder.chatitem_text.setBackgroundColor(Color.WHITE);
            holder.chatitem_text.setBackgroundResource(R.drawable.message_text_background_white);
            holder.chatitem_text.setTextColor(Color.BLACK);
            holder.chatitem_imageview.setBackgroundColor(Color.WHITE);
            holder.chatitem_dispalyname.setText(currentuser_name);
            if (!currentuser_image.equals("null")) {
                Glide.with(context)
                        .load(currentuser_image)
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
            holder.chatitem_dispalyname.setText(chat_name);
            if (!chat_image.equals("null")) {
                Glide.with(context)
                        .load(chat_image)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.chatitem_image);
            } else {
                holder.chatitem_image.setImageResource(R.mipmap.empty_profile);
            }

        }

        long currentTime = Long.valueOf(c.getTime());
        Date date = new Date(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd HH:mm");
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
