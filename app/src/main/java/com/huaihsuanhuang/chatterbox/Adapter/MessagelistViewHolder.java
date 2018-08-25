package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagelistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    static View mView;
    private ItemonClickListener itemonclicklistener;
    public MessagelistViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mView = itemView;


    }

    public static void setMessage(String message, String isSeen){

        TextView userStatusView =  mView.findViewById(R.id.users_status);
        userStatusView.setText(message);

        if(!isSeen.equals("seen")){
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
        } else {
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
        }

    }

    public static void setName(String name){

        TextView userNameView =  mView.findViewById(R.id.users_name);
        userNameView.setText(name);

    }

    public static void setUserImage(String thumb, Context context){

        CircleImageView userImageView =  mView.findViewById(R.id.users_image);

        if (!thumb.equals("null")) {
            Glide.with(context)
                    .load(thumb)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(userImageView);
        } else {
            userImageView.setImageResource(R.mipmap.empty_profile);
        }

    }

    public static void setUserOnline(String online_status) {

        ImageView userOnlineView =  mView.findViewById(R.id.users_online);

        if(online_status.equals("true")){

            userOnlineView.setVisibility(View.VISIBLE);

        } else {

            userOnlineView.setVisibility(View.INVISIBLE);

        }

    }
    public void setItemonclicklistener(ItemonClickListener itemonclicklistener) {
        this.itemonclicklistener = itemonclicklistener;
    }
    @Override
    public void onClick(View v) {
        itemonclicklistener.onClick(v, getAdapterPosition(), false);
    }
}
