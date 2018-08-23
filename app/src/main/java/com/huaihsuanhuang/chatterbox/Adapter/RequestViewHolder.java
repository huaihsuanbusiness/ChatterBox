package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Account.ProfileActivity;
import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    View mview;
    private Button users_accept,users_decline;

    private ItemonClickListener itemonclicklistener;
    public RequestViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mview = itemView;

    }



    public void setName(String name) {
        TextView username = mview.findViewById(R.id.users_name);
        username.setText(name);
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
    public void setItemonclicklistener(ItemonClickListener itemonclicklistener) {
        this.itemonclicklistener = itemonclicklistener;
    }

    @Override
    public void onClick(View v) {
        itemonclicklistener.onClick(v, getAdapterPosition(), false);
    }

    public void setStatus() {
        TextView userstatus = mview.findViewById(R.id.users_status);
        userstatus.setText("Hi, I sent you a friend request!");
    }

    public void setdeclineButton() {

        users_accept=itemView.findViewById(R.id.users_decline);
        users_decline.setVisibility(View.VISIBLE);

    }
//TODO 有辦法合併？
    public void setacceptButton() {
        users_accept=itemView.findViewById(R.id.users_accept);
        users_accept.setVisibility(View.VISIBLE);
        users_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.current_state=3;

            }
        });
    }
}
