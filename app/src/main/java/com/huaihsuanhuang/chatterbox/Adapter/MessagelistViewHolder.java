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
//        itemView.setOnClickListener(this);


        userStatusView = itemView.findViewById(R.id.users_status);
        userNameView = itemView.findViewById(R.id.users_name);
        userImageView = itemView.findViewById(R.id.users_image);
        userOnlineView = itemView.findViewById(R.id.users_online);
        users_carditem = itemView.findViewById(R.id.users_carditem);
    }


    //    public static void setMessage(String message, String isSeen){
//
//        TextView userStatusView =  mView.findViewById(R.id.users_status);
//        userStatusView.setText(message);
//
//        if(!isSeen.equals("seen")){
//            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
//        } else {
//            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
//        }
//
//    }
//
//    public static void setName(String name){
//
//        TextView userNameView =  mView.findViewById(R.id.users_name);
//        userNameView.setText(name);
//
//    }
//
//    public static void setUserImage(String thumb, Context context){
//
//        CircleImageView userImageView =  mView.findViewById(R.id.users_image);
//
//        if (!thumb.equals("null")) {
//            Glide.with(context)
//                    .load(thumb)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
//                    .into(userImageView);
//        } else {
//            userImageView.setImageResource(R.mipmap.empty_profile);
//        }
//
//    }
//
//    public static void setUserOnline(String online_status) {
//
//        ImageView userOnlineView =  mView.findViewById(R.id.users_online);
//
//        if(online_status.equals("true")){
//
//            userOnlineView.setVisibility(View.VISIBLE);
//
//        } else {
//
//            userOnlineView.setVisibility(View.INVISIBLE);
//
//        }
//
//    }
//    public void setItemonclicklistener(ItemonClickListener itemonclicklistener) {
//        this.itemonclicklistener = itemonclicklistener;
//    }
//
//    @Override
//    public void onClick(View v) {
//        itemonclicklistener.onClick(v, getAdapterPosition(), false);
//    }
}
