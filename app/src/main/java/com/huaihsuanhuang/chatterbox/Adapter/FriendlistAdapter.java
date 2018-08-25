package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Account.ProfileActivity;
import com.huaihsuanhuang.chatterbox.Messages.ChatActivity;
import com.huaihsuanhuang.chatterbox.Model.FriendUsers;
import com.huaihsuanhuang.chatterbox.Model.Users;
import com.huaihsuanhuang.chatterbox.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendlistAdapter extends RecyclerView.Adapter<FriendlistAdapter.FriendlistViewHolder> {
    private Context context;
   // private List<Users> usersList;
    private List<FriendUsers> friendUsersList;
    private View mView;
  //  private String user_id;

//    public FriendlistAdapter(Context context, List<Users> usersList, String user_id) {
//        this.context = context;
//        this.usersList = usersList;
//        this.user_id = user_id;
//    }

    public FriendlistAdapter(Context context, List<FriendUsers> friendUsersList) {
        this.context = context;
        this.friendUsersList = friendUsersList;
    }

    @NonNull
    @Override
    public FriendlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.users_item, parent, false);
        return new FriendlistViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final FriendlistViewHolder holder, final int position) {
        holder.users_name.setText(friendUsersList.get(position).getName());
        holder.users_status.setText(friendUsersList.get(position).getStatus());
        setuseronline(friendUsersList.get(position).getOnline());
        String thumb = friendUsersList.get(position).getThumb_image();
        if (!thumb.equals("null")) {
            Glide.with(context)
                    .load(thumb)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.users_image);
        } else {
            holder.users_image.setImageResource(R.mipmap.empty_profile);
        }
        holder.users_carditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertdialog(friendUsersList.get(holder.getAdapterPosition()).getKey(), friendUsersList.get(holder.getAdapterPosition()).getName());

            }
        });

    }

    private void setuseronline(String online) {
        ImageView onlinestatus = mView.findViewById(R.id.users_online);
        if (online.equals("true")) {
            onlinestatus.setVisibility(View.VISIBLE);
        } else {
            onlinestatus.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return friendUsersList.size();
    }


    class FriendlistViewHolder extends RecyclerView.ViewHolder {

        CardView users_carditem;
        CircleImageView users_image;
        TextView users_name, users_status;
        //    ImageView users_online;

        FriendlistViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            users_carditem = itemView.findViewById(R.id.users_carditem);
            users_image = itemView.findViewById(R.id.users_image);
            users_name = itemView.findViewById(R.id.users_name);
            users_status = itemView.findViewById(R.id.users_status);
            //   users_online = itemView.findViewById(R.id.users_online);


        }


    }

    private void showAlertdialog(final String user_id, final String username) {
        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        CharSequence options[] = new CharSequence[]{"Opne Profile", "Send Message"};
        ad.setTitle("Select Options");
        ad.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("uid", user_id);
                    context.startActivity(intent);
                }
                if (which == 1) {
                    Intent intent_chat = new Intent(context, ChatActivity.class);
                    intent_chat.putExtra("uid", user_id);
                    intent_chat.putExtra("name", username);
                    context.startActivity(intent_chat);
                }
            }
        });
        ad.show();

    }
}
