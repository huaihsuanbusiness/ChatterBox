package com.huaihsuanhuang.chatterbox.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagelistViewHolder extends RecyclerView.ViewHolder {

    private ItemonClickListener itemonclicklistener;
    public TextView userStatusView;
    public TextView userNameView;
    public CircleImageView userImageView;
    public ImageView userOnlineView;
    public CardView users_carditem;

    public MessagelistViewHolder(View itemView) {
        super(itemView);


        userStatusView = itemView.findViewById(R.id.users_status);
        userNameView = itemView.findViewById(R.id.users_name);
        userImageView = itemView.findViewById(R.id.users_image);
        userOnlineView = itemView.findViewById(R.id.users_online);
        users_carditem = itemView.findViewById(R.id.users_carditem);
    }


}
