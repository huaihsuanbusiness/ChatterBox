package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.huaihsuanhuang.chatterbox.Model.Seen;
import com.huaihsuanhuang.chatterbox.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessagelistViewHolder>{

    private Context context;
    private String chat_uid,lastdata;
    private List<MessageListUsers> messageListUsersList;
    private List<Seen> seenlist;

    public MessageListAdapter(Context context, String chat_uid, String lastdata, List<MessageListUsers> messageListUsersList, List<Seen> seenlist) {
        this.context = context;
        this.chat_uid = chat_uid;
        this.lastdata = lastdata;
        this.messageListUsersList = messageListUsersList;
        this.seenlist = seenlist;
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
    public void onBindViewHolder(@NonNull MessagelistViewHolder holder, int position) {
        holder.userStatusView.setText(lastdata);

        if(!seenlist.get(position).getSeen().equals("seen")){
            holder.userStatusView.setTypeface(holder.userStatusView.getTypeface(), Typeface.BOLD);
        } else {
            holder.userStatusView.setTypeface(holder.userStatusView.getTypeface(), Typeface.NORMAL);
        }
        final String chat_name = messageListUsersList.get(position).getChat_userName();
        holder.userNameView.setText(chat_name);

        if (!messageListUsersList.get(position).getChat_userThumb().equals("null")) {
            Glide.with(context)
                    .load(messageListUsersList.get(position).getChat_userThumb())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.userImageView);
        } else {
            holder.userImageView.setImageResource(R.mipmap.empty_profile);
        }

                if(messageListUsersList.get(position).getChat_userOnline().equals("true")){

            holder.userOnlineView.setVisibility(View.VISIBLE);

        } else {

            holder.userOnlineView.setVisibility(View.INVISIBLE);

        }
        holder.users_carditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_chat = new Intent(context, ChatActivity.class);
                intent_chat.putExtra("uid", chat_uid);
                intent_chat.putExtra("name", chat_name);
                context.startActivity(intent_chat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return seenlist.size();
    }
}
