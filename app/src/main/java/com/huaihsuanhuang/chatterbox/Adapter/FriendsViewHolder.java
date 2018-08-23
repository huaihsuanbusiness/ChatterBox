package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;

public class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    View mview;
    private ItemonClickListener itemonclicklistener;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mview = itemView;
    }

    public void setDate(String date) {
        TextView userdate = mview.findViewById(R.id.users_date);
        userdate.setText(date);

    }

    public void setName(String name) {
        TextView username = mview.findViewById(R.id.users_name);
        username.setText(name);

    }

    public void setStatus(String status) {
        TextView userstatus = mview.findViewById(R.id.users_status);
        userstatus.setText(status);
    }

    public void setuseronline(String online) {
        ImageView onlinestatus = mview.findViewById(R.id.users_online);
        if (online.equals(true)) {
            onlinestatus.setVisibility(View.VISIBLE);
        } else {
            onlinestatus.setVisibility(View.INVISIBLE);
        }

    }

    public void setthumb(String thumb, Context context) {
        ImageView users_image = itemView.findViewById(R.id.users_image);
        if (!thumb.equals("null")) {
            Glide.with(context)
                    .load(thumb)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(users_image);
        } else {
            users_image.setImageResource(R.mipmap.empty_profile);
        }

    }

    //remind set status and photo
    public void setItemonclicklistener(ItemonClickListener itemonclicklistener) {
        this.itemonclicklistener = itemonclicklistener;
    }

    @Override
    public void onClick(View v) {
        itemonclicklistener.onClick(v, getAdapterPosition(), false);
    }
}
