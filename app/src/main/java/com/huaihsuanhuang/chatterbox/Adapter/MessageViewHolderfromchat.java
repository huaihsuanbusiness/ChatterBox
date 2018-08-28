package com.huaihsuanhuang.chatterbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaihsuanhuang.chatterbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolderfromchat extends RecyclerView.ViewHolder{

public TextView chatitem_text,chatitem_time,chatitem_dispalyname;
public CircleImageView chatitem_image;
public ImageView chatitem_imageview;



    public MessageViewHolderfromchat(View itemView) {
        super(itemView);

        chatitem_text=itemView.findViewById(R.id.chatitem_text);
        chatitem_image=itemView.findViewById(R.id.chatitem_image);
        chatitem_time=itemView.findViewById(R.id.chatitem_time);
        chatitem_dispalyname=itemView.findViewById(R.id.chatitem_dispalyname);
        chatitem_imageview =itemView.findViewById(R.id.chatitem_imageview);



    }
}
