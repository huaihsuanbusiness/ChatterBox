package com.huaihsuanhuang.chatterbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaihsuanhuang.chatterbox.Model.ItemonClickListener;
import com.huaihsuanhuang.chatterbox.R;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView users_name;
    public TextView users_status;
    public ImageView users_image;
    private ItemonClickListener itemonclicklistener;

    public UsersViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        users_image = itemView.findViewById(R.id.users_image);
        users_name = itemView.findViewById(R.id.users_name);
        users_status = itemView.findViewById(R.id.users_status);


    }

    public void setItemonclicklistener(ItemonClickListener itemonclicklistener) {
        this.itemonclicklistener = itemonclicklistener;
    }

    @Override
    public void onClick(View v) {
        itemonclicklistener.onClick(v, getAdapterPosition(), false);
    }
}
