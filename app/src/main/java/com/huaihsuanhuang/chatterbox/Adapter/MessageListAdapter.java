package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Messages.ChatActivity;
import com.huaihsuanhuang.chatterbox.Model.MessageListUsers;
import com.huaihsuanhuang.chatterbox.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessagelistViewHolder> {

    private Context context;
    private List<MessageListUsers> messageListUsersList;

    public MessageListAdapter(Context context, List<MessageListUsers> messageListUsersList) {
        this.context = context;
        this.messageListUsersList = messageListUsersList;
    }

    @NonNull
    @Override
    public MessagelistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.users_item, parent, false);
        return new MessagelistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagelistViewHolder holder, final int position) {
        holder.userStatusView.setText(messageListUsersList.get(position).getLastMsg());
        final String chatName = messageListUsersList.get(position).getChatUserName();
        final String chatUid =  messageListUsersList.get(position).getChatUid();
        holder.userNameView.setText(chatName);

        if (!messageListUsersList.get(position).getChatUserThumb().equals("null")) {
            Glide.with(context)
                    .load(messageListUsersList.get(position).getChatUserThumb())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.userImageView);
        } else {
            holder.userImageView.setImageResource(R.mipmap.empty_profile);
        }

        if (messageListUsersList.get(position).getChatUserOnline().equals("true")) {
            holder.userOnlineView.setVisibility(View.VISIBLE);
        } else {
            holder.userOnlineView.setVisibility(View.INVISIBLE);
        }

        holder.users_carditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_chat = new Intent(context, ChatActivity.class);
                intent_chat.putExtra("uid", chatUid);
                intent_chat.putExtra("name", chatName);
                context.startActivity(intent_chat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageListUsersList.size();
    }
}
